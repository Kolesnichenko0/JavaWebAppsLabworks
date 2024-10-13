package csit.semit.kde.javahibernatewebappskdelab2.servlets.train;

import csit.semit.kde.javahibernatewebappskdelab2.dto.TrainDTO;
import csit.semit.kde.javahibernatewebappskdelab2.entity.Train;
import csit.semit.kde.javahibernatewebappskdelab2.enums.MovementType;
import csit.semit.kde.javahibernatewebappskdelab2.service.TrainService;
import csit.semit.kde.javahibernatewebappskdelab2.util.criteria.TrainQueryParams;
import csit.semit.kde.javahibernatewebappskdelab2.util.result.FieldName;
import csit.semit.kde.javahibernatewebappskdelab2.util.result.service.ServiceErrorUtil;
import csit.semit.kde.javahibernatewebappskdelab2.util.result.service.ServiceResult;
import csit.semit.kde.javahibernatewebappskdelab2.util.result.service.ServiceStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet("/trains/*")
public class TrainServlet extends HttpServlet {
    private static final String TRAIN_JSP_PATH = "/WEB-INF/views/train/train.jsp";
    private static final String TRAINS_JSP_PATH = "/WEB-INF/views/train/trains.jsp";
    private static final String TRAIN_CREATE_JSP_PATH = "/WEB-INF/views/train/train-create.jsp";
    private static final String TRAINS_RESTORE_JSP_PATH = "/WEB-INF/views/train/train-restore.jsp";
    private TrainService trainService;

