package csit.semit.kde.javahibernatewebappskdelab2.util.result;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum FieldName {
    ID("id"),
    NUMBER("number"),
    ARRIVAL_STATION("arrivalStation"),
    DEPARTURE_STATION("departureStation"),
    DEPARTURE_TIME("departureTime"),
    MOVEMENT_TYPE("movementType"),
    DURATION("duration");

    private final String realName;

    FieldName(String realName) {
        this.realName = realName;
    }

    public static FieldName getByRealName(String realName) {
        return Arrays.stream(values())
                .filter(mv -> mv.getRealName().equalsIgnoreCase(realName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No movement type found for display name: " + realName));
    }
}
