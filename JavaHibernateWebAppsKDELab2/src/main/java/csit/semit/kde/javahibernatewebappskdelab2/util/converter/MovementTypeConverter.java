package csit.semit.kde.javahibernatewebappskdelab2.util.converter;

import csit.semit.kde.javahibernatewebappskdelab2.enums.MovementType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converter class for converting {@link MovementType} to {@link String} and vice versa.
 * <p>
 * This class is annotated with {@link Converter} to indicate that it is a JPA attribute converter.
 * It provides methods to convert {@link MovementType} objects to their equivalent {@link String} representation
 * for database storage and to convert {@link String} values from the database back to {@link MovementType} objects.
 * </p>
 * <p>
 * The `MovementTypeConverter` class includes:
 * <ul>
 *   <li>Conversion of {@link MovementType} to {@link String} in the {@code convertToDatabaseColumn} method</li>
 *   <li>Conversion of {@link String} to {@link MovementType} in the {@code convertToEntityAttribute} method</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @see AttributeConverter
 * @see MovementType
 * @see String
 * @since 1.0.0
 */
@Converter
public class MovementTypeConverter implements AttributeConverter<MovementType, String> {

    @Override
    public String convertToDatabaseColumn(MovementType movementType) {
        return (movementType == null) ? null : movementType.getDisplayName();
    }

    @Override
    public MovementType convertToEntityAttribute(String dbData) {
        return (dbData == null || dbData.isEmpty()) ? null : MovementType.getByDisplayName(dbData);
    }
}
