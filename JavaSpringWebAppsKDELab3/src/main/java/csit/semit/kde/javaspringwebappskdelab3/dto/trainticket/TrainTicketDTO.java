package csit.semit.kde.javaspringwebappskdelab3.dto.trainticket;

import csit.semit.kde.javaspringwebappskdelab3.dto.Identifiable;
import csit.semit.kde.javaspringwebappskdelab3.entity.trainticket.TrainTicket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) for {@link TrainTicket} entities.
 * <p>
 * This class is used to transfer train ticket data between different layers of the application.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainTicketDTO implements Identifiable {
    private Long id;
    private String trainNumber;
    private String trainDepartureStation;
    private String trainArrivalStation;
    @NonNull
    private Long trainId;
    @NonNull
    private String passengerSurname;
    @NonNull
    private String passportNumber;
    @NonNull
    private Integer carriageNumber;
    @NonNull
    private Integer seatNumber;
    @NonNull
    private LocalDate departureDate;
}