    @Override
    public void init() {
        trainService = (TrainService) getServletContext().getAttribute("TrainService");
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if ("PATCH".equalsIgnoreCase(request.getMethod())) {
            doPatch(request, response);
        } else {
            super.service(request, response);
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String qNumber = getDecodedParameter(request, "qNumber");
        String qDeparture = getDecodedParameter(request, "qDeparture");
        String qArrival = getDecodedParameter(request, "qArrival");
        String sortBy = getDecodedParameter(request, "sortBy");
        String depTimeFrom = getDecodedParameter(request, "fDepTimeFrom");
        String depTimeTo = getDecodedParameter(request, "fDepTimeTo");
        String minDuration = getDecodedParameter(request, "fMinDuration");
        String maxDuration = getDecodedParameter(request, "fMaxDuration");
        String movementType = getDecodedParameter(request, "fMovementType");

        String acceptHeader = request.getHeader("Accept");

        if (qNumber == null && qDeparture == null && qArrival == null && sortBy == null && depTimeFrom == null && depTimeTo == null && minDuration == null && maxDuration == null && movementType == null) {
            if (pathInfo == null || pathInfo.isEmpty()) {
                if (acceptHeader != null && acceptHeader.contains("application/json")) {
                    ServiceResult<TrainDTO> result = trainService.getAllList();
                    handleGETResult(result, response);
                } else {
                    request.getRequestDispatcher(TRAINS_JSP_PATH).forward(request, response);
                }
                return;
            } else if (pathInfo.equals("/create")) {
                request.getRequestDispatcher(TRAIN_CREATE_JSP_PATH).forward(request, response);
                return;
            } else if (pathInfo.equals("/restore")) {
                request.getRequestDispatcher(TRAINS_RESTORE_JSP_PATH).forward(request, response);
                return;
            }  else if (pathInfo.equals("/deleted")) {
                ServiceResult<TrainDTO> result = trainService.findDeletedEntities();
                handleGETResult(result, response);
                return;
            } else if (!pathInfo.matches("/\\d+")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
                response.setHeader("Error-Message", "Invalid URL or parameters");
                return;
            }

            if (acceptHeader != null && acceptHeader.contains("text/html")) {
                request.getRequestDispatcher(TRAIN_JSP_PATH).forward(request, response);
            } else {
                Long trainId = Long.parseLong(pathInfo.substring(1));
                ServiceResult<TrainDTO> result = trainService.findById(trainId);
                handleGETResult(result, response);
            }
        } else {
            // Handle search query
            if (acceptHeader != null && acceptHeader.contains("text/html")) {
                request.getRequestDispatcher(TRAINS_JSP_PATH).forward(request, response);
            } else {
                TrainQueryParams.Builder queryParamsBuilder = new TrainQueryParams.Builder();

                if (qNumber != null) {
                    queryParamsBuilder.searchingNumber(qNumber);
                } else if (qDeparture != null && qArrival != null) {
                    queryParamsBuilder.searchingDepartureStation(qDeparture)
                            .searchingArrivalStation(qArrival);
                } else if (qDeparture != null) {
                    queryParamsBuilder.searchingDepartureStation(qDeparture);
                } else if (qArrival != null) {
                    queryParamsBuilder.searchingArrivalStation(qArrival);
                }
                try {
                    processFiltersAndSort(depTimeFrom, depTimeTo, minDuration, maxDuration, movementType, sortBy, queryParamsBuilder);
                } catch (IllegalArgumentException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
                    response.setHeader("Error-Message", "Invalid URL or parameters");
                    return;
                }

                ServiceResult<TrainDTO> result = trainService.findAndFilterAndSortByQueryParams(queryParamsBuilder.build());
                handleGETResult(result, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (!isValidContentType(request, response)) return;

        JSONObject jsonObject = parseRequestBody(request, response);
        if (jsonObject == null) return;

        TrainDTO trainDTO = parseTrainDTO(jsonObject, response);
        if (trainDTO == null) return;

        ServiceResult<TrainDTO> result = trainService.create(trainDTO);
        handlePOSTServiceResult(result, request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (!isValidContentType(request, response)) return;

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || !pathInfo.matches("/\\d+")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
            response.setHeader("Error-Message", "Invalid URL or parameters");
            return;
        }

        long id;
        try {
            id = Long.parseLong(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
            response.setHeader("Error-Message", "Invalid field - " + FieldName.ID.getRealName());
            return;
        }

        JSONObject jsonObject = parseRequestBody(request, response);
        if (jsonObject == null) return;

        TrainDTO trainDTO = parseTrainDTO(jsonObject, response);
        if (trainDTO == null) return;

        ServiceResult<TrainDTO> result = trainService.update(id, trainDTO);
        handlePUTResult(result, request, response);
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (!isValidContentType(request, response)) return;

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || !pathInfo.matches("/\\d+")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
            response.setHeader("Error-Message", "Invalid URL or parameters");
            return;
        }

        long id;
        try {
            id = Long.parseLong(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
            response.setHeader("Error-Message", "Invalid field - " + FieldName.ID.getRealName());
            return;
        }

        ServiceResult<TrainDTO> result = trainService.delete(id);
        handlePATCHResult(result, request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || !pathInfo.matches("/\\d+")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
            response.setHeader("Error-Message", "Invalid URL or parameters");
            return;
        }

        long id;
        try {
            id = Long.parseLong(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
            response.setHeader("Error-Message", "Invalid ID format");
            return;
        }

        ServiceResult<TrainDTO> result = trainService.permanentDelete(id);
        handleDELETEResult(result, request, response);
    }

    private void processFiltersAndSort(String depTimeFrom, String depTimeTo, String minDuration, String maxDuration, String movementType, String sortBy, TrainQueryParams.Builder queryParamsBuilder) {
        if (depTimeFrom != null && !depTimeFrom.isEmpty()) {
            queryParamsBuilder.filteringFrom(LocalTime.parse(depTimeFrom.replace("-", ":")));
        }
        if (depTimeTo != null && !depTimeTo.isEmpty()) {
            queryParamsBuilder.filteringTo(LocalTime.parse(depTimeTo.replace("-", ":")));
        }
        if (minDuration != null && !minDuration.isEmpty()) {
            queryParamsBuilder.filteringMinDuration(Duration.ofMinutes(Integer.parseInt(minDuration)));
        }
        if (maxDuration != null && !maxDuration.isEmpty()) {
            queryParamsBuilder.filteringMaxDuration(Duration.ofMinutes(Integer.parseInt(maxDuration)));
        }
        if (movementType != null && !movementType.isEmpty()) {
            Set<MovementType> movementTypes = Arrays.stream(movementType.split("T"))
                    .map(MovementType::getByDisplayName)
                    .collect(Collectors.toSet());
            queryParamsBuilder.filteringMovementTypes(movementTypes);
        }
        if (sortBy != null && !sortBy.isEmpty()) {
            processSort(sortBy, queryParamsBuilder);
        }
    }

    private void processSort(String sort, TrainQueryParams.Builder builder) {
        switch (sort) {
            case "numberAsc":
                builder.sortedByTrainNumberAsc(true);
                break;
            case "numberDesc":
                builder.sortedByTrainNumberAsc(false);
                break;
            case "durationAsc":
                builder.sortedByDurationAsc(true);
                break;
            case "durationDesc":
                builder.sortedByDurationAsc(false);
                break;
            case "departureTimeAsc":
                builder.sortedByDepartureTimeAsc(true);
                break;
            case "departureTimeDesc":
                builder.sortedByDepartureTimeAsc(false);
                break;
        }
    }

    private TrainDTO parseTrainDTO(JSONObject jsonObject, HttpServletResponse response) throws IOException {
        try {
            String number = jsonObject.getString(FieldName.NUMBER.getRealName());
            String departureStation = jsonObject.getString(FieldName.DEPARTURE_STATION.getRealName());
            String arrivalStation = jsonObject.getString(FieldName.ARRIVAL_STATION.getRealName());
            MovementType movementType = MovementType.getByDisplayName(jsonObject.getString(FieldName.MOVEMENT_TYPE.getRealName()));
            LocalTime departureTime = LocalTime.parse(jsonObject.getString(FieldName.DEPARTURE_TIME.getRealName()));
            Duration duration = Duration.ofMinutes(jsonObject.getLong(FieldName.DURATION.getRealName()));

            return new TrainDTO(null, number, departureStation, arrivalStation, movementType, departureTime, duration);
        } catch (JSONException e) {
            FieldName invalidField = FieldName.getByRealName(e.getMessage().split(" ")[1]);
            setErrorResponse(response, 422, "Invalid field - " + invalidField.getRealName());
        } catch (DateTimeParseException e) {
            setErrorResponse(response, 422, "Invalid field - " + FieldName.DEPARTURE_TIME.getRealName());
        } catch (Exception e) {
            setErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unknown error occurred. Please try again later.");
        }
        return null;
    }

    private JSONObject parseRequestBody(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String jsonBuffer;
        try (BufferedReader reader = request.getReader()) {
            jsonBuffer = reader.lines().collect(Collectors.joining());
        } catch (IOException e) {
            setErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Error reading JSON data");
            return null;
        }

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonBuffer);
        } catch (JSONException e) {
            setErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON format");
            return null;
        }

        return jsonObject;
    }

    private JSONObject processTrainEntity(JSONObject jsonObject) {
        MovementType movementType = (MovementType) jsonObject.get("movementType");
        jsonObject.put("movementType", movementType.getDisplayName());

        String duration = jsonObject.getString("duration");
        jsonObject.put("duration", formatDuration(duration));

        return jsonObject;
    }

    private String formatDuration(String duration) {
        // Format duration to "8 год. 35 хв."
        Duration d = Duration.parse(duration);
        long hours = d.toHours();
        long minutes = d.toMinutes() % 60;
        return String.format("%d год. %d хв.", hours, minutes);
    }

    private void setErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        response.setHeader("Error-Message", message);
    }

    private boolean isValidContentType(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String contentType = request.getContentType();
        if (contentType == null || !contentType.contains("application/json")) {
            setErrorResponse(response, HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Content type must be application/json");
            return false;
        }
        return true;
    }

    private void handleGETResult(ServiceResult<?> result, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (result.getStatus() == ServiceStatus.SUCCESS) {
            response.setStatus(HttpServletResponse.SC_OK); // 200
            if (result.getEntityList() != null) {
                JSONArray jsonResponse = new JSONArray();
                for (Object entity : result.getEntityList()) {
                    JSONObject jsonObject = new JSONObject(entity);
                    jsonResponse.put(processTrainEntity(jsonObject));
                }
                response.getWriter().write(jsonResponse.toString());
            } else {
                JSONObject jsonResponse = new JSONObject(result.getEntity());
                response.getWriter().write(jsonResponse.toString());
            }
            response.setHeader("Success-Message", "Operation successful");
        } else if (result.getStatus() == ServiceStatus.ENTITIES_NOT_FOUND || result.getStatus() == ServiceStatus.ENTITY_NOT_FOUND) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204
            response.setHeader("Success-Message", ServiceErrorUtil.getMessage(result));
        } else {
            int statusCode = ServiceErrorUtil.getHttpErrorStatusCode(result.getStatus());
            response.setStatus(statusCode);
            String message = ServiceErrorUtil.getMessage(result);
            response.setHeader("Error-Message", message);
        }
    }

    private void handlePOSTServiceResult(ServiceResult<?> result, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (result.getStatus() == ServiceStatus.SUCCESS) {
            response.setStatus(HttpServletResponse.SC_CREATED);
            JSONObject jsonResponse = new JSONObject(result.getEntity());
            response.getWriter().write(jsonResponse.toString());
            String newResourceUrl = request.getRequestURL().toString() + "/" + ((TrainDTO) result.getEntity()).getId();
            response.setHeader("Location", newResourceUrl);
            response.setHeader("Success-Message", "Resource created successfully");
        } else {
            int statusCode = ServiceErrorUtil.getHttpErrorStatusCode(result.getStatus());
            response.setStatus(statusCode);
            String message = ServiceErrorUtil.getMessage(result);
            response.setHeader("Error-Message", message);
        }
    }

    private void handlePUTResult(ServiceResult<?> result, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String resourceUrl = request.getRequestURL().toString();
        handleResult(result, request, response, resourceUrl);
    }

    private void handlePATCHResult(ServiceResult<?> result, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String successLocation = request.getContextPath() + "/trains";
        handleResult(result, request, response, successLocation);
    }

    private void handleDELETEResult(ServiceResult<?> result, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String successLocation = request.getContextPath() + "/trains";
        handleResult(result, request, response, successLocation);
    }

    private void handleResult(ServiceResult<?> result, HttpServletRequest request, HttpServletResponse response, String successLocation) throws IOException {
        if (result.getStatus() == ServiceStatus.SUCCESS) {
            response.setStatus(HttpServletResponse.SC_OK); // 200
            JSONObject jsonResponse = new JSONObject(result.getEntity());
            response.getWriter().write(jsonResponse.toString());
            response.setHeader("Success-Message", "Operation successful");
            response.setHeader("Location", successLocation);
        } else {
            int statusCode = ServiceErrorUtil.getHttpErrorStatusCode(result.getStatus());
            response.setStatus(statusCode);
            String message = ServiceErrorUtil.getMessage(result);
            response.setHeader("Error-Message", message);
        }
    }
    private String getDecodedParameter(HttpServletRequest request, String paramName) {
        String param = request.getParameter(paramName);
        return param != null ? URLDecoder.decode(param, StandardCharsets.UTF_8) : null;
    }
}