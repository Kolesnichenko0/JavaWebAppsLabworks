package csit.semit.kde.javaspringwebappskdelab3.controller.train;

import csit.semit.kde.javaspringwebappskdelab3.controller.EntityBaseController;
import csit.semit.kde.javaspringwebappskdelab3.controller.EntityBaseRestController;
import csit.semit.kde.javaspringwebappskdelab3.dto.train.TrainExportRequestDTO;
import csit.semit.kde.javaspringwebappskdelab3.dto.train.TrainDTO;
import csit.semit.kde.javaspringwebappskdelab3.dto.trainticket.TrainTicketDTO;
import csit.semit.kde.javaspringwebappskdelab3.dto.trainticket.TrainTicketExportRequestDTO;
import csit.semit.kde.javaspringwebappskdelab3.enums.train.MovementType;
import csit.semit.kde.javaspringwebappskdelab3.service.train.TrainExportService;
import csit.semit.kde.javaspringwebappskdelab3.service.train.TrainService;
import csit.semit.kde.javaspringwebappskdelab3.service.trainticket.TrainTicketExportService;
import csit.semit.kde.javaspringwebappskdelab3.service.trainticket.TrainTicketService;
import csit.semit.kde.javaspringwebappskdelab3.util.criteria.train.TrainQueryParams;
import csit.semit.kde.javaspringwebappskdelab3.util.criteria.trainticket.TrainTicketQueryParams;
import csit.semit.kde.javaspringwebappskdelab3.util.result.controller.ErrorResponse;
import csit.semit.kde.javaspringwebappskdelab3.util.result.entity.FieldValidationException;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceResult;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceResultHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controller for handling train-related requests.
 * <p>
 * This controller is mapped to the "/trains" URL and provides methods for handling requests related to trains.
 * It extends the {@link EntityBaseController} to inherit common functionality for entity controllers.
 * </p>
 * <p>
 * The main functionalities provided by this controller include:
 * <ul>
 *   <li>Returning the base path for train-related views.</li>
 *   <li>Returning the entity name for train-related operations.</li>
 *   <li>Handling requests to view tickets for a specific train.</li>
 * </ul>
 * </p>
 * <p>
 * The {@code trainTickets} method handles GET requests to "/trains/{id}/tickets" and adds attributes to the model
 * to indicate whether the user is logged in and whether the request is for a specific train.
 * </p>
 * <p>
 * This controller uses Spring MVC annotations to map requests and handle them appropriately.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see EntityBaseController
 * @see org.springframework.stereotype.Controller
 * @see org.springframework.ui.Model
 * @see org.springframework.web.bind.annotation.GetMapping
 * @see org.springframework.web.bind.annotation.RequestMapping
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/trains")
public class TrainRestControllerEntity extends EntityBaseRestController<TrainDTO> {
    private final TrainService trainService;
    private final TrainTicketService trainTicketService;

    private final TrainExportService trainExportService;
    private final TrainTicketExportService trainTicketExportService;

    @Autowired
    public TrainRestControllerEntity(TrainService trainService, TrainTicketService trainTicketService, TrainExportService trainExportService, TrainTicketExportService trainTicketExportService) {
        this.trainService = trainService;
        this.trainTicketService = trainTicketService;
        this.trainExportService = trainExportService;
        this.trainTicketExportService = trainTicketExportService;
    }

    @Override
    protected ServiceResult<TrainDTO> findById(Long id) {
        return trainService.findById(id);
    }

    @Override
    protected ServiceResult<TrainDTO> saveEntity(TrainDTO dto) {
        return trainService.save(dto);
    }

    @Override
    protected ServiceResult<Void> deleteEntity(Long id) {
        return trainService.delete(id);
    }

