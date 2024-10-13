package csit.semit.kde.javahibernatewebappskdelab2.util.mapper;

import csit.semit.kde.javahibernatewebappskdelab2.dto.TrainDTO;
import csit.semit.kde.javahibernatewebappskdelab2.entity.Train;
import lombok.NonNull;

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
