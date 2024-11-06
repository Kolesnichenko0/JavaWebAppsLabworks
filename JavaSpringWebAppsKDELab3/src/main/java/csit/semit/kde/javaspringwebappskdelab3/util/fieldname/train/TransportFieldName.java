package csit.semit.kde.javaspringwebappskdelab3.util.fieldname.train;

import lombok.Getter;

import java.util.Arrays;

/**
 * Enum representing the various field names used in the Transport class.
 * <p>
 * This enum is used to define the different field names that can be used in the Transport class. Each field name has a
 * corresponding real name that is used for display purposes. The enum provides methods to get the real name of a field
 * and to retrieve a field name by its real name.
 * </p>
 * <p>
 * The `TransportFieldName` enum includes:
 * <ul>
 *   <li>{@code ID} - Represents the ID field.</li>
 *   <li>{@code MOVEMENT_TYPE} - Represents the movement type field.</li>
 *   <li>{@code DEPARTURE_TIME} - Represents the departure time field.</li>
 *   <li>{@code DURATION} - Represents the duration field.</li>
 * </ul>
 * </p>
 * <p>
 * This class uses Lombok's {@link Getter} annotation to automatically generate getter methods for the real name.
 * </p>
 */
@Getter
public enum TransportFieldName {
    ID("id"),
    MOVEMENT_TYPE("movementType"),
    DEPARTURE_TIME("departureTime"),
    DURATION("duration");

    private final String realName;

    TransportFieldName(String realName) {
        this.realName = realName;
    }

    public static TransportFieldName getByRealName(String realName) {
        return Arrays.stream(values())
                .filter(mv -> mv.getRealName().equalsIgnoreCase(realName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No field name found for display name: " + realName));
    }
}