package csit.semit.kde.javahibernatewebappskdelab2.util.result;

import lombok.Getter;

/**
 * Exception thrown when a field validation error occurs.
 * <p>
 * This exception is used to indicate that a validation error has occurred for a specific field. It extends
 * {@link IllegalArgumentException} and includes the {@link FieldName} that caused the validation error.
 * </p>
 * <p>
 * The `FieldValidationException` class includes:
 * <ul>
 *   <li>The field name associated with the validation error: {@code fieldName}</li>
 * </ul>
 * </p>
 * <p>
 * This class uses Lombok's {@link Getter} annotation to automatically generate getter methods for the field name.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see FieldName
 * @since 1.0.0
 */
@Getter
public class FieldValidationException extends IllegalArgumentException {
    private final FieldName fieldName;

    public FieldValidationException(FieldName fieldName) {
        this.fieldName = fieldName;
    }

    public FieldValidationException(String message, FieldName fieldName) {
        super(message);
        this.fieldName = fieldName;
    }

}
