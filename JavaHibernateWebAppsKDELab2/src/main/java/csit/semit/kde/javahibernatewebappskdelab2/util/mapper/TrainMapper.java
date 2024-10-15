package csit.semit.kde.javahibernatewebappskdelab2.util.mapper;

import csit.semit.kde.javahibernatewebappskdelab2.dto.TrainDTO;
import csit.semit.kde.javahibernatewebappskdelab2.entity.Train;
import lombok.NonNull;

/**
 * Utility class for mapping between {@link Train} entities and {@link TrainDTO} data transfer objects.
 * <p>
 * This class provides static methods to convert {@link TrainDTO} objects to {@link Train} entities and vice versa.
 * It ensures that the data is correctly mapped between the two representations, facilitating the transfer of data
 * between different layers of the application.
 * </p>
 * <p>
 * The `TrainMapper` class includes:
 * <ul>
 *   <li>Conversion of {@link TrainDTO} to {@link Train} in the {@code toEntity} method</li>
 *   <li>Conversion of {@link Train} to {@link TrainDTO} in the {@code toDTO} method</li>
 * </ul>
 * </p>
 * <p>
 * The class uses Lombok's {@link NonNull} annotation to ensure that the input parameters are not null.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see Train
 * @see TrainDTO
 * @since 1.0.0
 */
public class TrainMapper {
    public static Train toEntity(@NonNull TrainDTO trainDTO) {
        Train train = new Train(
                trainDTO.getNumber(),
                trainDTO.getArrivalStation(),
                trainDTO.getDepartureStation(),
                trainDTO.getMovementType(),
                trainDTO.getDepartureTime(),
                trainDTO.getDuration()
        );

        if (trainDTO.getId() != null) {
            train.setId(trainDTO.getId());
        }

        return train;
    }

    public static TrainDTO toDTO(Train train) {
        if (train == null) {
            return null;
        }
        return new TrainDTO(
                train.getId(),
                train.getNumber(),
                train.getDepartureStation(),
                train.getArrivalStation(),
                train.getMovementType(),
                train.getDepartureTime(),
                train.getDuration()
        );
    }
}
