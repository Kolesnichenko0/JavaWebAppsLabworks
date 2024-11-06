package csit.semit.kde.javaspringwebappskdelab3.entity.trainticket;

import csit.semit.kde.javaspringwebappskdelab3.util.result.entity.FieldValidationException;
import csit.semit.kde.javaspringwebappskdelab3.util.fieldname.trainticket.TicketFieldName;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.Check;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Abstract base class for ticket entities.
 * <p>
 * This class is mapped as a superclass in JPA and provides common fields and methods for ticket entities.
 * It includes basic ticket information such as passenger surname, passport number, carriage number,
 * seat number, and departure date.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @see TicketFieldName
 * @see FieldValidationException
 * @since 1.0.0
 */
@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class Ticket implements Serializable {

    @Serial
    private static final long serialVersionUID = 987654321123456789L;

    public static final String PASSENGER_SURNAME_REGEX = "^([А-ЗЙ-ЩЮЯЇІЄҐ][а-щьюяїієґ[\\']]+(\\-[А-ЗЙ-ЩЮЯЇІЄҐ][а-щьюяїієґ[\\']]+)?){1,50}$";
    public static final String PASSPORT_NUMBER_REGEX = "^([А-ЗЙ-ЩЮЯЇІЄҐ]{2}[0-9]{6}|[0-9]{9})$";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NotNull
    @Pattern(regexp = PASSENGER_SURNAME_REGEX, message = "Passenger surname must be in Ukrainian")
    @Check(constraints = "REGEXP_LIKE(passenger_surname, '" + PASSENGER_SURNAME_REGEX + "', 'c')")
    @Column(name = "passenger_surname", nullable = false, length = 50)
    protected String passengerSurname;


    @NotNull
    @Pattern(regexp = PASSPORT_NUMBER_REGEX, message = "Passport number must be either 9 digits (ID-card) or 2 Ukrainian letters followed by 6 digits")
    @Check(constraints = "REGEXP_LIKE(passenger_passport_number, '" + PASSPORT_NUMBER_REGEX + "', 'c')")
    @Column(name = "passenger_passport_number", nullable = false, length = 9)
    protected String passportNumber;

    @NotNull
    @Min(1)
//    @Check(constraints = "seat_number>=1")
    @Column(name = "seat_number", nullable = false)
    protected Integer seatNumber;

    @NotNull
    @Column(name = "departure_date", nullable = false)
    protected LocalDate departureDate;

    protected Ticket(
            @NonNull String passengerSurname,
            @NonNull String passportNumber,
            @NonNull Integer seatNumber,
            @NonNull LocalDate departureDate
    ) {
        setPassengerSurname(passengerSurname);
        setPassportNumber(passportNumber);
        setSeatNumber(seatNumber);
        this.departureDate = validateDepartureDate(departureDate);
    }

    public static String validatePassengerSurname(@NonNull String passengerSurname) {
        if (passengerSurname.isEmpty()) {
            throw new FieldValidationException("Passenger surname cannot be empty",
                    TicketFieldName.PASSENGER_SURNAME.getRealName());
        }

        passengerSurname = passengerSurname.trim();

        String[] parts = passengerSurname.split("-");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].substring(0, 1).toUpperCase() + parts[i].substring(1).toLowerCase();
        }
        passengerSurname = String.join("-", parts);

        if (!passengerSurname.matches(PASSENGER_SURNAME_REGEX)) {
            throw new FieldValidationException(
                    "Passenger surname must be in Ukrainian and may include a hyphen for double surnames",
                    TicketFieldName.PASSENGER_SURNAME.getRealName()
            );
        }

        return passengerSurname;
    }

    public static String validatePassportNumber(@NonNull String passportNumber) {
        if (passportNumber.isEmpty()) {
            throw new FieldValidationException("Passport number cannot be empty",
                    TicketFieldName.PASSPORT_NUMBER.getRealName());
        }

        passportNumber = passportNumber.trim().toUpperCase();

        if (!passportNumber.matches(PASSPORT_NUMBER_REGEX)) {
            throw new FieldValidationException(
                    "Passport number must be either 9 digits (ID-card) or 2 Ukrainian letters followed by 6 digits",
                    TicketFieldName.PASSPORT_NUMBER.getRealName()
            );
        }

        return passportNumber;
    }

    public static Integer validateSeatNumber(@NonNull Integer seatNumber) {
        if (seatNumber < 1) {
            throw new FieldValidationException(
                    "Seat number must be greater than 0",
                    TicketFieldName.SEAT_NUMBER.getRealName()
            );
        }
        return seatNumber;
    }

    public static LocalDate validateDepartureDate(@NonNull LocalDate departureDate) {
        LocalDate currentDate = LocalDate.now();
        if (departureDate.isBefore(currentDate)) {
            throw new FieldValidationException(
                    "Departure date cannot be in the past",
                    TicketFieldName.DEPARTURE_DATE.getRealName()
            );
        }
        return departureDate;
    }

    public void setPassengerSurname(@NonNull String passengerSurname) {
        this.passengerSurname = validatePassengerSurname(passengerSurname);
    }

    public void setPassportNumber(@NonNull String passportNumber) {
        this.passportNumber = validatePassportNumber(passportNumber);
    }

    public void setSeatNumber(@NonNull Integer seatNumber) {
        this.seatNumber = validateSeatNumber(seatNumber);
    }

    public void setDepartureDate(@NonNull LocalDate departureDate) {
        this.departureDate = validateDepartureDate(departureDate);
    }
}
