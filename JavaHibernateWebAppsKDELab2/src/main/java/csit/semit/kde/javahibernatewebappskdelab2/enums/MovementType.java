package csit.semit.kde.javahibernatewebappskdelab2.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * Enumeration representing different types of train movements.
 * <p>
 * The `MovementType` enum includes:
 * <ul>
 *   <li>DAILY: Represents daily movements ("щоденні")</li>
 *   <li>EVEN_DAYS: Represents movements on even days ("парні")</li>
 *   <li>ODD_DAYS: Represents movements on odd days ("непарні")</li>
 * </ul>
 * </p>
 * <p>
 * Each enum constant has a display name in Ukrainian, which can be retrieved using the `getDisplayName` method.
 * </p>
 * <p>
 * The `MovementType` enum provides utility methods to:
 * <ul>
 *   <li>Get all movement types as an array of display names</li>
 *   <li>Get a `MovementType` by its display name</li>
 *   <li>Get a `MovementType` by its index</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @see java.util.Arrays
 * @since 1.0.0
 */
@Getter
public enum MovementType {
    DAILY("щоденні"),
    EVEN_DAYS("парні"),
    ODD_DAYS("непарні");

    private final String displayName;

    MovementType(String displayName) {
        this.displayName = displayName;
    }

    public static String[] getMovementTypes() {
        return Arrays.stream(values())
                .map(MovementType::getDisplayName)
                .toArray(String[]::new);
    }

    public static MovementType getByDisplayName(String displayName) {
        return Arrays.stream(values())
                .filter(mv -> mv.getDisplayName().equalsIgnoreCase(displayName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No movement type found for display name: " + displayName));
    }

    public static MovementType getByIndex(int index) {
        if (index < 0 || index >= values().length) {
            throw new IllegalArgumentException("Invalid MovementType ID: " + index);
        }
        return values()[index];
    }

    @Override
    public String toString() {
        return displayName;
    }
}
