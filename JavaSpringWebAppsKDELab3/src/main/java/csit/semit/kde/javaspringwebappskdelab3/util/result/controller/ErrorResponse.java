package csit.semit.kde.javaspringwebappskdelab3.util.result.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Class representing an error response.
 * <p>
 * This class is used to encapsulate error messages that are returned to the client in case of an error.
 * It contains a single field, `message`, which holds the error message.
 * </p>
 * <p>
 * The main functionalities provided by this class include:
 * <ul>
 *   <li>Encapsulating error messages in a structured format.</li>
 *   <li>Providing a getter method to access the error message.</li>
 * </ul>
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * ErrorResponse errorResponse = new ErrorResponse("An error occurred");
 * String errorMessage = errorResponse.getMessage();
 * }
 * </pre>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final String message;
}