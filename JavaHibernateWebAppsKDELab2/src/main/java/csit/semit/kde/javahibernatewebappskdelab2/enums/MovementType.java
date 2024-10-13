package csit.semit.kde.javahibernatewebappskdelab2.enums;

import lombok.Getter;

import java.util.Arrays;

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
