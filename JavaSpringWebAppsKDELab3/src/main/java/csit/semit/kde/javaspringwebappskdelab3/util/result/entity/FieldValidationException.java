package csit.semit.kde.javaspringwebappskdelab3.util.result.entity;

import lombok.Getter;

/**
 * Exception thrown when a field validation error occurs.
 * <p>
 * This class extends {@link IllegalArgumentException} and is used to indicate that a validation error has occurred
 * for a specific field. It contains the name of the field that caused the validation error.
 * </p>
 * <p>
 * The main functionalities provided by this class include:
 * <ul>
 *   <li>Storing the name of the field that caused the validation error.</li>
 *   <li>Providing constructors to create an exception with or without a detailed message.</li>
 * </ul>
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * if (!isValid(fieldValue)) {
 *     throw new FieldValidationException("Invalid value", "fieldName");
 * }
 * }
 * </pre>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @since 1.0.0
 */
@Getter
public class FieldValidationException extends IllegalArgumentException {
    private final String fieldName;

    public FieldValidationException(String fieldName) {
        this.fieldName = fieldName;
    }

    public FieldValidationException(String message, String fieldName) {
        super(message);
        this.fieldName = fieldName;
    }
}