package csit.semit.kde.javaspringwebappskdelab3.util.mapper.trainticket;

import csit.semit.kde.javaspringwebappskdelab3.dto.trainticket.TrainTicketDTO;
import csit.semit.kde.javaspringwebappskdelab3.entity.trainticket.TrainTicket;
import csit.semit.kde.javaspringwebappskdelab3.entity.train.Train;
import lombok.NonNull;

/**
 * Utility class for mapping between {@link TrainTicket} entities and {@link TrainTicketDTO} data transfer objects.
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @since 1.0.0
 */
public class TrainTicketMapper {
    public static TrainTicket toEntity(@NonNull TrainTicketDTO dto, @NonNull Train train) {
        TrainTicket ticket = new TrainTicket(
                train,
                dto.getPassengerSurname(),
                dto.getPassportNumber(),
                dto.getCarriageNumber(),
                dto.getSeatNumber(),
                dto.getDepartureDate()
        );

        if (dto.getId() != null) {
            ticket.setId(dto.getId());
        }

        return ticket;
    }

    public static TrainTicketDTO toDTO(@NonNull TrainTicket ticket) {
        return new TrainTicketDTO(
                ticket.getId(),
                ticket.getTrain().getNumber(),
                ticket.getTrain().getDepartureStation(),
                ticket.getTrain().getArrivalStation(),
                ticket.getTrain().getId(),
                ticket.getPassengerSurname(),
                ticket.getPassportNumber(),
                ticket.getCarriageNumber(),
                ticket.getSeatNumber(),
                ticket.getDepartureDate()
        );
    }
}