package csit.semit.kde.javahibernatewebappskdelab2.dto;

import csit.semit.kde.javahibernatewebappskdelab2.enums.MovementType;

import java.time.Duration;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainDTO {
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
