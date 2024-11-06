package csit.semit.kde.javaspringwebappskdelab3.entity.trainticket;

import csit.semit.kde.javaspringwebappskdelab3.entity.train.Train;
import csit.semit.kde.javaspringwebappskdelab3.enums.train.MovementType;
import csit.semit.kde.javaspringwebappskdelab3.util.result.entity.FieldValidationException;
import csit.semit.kde.javaspringwebappskdelab3.util.fieldname.trainticket.TicketFieldName;
import csit.semit.kde.javaspringwebappskdelab3.util.fieldname.trainticket.TrainTicketFieldName;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Entity class representing a Train Ticket.
 * <p>
 * This class extends the base {@link Ticket} class and adds train-specific functionality.
 * It is mapped to the "train_tickets" table in the database and includes a reference to the train.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @see Ticket
 * @see Train
 * @since 1.0.0
 */
@Entity
@Table(name = "train_tickets",
        indexes = {
                @Index(name = "idx_train_ticket_passenger_surname", columnList = "passenger_surname"),
                @Index(name = "idx_train_ticket_passenger_passport_number", columnList = "passenger_passport_number"),
                @Index(name = "idx_train_ticket_train_id_carriage_number_departure_date", columnList = "train_id, carriage_number, departure_date"),
                @Index(name = "idx_train_ticket_departure_date", columnList = "departure_date")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_seat_carriage_date_train",
                        columnNames = {"seat_number", "carriage_number", "departure_date", "train_id"}
                )
        })
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TrainTicket extends Ticket {

    @Serial
    private static final long serialVersionUID = 192837465564738291L;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "train_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private Train train;

    @NotNull
    @Min(1)
//    @Check(constraints = "carriage_number>=1")
    @Column(name = "carriage_number", nullable = false)
    protected Integer carriageNumber;

    public TrainTicket(
            @NonNull Train train,
            @NonNull String passengerSurname,
            @NonNull String passportNumber,
            @NonNull Integer carriageNumber,
            @NonNull Integer seatNumber,
            @NonNull LocalDate departureDate
    ) {
        super(passengerSurname, passportNumber, seatNumber, departureDate);
        setTrain(train);
        setDepartureDate(departureDate);
        setCarriageNumber(carriageNumber);
    }

    public static Train validateTrain(@NonNull Train train) {
        if (train.getId() == null || train.getId() <= 0) {
            throw new FieldValidationException("Invalid train reference", TrainTicketFieldName.TRAIN.getRealName());
        }
        return train;
    }

    public static Integer validateCarriageNumber(@NonNull Integer carriageNumber) {
        if (carriageNumber < 1) {
            throw new FieldValidationException(
                    "Carriage number must be greater than 0",
                    TrainTicketFieldName.CARRIAGE_NUMBER.getRealName()
            );
        }
        return carriageNumber;
    }

    public static LocalDate validateTrainDepartureDate(@NonNull LocalDate departureDate, @NonNull Train train) {
        departureDate = validateDepartureDate(departureDate);

        LocalTime trainDepartureTime = train.getDepartureTime();
        LocalTime currentTime = LocalTime.now();
        LocalDate currentDate = LocalDate.now();

        // Check if the train has already departed today
        if (departureDate.isEqual(currentDate) && currentTime.isAfter(trainDepartureTime)) {
            throw new FieldValidationException(
                    "Cannot create ticket for a train that has already departed today",
                    TicketFieldName.DEPARTURE_DATE.getRealName()
            );
        }

        // Check MovementType for additional constraints
        MovementType movementType = train.getMovementType();
        switch (movementType) {
            case DAILY:
                break;
            case ODD_DAYS:
                if (departureDate.getDayOfMonth() % 2 == 0) {
                    throw new FieldValidationException(
                            "Cannot create ticket for a train that only runs on odd days",
                            TicketFieldName.DEPARTURE_DATE.getRealName()
                    );
                }
                break;
            case EVEN_DAYS:
                if (departureDate.getDayOfMonth() % 2 != 0) {
                    throw new FieldValidationException(
                            "Cannot create ticket for a train that only runs on even days",
                            TicketFieldName.DEPARTURE_DATE.getRealName()
                    );
                }
                break;
            default:
                throw new FieldValidationException(
                        "Unknown movement type",
                        TicketFieldName.DEPARTURE_DATE.getRealName()
                );
        }

        return departureDate;
    }

    public void setTrain(@NonNull Train train) {
        this.train = validateTrain(train);
    }

    public void setCarriageNumber(@NonNull Integer carriageNumber) {
        this.carriageNumber = validateCarriageNumber(carriageNumber);
    }

    @Override
    public void setDepartureDate(@NonNull LocalDate departureDate) {
        super.setDepartureDate(validateTrainDepartureDate(departureDate, this.train));
    }
}