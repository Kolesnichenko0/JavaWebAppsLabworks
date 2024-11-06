package csit.semit.kde.javaspringwebappskdelab3.dto.train;

import csit.semit.kde.javaspringwebappskdelab3.dto.Identifiable;
import csit.semit.kde.javaspringwebappskdelab3.enums.train.MovementType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Data Transfer Object (DTO) for `Train` entities.
 * <p>
 * This class is used to transfer data between different layers of the application.
 * It encapsulates the details of a train, including its ID, number, departure and arrival stations, movement type,
 * departure time, and duration.
 * </p>
 * <p>
 * The `TrainDTO` class includes:
 * <ul>
 *   <li>ID of the train</li>
 *   <li>Train number</li>
 *   <li>Departure station</li>
 *   <li>Arrival station</li>
 *   <li>Movement type (e.g., daily, even days, odd days)</li>
 *   <li>Departure time</li>
 *   <li>Duration of the journey</li>
 * </ul>
 * </p>
 * <p>
 * This class uses Lombok annotations to generate boilerplate code such as getters, setters, constructors, and `toString` method.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see MovementType
 * @see Data
 * @see NoArgsConstructor
 * @see AllArgsConstructor
 * @see NonNull
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainDTO implements Identifiable {
    private Long id;
    @NonNull
    private String number;
    @NonNull
    private String departureStation;
    @NonNull
    private String arrivalStation;
    @NonNull
    private MovementType movementType;
    @NonNull
    private LocalTime departureTime;
    @NonNull
    private Duration duration;
}
