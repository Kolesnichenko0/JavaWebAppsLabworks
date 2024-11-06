package csit.semit.kde.javaspringwebappskdelab3.entity.train;

import csit.semit.kde.javaspringwebappskdelab3.entity.trainticket.TrainTicket;
import csit.semit.kde.javaspringwebappskdelab3.enums.train.MovementType;
import csit.semit.kde.javaspringwebappskdelab3.util.result.entity.FieldValidationException;
import csit.semit.kde.javaspringwebappskdelab3.util.fieldname.train.TrainFieldName;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.Check;

import java.io.Serial;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

/**
 * Entity class representing a Train.
 * <p>
 * This class is mapped to the "trains" table in the database and includes various fields such as number, departure station,
 * arrival station, movement type, departure time, and duration. It also includes validation constraints for the fields.
 * </p>
 * <p>
 * The `Train` class includes:
 * <ul>
 *   <li>Field validation using regular expressions and custom validation methods</li>
 *   <li>Annotations for JPA entity mapping and Lombok for boilerplate code generation</li>
 *   <li>Static methods for validating train number and station names</li>
 * </ul>
 * </p>
 * <p>
 * This class extends the `Transport` class and inherits its fields and methods.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @see Transport
 * @see TrainFieldName
 * @see MovementType
 * @see FieldValidationException
 * @since 1.0.0
 */
@Entity
@Table(name = "trains", indexes = {
        @Index(name = "idx_train_departure_station", columnList = "departure_station"),
        @Index(name = "idx_train_arrival_station", columnList = "arrival_station"),
        @Index(name = "idx_train_departure_time", columnList = "departure_time")
})
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Train extends Transport {

    @Serial
    private static final long serialVersionUID = 176873029745254541L;

    public static final String NUMBER_REGEX = "^(" +
            "(?:00[1-9]|0[1-9][0-9]|1[0-4][0-9]|150|" +           // 001-150
            "1(?:5[1-9]|[6-9][0-9])|2[0-8][0-9]|29[0-8]|" +       // 151-298
            "30[1-9]|3[1-9][0-9]|4[0-4][0-9]|450|" +              // 301-450
            "4(?:5[1-9]|[6-9][0-9])|5[0-8][0-9]|59[0-8]|" +       // 451-598
            "60[1-9]|6[1-9][0-9]|" +                              // 601-698
            "70[1-9]|7[1-4][0-9]|750|" +                          // 701-750
            "75[1-9]|7[6-8][0-8])" +                              // 751-788
            "(?:ІС[\\+]?|РЕ|Р|НЕ|НШ|НП)" +
            ")$";

    public static final String STATION_REGEX = "^(" +
            "([А-ЗЙ-ЩЮЯЇІЄ][А-ЩЮЯЇІЄҐ]+|[А-ЗЙ-ЩЮЯЇІЄ][а-щьюяїієґ[\\']]+[\\.]?)" +
            "(?:[ -]" +
            "(?:[А-ЗЙ-ЩЮЯЇІЄ][А-ЩЮЯЇІЄҐ]+[0-9]*|" +
            "[А-ЗЙ-ЩЮЯЇІЄ]?[а-щьюяїієґ[\\']]+[.|0-9]*|[0-9]+)|" +
            "[\\(][А-ЗЙ-ЩЮЯЇІЄ]?[а-щьюяїієґ[\\']]+[\\.]?[\\)])*" +
            "){1,100}$";

    @NotNull
    @Pattern(regexp = NUMBER_REGEX, message = "Number must start with three digits, followed by one uppercase Ukrainian letter.")
    @Check(constraints = "REGEXP_LIKE(number, '" + NUMBER_REGEX + "', 'c')")
    @Column(name = "number", nullable = false, unique = true, length = 6)
    private String number;

    @NotNull
    @Pattern(regexp = STATION_REGEX, message = "Station name must consist of Ukrainian letters and can include spaces and apostrophes, with a maximum length of 100 characters.")
    @Check(constraints = "REGEXP_LIKE(departure_station, '" + STATION_REGEX + "', 'c')")
    @Column(name = "departure_station", nullable = false, length = 100)
    private String departureStation;

    @NotNull
    @Pattern(regexp = STATION_REGEX, message = "Station name must consist of Ukrainian letters and can include spaces and apostrophes, with a maximum length of 100 characters.")
    @Check(constraints = "REGEXP_LIKE(arrival_station, '" + STATION_REGEX + "', 'c')")
    @Column(name = "arrival_station", nullable = false, length = 100)
    private String arrivalStation;

    @OrderBy("departureDate ASC, carriageNumber ASC, seatNumber ASC")
    @OneToMany(mappedBy = "train", fetch = FetchType.LAZY) // cascade = CascadeType.REMOVE
    private List<TrainTicket> trainTickets;

    public Train(@NonNull String number, @NonNull String arrivalStation, @NonNull String departureStation, @NonNull MovementType movementType, @NonNull LocalTime departureTime, @NonNull Duration duration) {
        super(movementType, departureTime, duration);
        setNumber(number);
        setDepartureStation(departureStation);
        setArrivalStation(arrivalStation);
    }

    public static String validateNumber(@NonNull String number) {
        if (number.isEmpty()) {
            throw new FieldValidationException("The number cannot be empty", TrainFieldName.NUMBER.getRealName());
        }

        number = number.trim().toUpperCase();

        String numericPart = number.replaceAll("[^0-9]", "");

        while (numericPart.length() < 3) {
            number = "0" + number;
            numericPart = "0" + numericPart;
        }

        if (!number.matches(NUMBER_REGEX)) {
            throw new FieldValidationException("The number should consist of 1 to 3 digits and {ІС+, ІС, РЕ, P, НЕ, НШ, НП}", TrainFieldName.NUMBER.getRealName());
        }

        return number;
    }

    public static String validateStationName(@NonNull String station, @NonNull TrainFieldName stationType) {
        if (station.isEmpty()) {
            throw new FieldValidationException("Station name cannot be empty", stationType.getRealName());
        }

        station = station.trim();

        if (Character.isLowerCase(station.charAt(0))) {
            station = Character.toUpperCase(station.charAt(0)) + station.substring(1);
        }

        if (!station.matches(STATION_REGEX)) {
            throw new FieldValidationException("Station name must start with a Ukrainian capital letter and can include spaces, hyphens, numbers, and parentheses.", stationType.getRealName());
        }

        return station;
    }

    public void setNumber(@NonNull String number) {
        this.number = validateNumber(number);
    }

    public void setDepartureStation(@NonNull String departureStation) {
        this.departureStation = validateStationName(departureStation, TrainFieldName.DEPARTURE_STATION);
    }

    public void setArrivalStation(@NonNull String arrivalStation) {
        this.arrivalStation = validateStationName(arrivalStation, TrainFieldName.ARRIVAL_STATION);
    }
}

