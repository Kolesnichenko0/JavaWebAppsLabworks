package csit.semit.kde.javaspringwebappskdelab3.util.result.service;

import csit.semit.kde.javaspringwebappskdelab3.util.result.controller.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Utility class for handling service results and converting them into appropriate HTTP responses.
 * <p>
 * This class provides a method to handle the result of a service operation and convert it into a {@link ResponseEntity}
 * with the appropriate HTTP status and headers. It supports various statuses such as success, entity not found, validation error,
 * duplicate entry, unknown error, and deletion not allowed.
 * </p>
 * <p>
 * The main functionalities provided by this class include:
 * <ul>
 *   <li>Handling service results and converting them into HTTP responses.</li>
 *   <li>Setting appropriate HTTP headers and status codes based on the service result status.</li>
 *   <li>Returning error messages encapsulated in {@link ErrorResponse} objects for error statuses.</li>
 * </ul>
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * ServiceResult<MyEntity> result = service.performOperation();
 * ResponseEntity<?> response = ServiceResultHandler.handleServiceResult(result);
 * }
 * </pre>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @see ServiceResult
 * @see ErrorResponse
 * @since 1.0.0
 */
public class ServiceResultHandler {
    public static ResponseEntity<?> handleServiceResult(ServiceResult<?> result) {
        HttpHeaders headers = new HttpHeaders();
        String errorMessage = null;

        switch (result.getStatus()) {
            case SUCCESS -> {
                headers.add("Success-Message", "Operation successful");
                if (result.getEntityList() != null) {
                    return ResponseEntity.ok().headers(headers).body(result.getEntityList());
                } else if (result.getEntity() != null) {
                    return ResponseEntity.ok().headers(headers).body(result.getEntity());
                } else {
                    return ResponseEntity.ok().headers(headers).build();
                }
            }
            case ENTITIES_NOT_FOUND -> {
                headers.add("Success-Message", "No entities were found");
                return ResponseEntity.noContent().headers(headers).build();
            }
            case ENTITY_NOT_FOUND -> {
                errorMessage = "The requested entity was not found";
                headers.add("Error-Message", errorMessage);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(headers).body(new ErrorResponse(errorMessage));
            }
            case VALIDATION_ERROR -> {
                errorMessage = "Invalid field - " + result.getFieldName();
                headers.add("Error-Message", errorMessage);
                return ResponseEntity.unprocessableEntity().headers(headers).body(new ErrorResponse(errorMessage));
            }
            case DUPLICATE_ENTRY -> {
                errorMessage = "Duplicate entry found for field - " + result.getFieldName();
                headers.add("Error-Message", errorMessage);
                return ResponseEntity.status(HttpStatus.CONFLICT).headers(headers).body(new ErrorResponse(errorMessage));
            }
            case UNKNOWN_ERROR -> {
                errorMessage = "An unknown error occurred";
                headers.add("Error-Message", errorMessage);
                return ResponseEntity.internalServerError().headers(headers).body(new ErrorResponse(errorMessage));
            }
            case DELETION_NOT_ALLOWED -> {
                errorMessage = "Deletion not allowed. Please delete all tickets associated with this train first.";
                headers.add("Error-Message", errorMessage);
                return ResponseEntity.status(HttpStatus.CONFLICT).headers(headers).body(new ErrorResponse(errorMessage));
            }
            default -> {
                errorMessage = "An unexpected error occurred";
                headers.add("Error-Message", errorMessage);
                return ResponseEntity.internalServerError().headers(headers).body(new ErrorResponse(errorMessage));
            }
        }
    }
}