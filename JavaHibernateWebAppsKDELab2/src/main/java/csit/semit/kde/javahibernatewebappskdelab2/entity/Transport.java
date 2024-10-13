package csit.semit.kde.javahibernatewebappskdelab2.entity;

import csit.semit.kde.javahibernatewebappskdelab2.util.result.FieldName;
import csit.semit.kde.javahibernatewebappskdelab2.enums.MovementType;
import csit.semit.kde.javahibernatewebappskdelab2.util.converter.DurationConverter;
import csit.semit.kde.javahibernatewebappskdelab2.util.result.FieldValidationException;
import jakarta.persistence.*;
import lombok.*;

import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class Transport implements Serializable, SoftDeletable {

    @Serial
    private static final long serialVersionUID = 12345678987654321L;

    private static final long MIN_DURATION_MINUTES = 0;
    private static final long MAX_DURATION_MINUTES = 24 * 60;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
//    @Convert(converter = MovementTypeConverter.class)
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

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    @ColumnDefault("0")
    protected Boolean isDeleted;

    public Transport(@NonNull MovementType movementType, @NonNull LocalTime departureTime, @NonNull Duration duration) {
        this.movementType = validateMovementType(movementType);
        this.departureTime = validateDepartureTime(departureTime);
        this.duration = validateDuration(duration);
        this.isDeleted = false;
    }

    public static MovementType validateMovementType(@NonNull MovementType movementType) {
        return movementType;
    }

    public static LocalTime validateDepartureTime(@NonNull LocalTime departureTime) {
        return departureTime.withNano(0);
    }

    public static Duration validateDuration(@NonNull Duration duration) {
        if (duration.compareTo(Duration.ofMinutes(MIN_DURATION_MINUTES)) < 0 || duration.compareTo(Duration.ofMinutes(MAX_DURATION_MINUTES)) > 0) {
            throw new FieldValidationException("Duration must be between " + MIN_DURATION_MINUTES + " minutes and " + (MAX_DURATION_MINUTES / 60) + " hours", FieldName.DURATION);
        }
        return duration.withNanos(0);
    }

    public static Boolean validateIsDeleted(@NonNull Boolean isDeleted) {
        return isDeleted;
    }

    public static Long validateId(@NonNull Long id) {
        if (id <= 0) {
            throw new FieldValidationException("ID cannot be null or less than or equal to zero", FieldName.ID);
        }
        return id;
    }

    public void setDuration(@NonNull Duration duration) {
        this.duration = validateDuration(duration);
    }

    public void setIsDeleted(@NonNull Boolean isDeleted) {
        this.isDeleted = validateIsDeleted(isDeleted);
    }
}
