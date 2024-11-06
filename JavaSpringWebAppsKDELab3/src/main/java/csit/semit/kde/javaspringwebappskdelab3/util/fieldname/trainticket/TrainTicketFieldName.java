package csit.semit.kde.javaspringwebappskdelab3.util.fieldname.trainticket;

/**
 * Enum representing the various field names used in the TrainTicket class.
 * <p>
 * This enum is used to define the different field names that can be used in the TrainTicket class. Each field name has a
 * corresponding real name that is used for display purposes. The enum provides methods to get the real name of a field.
 * </p>
 * <p>
 * The `TrainTicketFieldName` enum includes:
 * <ul>
 *   <li>{@code TRAIN} - Represents the train field.</li>
 *   <li>{@code CARRIAGE_NUMBER} - Represents the carriage number field.</li>
 * </ul>
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * TrainTicketFieldName fieldName = TrainTicketFieldName.TRAIN;
 * String realName = fieldName.getRealName();
 * }
 * </pre>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @since 1.0.0
 */
public enum TrainTicketFieldName {
    TRAIN("train"),
    CARRIAGE_NUMBER("carriageNumber");

    private final String realName;

    TrainTicketFieldName(String realName) {
        this.realName = realName;
    }

    public String getRealName() {
        return realName;
    }
}