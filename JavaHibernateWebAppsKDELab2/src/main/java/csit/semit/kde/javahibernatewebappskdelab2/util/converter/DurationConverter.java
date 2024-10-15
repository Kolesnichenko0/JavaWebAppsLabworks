package csit.semit.kde.javahibernatewebappskdelab2.util.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Duration;

/**
 * Converter class for converting {@link Duration} to {@link Long} and vice versa.
 * <p>
 * This class is annotated with {@link Converter} to indicate that it is a JPA attribute converter.
 * It provides methods to convert {@link Duration} objects to their equivalent {@link Long} representation in seconds
 * for database storage and to convert {@link Long} values from the database back to {@link Duration} objects.
 * </p>
 * <p>
 * The `DurationConverter` class includes:
 * <ul>
 *   <li>Conversion of {@link Duration} to {@link Long} in the {@code convertToDatabaseColumn} method</li>
 *   <li>Conversion of {@link Long} to {@link Duration} in the {@code convertToEntityAttribute} method</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @see AttributeConverter
 * @see Duration
 * @see Long
 * @since 1.0.0
 */
@Converter
public class DurationConverter implements AttributeConverter<Duration, Long> {

    @Override
    public Long convertToDatabaseColumn(Duration duration) {
        return duration == null ? null : duration.getSeconds();
    }

    @Override
    public Duration convertToEntityAttribute(Long dbData) {
        return dbData == null ? null : Duration.ofSeconds(dbData);
    }
}

