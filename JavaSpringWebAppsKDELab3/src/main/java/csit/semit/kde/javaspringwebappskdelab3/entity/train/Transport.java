package csit.semit.kde.javaspringwebappskdelab3.entity.train;

import csit.semit.kde.javaspringwebappskdelab3.enums.train.MovementType;
import csit.semit.kde.javaspringwebappskdelab3.util.converter.train.DurationConverter;
import csit.semit.kde.javaspringwebappskdelab3.util.result.entity.FieldValidationException;
import csit.semit.kde.javaspringwebappskdelab3.util.fieldname.train.TransportFieldName;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Check;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;

/**
 * Abstract base class for transport entities.
 * <p>
 * This class is mapped as a superclass in JPA and provides common fields and methods for transport entities.
 * It includes fields for ID, movement type, departure time, duration, and deletion status.
 * </p>
 * <p>
 * The `Transport` class includes:
 * <ul>
 *   <li>Field validation for movement type, departure time, duration, and deletion status</li>
 *   <li>Annotations for JPA entity mapping and Lombok for boilerplate code generation</li>
 *   <li>Static methods for validating various fields</li>
 * </ul>
 * </p>
 */
@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class Transport implements Serializable {

    @Serial
    private static final long serialVersionUID = 12345678987654321L;

    private static final long MIN_DURATION_MINUTES = 0;
    private static final long MAX_DURATION_MINUTES = 24 * 60;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false)
    protected MovementType movementType;

    @NotNull
    @Column(name = "departure_time", nullable = false)
    @Check(constraints = "departure_time BETWEEN '00:00:00' AND '23:59:59'")
    protected LocalTime departureTime;

    @NotNull
    @Convert(converter = DurationConverter.class)
    @Column(name = "duration_in_seconds", nullable = false)
    @Check(constraints = "duration_in_seconds BETWEEN " + (MIN_DURATION_MINUTES * 60) + " AND " + (MAX_DURATION_MINUTES * 60))
    protected Duration duration;

    public Transport(@NonNull MovementType movementType, @NonNull LocalTime departureTime, @NonNull Duration duration) {
        setMovementType(movementType);
        setDepartureTime(departureTime);
        setDuration(duration);
    }

    public static MovementType validateMovementType(@NonNull MovementType movementType) {
        return movementType;
    }

    public static LocalTime validateDepartureTime(@NonNull LocalTime departureTime) {
        return departureTime.withNano(0);
    }

    public static Duration validateDuration(@NonNull Duration duration) {
        if (duration.compareTo(Duration.ofMinutes(MIN_DURATION_MINUTES)) < 0 || duration.compareTo(Duration.ofMinutes(MAX_DURATION_MINUTES)) > 0) {
            throw new FieldValidationException("Duration must be between " + MIN_DURATION_MINUTES + " minutes and " + (MAX_DURATION_MINUTES / 60) + " hours", TransportFieldName.DURATION.getRealName());
        }
        return duration.withNanos(0);
    }

    public static Long validateId(@NonNull Long id) {
        if (id <= 0) {
            throw new FieldValidationException("ID cannot be null or less than or equal to zero", TransportFieldName.ID.getRealName());
        }
        return id;
    }

    public void setMovementType(@NonNull MovementType movementType) {
        this.movementType = validateMovementType(movementType);
    }

    public void setDepartureTime(@NonNull LocalTime departureTime) {
        this.departureTime = validateDepartureTime(departureTime);
    }

    public void setDuration(@NonNull Duration duration) {
        this.duration = validateDuration(duration);
    }
}