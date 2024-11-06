package csit.semit.kde.javaspringwebappskdelab3.util.fieldname.train;

import lombok.Getter;

import java.util.Arrays;

/**
 * Enum representing the various field names used in the application.
 * <p>
 * This enum is used to define the different field names that can be used in the application. Each field name has a
 * corresponding real name that is used for display purposes. The enum provides methods to get the real name of a field
 * and to retrieve a field name by its real name.
 * </p>
 * <p>
 * The `FieldName` enum includes:
 * <ul>
 *   <li>{@code ID} - Represents the ID field.</li>
 *   <li>{@code NUMBER} - Represents the number field.</li>
 *   <li>{@code ARRIVAL_STATION} - Represents the arrival station field.</li>
 *   <li>{@code DEPARTURE_STATION} - Represents the departure station field.</li>
 *   <li>{@code DEPARTURE_TIME} - Represents the departure time field.</li>
 *   <li>{@code MOVEMENT_TYPE} - Represents the movement type field.</li>
 *   <li>{@code DURATION} - Represents the duration field.</li>
 * </ul>
 * </p>
 * <p>
 * This class uses Lombok's {@link Getter} annotation to automatically generate getter methods for the real name.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see Getter
 * @since 1.0.0
 */
@Getter
public enum TrainFieldName {
    NUMBER("number"),
    ARRIVAL_STATION("arrivalStation"),
    DEPARTURE_STATION("departureStation");

    private final String realName;

    TrainFieldName(String realName) {
        this.realName = realName;
    }

    public static TrainFieldName getByRealName(String realName) {
        return Arrays.stream(values())
                .filter(mv -> mv.getRealName().equalsIgnoreCase(realName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No movement type found for display name: " + realName));
    }
}
