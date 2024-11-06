package csit.semit.kde.javaspringwebappskdelab3.controller.trainticket;

import csit.semit.kde.javaspringwebappskdelab3.controller.EntityBaseRestController;
import csit.semit.kde.javaspringwebappskdelab3.dto.trainticket.TrainTicketDTO;
import csit.semit.kde.javaspringwebappskdelab3.service.trainticket.TrainTicketService;
import csit.semit.kde.javaspringwebappskdelab3.util.criteria.trainticket.TrainTicketQueryParams;
import csit.semit.kde.javaspringwebappskdelab3.util.result.controller.ErrorResponse;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceResult;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceResultHandler;
import csit.semit.kde.javaspringwebappskdelab3.util.result.entity.FieldValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

/**
 * REST controller for handling train ticket-related requests.
 * <p>
 * This controller is mapped to the "/api/train-tickets" URL and provides methods for handling CRUD operations
 * and search functionalities related to train tickets.
 * It extends the {@link EntityBaseRestController} to inherit common functionality for REST controllers.
 * </p>
 * <p>
 * The main functionalities provided by this controller include:
 * <ul>
 *   <li>Finding a train ticket by its ID.</li>
 *   <li>Saving a new train ticket.</li>
 *   <li>Deleting a train ticket by its ID.</li>
 *   <li>Listing all train tickets with optional search and filter parameters.</li>
 *   <li>Partially updating a train ticket.</li>
 * </ul>
 * </p>
 * <p>
 * This controller uses Spring MVC annotations to map requests and handle them appropriately.
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * @RestController
 * @RequestMapping("/api/train-tickets")
 * public class TrainTicketRestControllerEntity extends EntityBaseRestController<TrainTicketDTO> {
 *     // methods and logic
 * }
 * }
 * </pre>
 * </p>
 * <p>
 * Dependencies:
 * <ul>
 *   <li>{@link TrainTicketService}</li>
 *   <li>{@link TrainTicketQueryParams}</li>
 *   <li>{@link ServiceResult}</li>
 *   <li>{@link ServiceResultHandler}</li>
 *   <li>{@link ErrorResponse}</li>
 *   <li>{@link FieldValidationException}</li>
 *   <li>{@link HttpServletRequest}</li>
 *   <li>{@link HttpHeaders}</li>
 *   <li>{@link ResponseEntity}</li>
 * </ul>
 * </p>
 * <p>
 * Annotations used:
 * <ul>
 *   <li>{@link RestController}</li>
 *   <li>{@link RequestMapping}</li>
 *   <li>{@link GetMapping}</li>
 *   <li>{@link PatchMapping}</li>
 *   <li>{@link RequestParam}</li>
 *   <li>{@link PathVariable}</li>
 *   <li>{@link RequestBody}</li>
 * </ul>
 * </p>
 * <p>
 * Note: This controller assumes that the base path for API endpoints is "/api/train-tickets".
 * </p>
 * <p>
 * Error handling:
 * <ul>
 *   <li>Handles {@link FieldValidationException} and responds with a bad request status and error message.</li>
 *   <li>Handles {@link DateTimeParseException} and responds with a bad request status and error message.</li>
 * </ul>
 * </p>
 * <p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see EntityBaseRestController
 * @see TrainTicketService
 * @see TrainTicketQueryParams
 * @see ServiceResult
 * @see ServiceResultHandler
 * @see ErrorResponse
 * @see FieldValidationException
 * @see HttpServletRequest
 * @see HttpHeaders
 * @see ResponseEntity
 * @see RestController
 * @see RequestMapping
 * @see GetMapping
 * @see PatchMapping
 * @see RequestParam
 * @see PathVariable
 * @see RequestBody
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/train-tickets")
public class TrainTicketRestControllerEntity extends EntityBaseRestController<TrainTicketDTO> {
    private final TrainTicketService trainTicketService;

    @Autowired
    public TrainTicketRestControllerEntity(TrainTicketService trainTicketService) {
        this.trainTicketService = trainTicketService;
    }

    @Override
    protected ServiceResult<TrainTicketDTO> findById(Long id) {
        return trainTicketService.findById(id);
    }

    @Override
    protected ServiceResult<TrainTicketDTO> saveEntity(TrainTicketDTO dto) {
        return trainTicketService.save(dto);
    }

    @Override
    protected ServiceResult<Void> deleteEntity(Long id) {
        return trainTicketService.delete(id);
    }

    @Override
    protected String getBasePath() {
        return "/api/train-tickets";
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) String qPassengerSurname,
            @RequestParam(required = false) String qPassengerPassportNumber,
            @RequestParam(required = false) String qDepartureDate,
            @RequestParam(required = false) String fDepartureDateFrom,
            @RequestParam(required = false) String fDepartureDateTo) {
        try {
            TrainTicketQueryParams.Builder queryParamsBuilder = new TrainTicketQueryParams.Builder();

            if (qPassengerSurname != null) {
                queryParamsBuilder.searchingPassengerSurname(qPassengerSurname);
            } else if (qPassengerPassportNumber != null) {
                queryParamsBuilder.searchingPassportNumber(qPassengerPassportNumber);
            } else if (qDepartureDate != null) {
                queryParamsBuilder.searchingDepartureDate(LocalDate.parse(qDepartureDate));
            }

            if (qDepartureDate == null) {
                if (fDepartureDateFrom != null && !fDepartureDateFrom.isEmpty()) {
                    queryParamsBuilder.filteringDepartureDateFrom(LocalDate.parse(fDepartureDateFrom));
                }
                if (fDepartureDateTo != null && !fDepartureDateTo.isEmpty()) {
                    queryParamsBuilder.filteringDepartureDateTo(LocalDate.parse(fDepartureDateTo));
                }
            }

            ServiceResult<TrainTicketDTO> result = trainTicketService.findTicketTrains(queryParamsBuilder.build());
            return ServiceResultHandler.handleServiceResult(result);
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

    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePartial(@PathVariable Long id, @RequestBody Map<String, Object> updates, HttpServletRequest request) {
        System.out.println("Request: " + request.getMethod() + " " + request.getRequestURI());
        System.out.println("Updates: " + updates);
        ServiceResult<TrainTicketDTO> result = trainTicketService.updatePartial(id, updates);
        return ServiceResultHandler.handleServiceResult(result);
    }
}