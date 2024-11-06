package csit.semit.kde.javaspringwebappskdelab3.util.fieldname.trainticket;

/**
 * Enum representing the various field names used in the Ticket class.
 * <p>
 * This enum is used to define the different field names that can be used in the Ticket class. Each field name has a
 * corresponding real name that is used for display purposes. The enum provides methods to get the real name of a field.
 * </p>
 * <p>
 * The `TicketFieldName` enum includes:
 * <ul>
 *   <li>{@code ID} - Represents the ID field.</li>
 *   <li>{@code PASSENGER_SURNAME} - Represents the passenger surname field.</li>
 *   <li>{@code PASSPORT_NUMBER} - Represents the passport number field.</li>
 *   <li>{@code SEAT_NUMBER} - Represents the seat number field.</li>
 *   <li>{@code DEPARTURE_DATE} - Represents the departure date field.</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @since 1.0.0
 */
public enum TicketFieldName {
    ID("id"),
    PASSENGER_SURNAME("passengerSurname"),
    PASSPORT_NUMBER("passportNumber"),
    SEAT_NUMBER("seatNumber"),
    DEPARTURE_DATE("departureDate");

    private final String realName;

    TicketFieldName(String realName) {
        this.realName = realName;
    }

    public String getRealName() {
        return realName;
    }
}