    @Override
    protected String getBasePath() {
        return "/api/trains";
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) String qNumber,
            @RequestParam(required = false) String qDeparture,
            @RequestParam(required = false) String qArrival,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String fDepTimeFrom,
            @RequestParam(required = false) String fDepTimeTo,
            @RequestParam(required = false) String fMinDuration,
            @RequestParam(required = false) String fMaxDuration,
            @RequestParam(required = false) String fMovementType) {

        try {
            TrainQueryParams.Builder queryParamsBuilder = new TrainQueryParams.Builder();

            // Search criteria
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

            // Filters and sorting
            processFiltersAndSort(fDepTimeFrom, fDepTimeTo, fMinDuration, fMaxDuration,
                    fMovementType, sortBy, queryParamsBuilder);

            ServiceResult<TrainDTO> result = trainService.findTrains(queryParamsBuilder.build());
            return ServiceResultHandler.handleServiceResult(result);

        } catch (IllegalArgumentException e) {
            String errorMessage = "Invalid parameters provided";
            HttpHeaders headers = new HttpHeaders();
            headers.add("Error-Message", errorMessage);
            return ResponseEntity.badRequest().headers(headers).body(new ErrorResponse(errorMessage));
        }
    }

    @GetMapping("/{id}/tickets")
    public ResponseEntity<?> getTickets(@PathVariable Long id,
                                        @RequestParam(required = false) String qPassengerSurname,
                                        @RequestParam(required = false) String qPassengerPassportNumber,
                                        @RequestParam(required = false) Integer qCarriageNumber,
                                        @RequestParam(required = false) String qDepartureDate,
                                        @RequestParam(required = false) String fDepartureDateFrom,
                                        @RequestParam(required = false) String fDepartureDateTo) {
        try {
            TrainTicketQueryParams.Builder queryParamsBuilder = new TrainTicketQueryParams.Builder();
            ServiceResult<?> result;

            if (qPassengerSurname != null) {
                queryParamsBuilder.searchingPassengerSurname(qPassengerSurname);
            } else if (qPassengerPassportNumber != null) {
                queryParamsBuilder.searchingPassportNumber(qPassengerPassportNumber);
            } else if (qDepartureDate != null) {
                queryParamsBuilder.searchingDepartureDate(LocalDate.parse(qDepartureDate));
                if (qCarriageNumber != null) {
                    queryParamsBuilder.searchingCarriageNumber(qCarriageNumber);
                }
            }

            if (qDepartureDate == null) {
                if (fDepartureDateFrom != null && !fDepartureDateFrom.isEmpty()) {
                    queryParamsBuilder.filteringDepartureDateFrom(LocalDate.parse(fDepartureDateFrom));
                }
                if (fDepartureDateTo != null && !fDepartureDateTo.isEmpty()) {
                    queryParamsBuilder.filteringDepartureDateTo(LocalDate.parse(fDepartureDateTo));
                }
            }

            TrainTicketQueryParams queryParamsWithoutId = queryParamsBuilder.build();
            if (queryParamsWithoutId.isEmpty()) {
                return ServiceResultHandler.handleServiceResult(trainService.getTrainTickets(id));
            } else {
                queryParamsBuilder.searchingTrainId(id);
                TrainTicketQueryParams queryParams = queryParamsBuilder.build();
                return ServiceResultHandler.handleServiceResult(trainTicketService.findTicketTrains(queryParams));
            }
        } catch (FieldValidationException e) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Error-Message", e.getMessage());
            return ResponseEntity.badRequest().headers(headers).body(new ErrorResponse(e.getMessage()));
        } catch (DateTimeParseException e) {
            String errorMessage = "Invalid parameter-filter format";
            HttpHeaders headers = new HttpHeaders();
            headers.add("Error-Message", errorMessage);
            return ResponseEntity.badRequest().headers(headers).body(new ErrorResponse(errorMessage));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody TrainDTO trainDTO,
                                    HttpServletRequest request) {
        ServiceResult<TrainDTO> result = trainService.update(id, trainDTO);
        return ServiceResultHandler.handleServiceResult(result);
    }

    private void processFiltersAndSort(String depTimeFrom, String depTimeTo,
                                       String minDuration, String maxDuration,
                                       String movementType, String sortBy,
                                       TrainQueryParams.Builder queryParamsBuilder) {
        try {
            if (depTimeFrom != null && !depTimeFrom.isEmpty()) {
                queryParamsBuilder.filteringFrom(LocalTime.parse(depTimeFrom.replace("-", ":")));
            }
            if (depTimeTo != null && !depTimeTo.isEmpty()) {
                queryParamsBuilder.filteringTo(LocalTime.parse(depTimeTo.replace("-", ":")));
            }
            if (minDuration != null && !minDuration.isEmpty()) {
                queryParamsBuilder.filteringMinDuration(Duration.ofMinutes(Long.parseLong(minDuration)));
            }
            if (maxDuration != null && !maxDuration.isEmpty()) {
                queryParamsBuilder.filteringMaxDuration(Duration.ofMinutes(Long.parseLong(maxDuration)));
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
        } catch (DateTimeParseException | NumberFormatException e) {
            throw new IllegalArgumentException("Invalid parameter-filter format");
        }
    }

    private void processSort(String sort, TrainQueryParams.Builder builder) {
        switch (sort) {
            case "numberAsc" -> builder.sortedByTrainNumberAsc(true);
            case "numberDesc" -> builder.sortedByTrainNumberAsc(false);
            case "durationAsc" -> builder.sortedByDurationAsc(true);
            case "durationDesc" -> builder.sortedByDurationAsc(false);
            case "departureTimeAsc" -> builder.sortedByDepartureTimeAsc(true);
            case "departureTimeDesc" -> builder.sortedByDepartureTimeAsc(false);
            default -> throw new IllegalArgumentException("Invalid parameter-sort parameter");
        }
    }

    @PostMapping("/export")
    public ResponseEntity<?> exportTrains(@RequestBody TrainExportRequestDTO request, Authentication authentication) {
        String username = authentication.getName();
        ServiceResult<TrainDTO> exportResult = trainExportService.exportAndSendTrains(request, username);
        return ServiceResultHandler.handleServiceResult(exportResult);
    }

    @PostMapping("/{id}/tickets/export")
    public ResponseEntity<?> exportTrainTickets(@RequestBody TrainTicketExportRequestDTO request,
                                                Authentication authentication, @PathVariable Long id) {
        String username = authentication.getName();
        ServiceResult<TrainTicketDTO> exportResult = trainTicketExportService.exportAndSendTrainTickets(request, id, username);
        return ServiceResultHandler.handleServiceResult(exportResult);
    }
}