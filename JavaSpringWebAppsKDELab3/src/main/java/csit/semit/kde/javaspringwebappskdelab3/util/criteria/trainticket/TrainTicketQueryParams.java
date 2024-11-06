package csit.semit.kde.javaspringwebappskdelab3.util.criteria.trainticket;

import csit.semit.kde.javaspringwebappskdelab3.entity.trainticket.Ticket;
import csit.semit.kde.javaspringwebappskdelab3.entity.trainticket.TrainTicket;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Class representing query parameters for searching and filtering train tickets.
 * <p>
 * This class provides various fields to specify search and filter criteria for train tickets.
 * It includes parameters such as passenger surname, passport number, carriage number, departure date, and train ID.
 * </p>
 * <p>
 * The main functionalities provided by this class include:
 * <ul>
 *   <li>Specifying search criteria for train tickets.</li>
 *   <li>Specifying filter criteria for train tickets based on departure dates.</li>
 *   <li>Validating the provided parameters.</li>
 * </ul>
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * TrainTicketQueryParams params = new TrainTicketQueryParams.Builder()
 *     .searchingPassengerSurname("Doe")
 *     .searchingPassportNumber("123456789")
 *     .searchingCarriageNumber(5)
 *     .searchingDepartureDate(LocalDate.of(2023, 10, 1))
 *     .searchingTrainId(1L)
 *     .filteringDepartureDateFrom(LocalDate.of(2023, 9, 1))
 *     .filteringDepartureDateTo(LocalDate.of(2023, 12, 31))
 *     .build();
 * }
 * </pre>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @see Ticket
 * @see TrainTicket
 * @since 1.0.0
 */
@Getter
@ToString
public class TrainTicketQueryParams {
    private String searchingPassengerSurname;
    private String searchingPassportNumber;
    private Integer searchingCarriageNumber;
    private LocalDate searchingDepartureDate;
    private Long searchingTrainId;
    private LocalDate filteringDepartureDateFrom;
    private LocalDate filteringDepartureDateTo;


    private TrainTicketQueryParams(Builder builder) {
        this.searchingPassengerSurname = builder.searchingPassengerSurname;
        this.searchingPassportNumber = builder.searchingPassportNumber;
        this.searchingCarriageNumber = builder.searchingCarriageNumber;
        this.searchingDepartureDate = builder.searchingDepartureDate;
        this.searchingTrainId = builder.searchingTrainId;
        this.filteringDepartureDateFrom = builder.filteringDepartureDateFrom;
        this.filteringDepartureDateTo = builder.filteringDepartureDateTo;
    }

    public boolean isEmpty() {
        return searchingPassengerSurname == null && searchingPassportNumber == null &&
                searchingCarriageNumber == null && searchingDepartureDate == null &&
                filteringDepartureDateFrom == null && filteringDepartureDateTo == null;
    }

    public static class Builder {
        private String searchingPassengerSurname;
        private String searchingPassportNumber;
        private Integer searchingCarriageNumber;
        private LocalDate searchingDepartureDate;
        private Long searchingTrainId;
        private LocalDate filteringDepartureDateFrom;
        private LocalDate filteringDepartureDateTo;

        public Builder searchingPassengerSurname(String searchingPassengerSurname) {
            this.searchingPassengerSurname = searchingPassengerSurname;
            return this;
        }

        public Builder searchingPassportNumber(String searchingPassportNumber) {
            this.searchingPassportNumber = searchingPassportNumber;
            return this;
        }

        public Builder searchingCarriageNumber(Integer searchingCarriageNumber) {
            this.searchingCarriageNumber = searchingCarriageNumber;
            return this;
        }

        public Builder searchingDepartureDate(LocalDate searchingDepartureDate) {
            this.searchingDepartureDate = searchingDepartureDate;
            return this;
        }

        public Builder searchingTrainId(Long searchingTrainId) {
            this.searchingTrainId = searchingTrainId;
            return this;
        }

        public Builder filteringDepartureDateFrom(LocalDate filteringDepartureDateFrom) {
            this.filteringDepartureDateFrom = filteringDepartureDateFrom;
            return this;
        }

        public Builder filteringDepartureDateTo(LocalDate filteringDepartureDateTo) {
            this.filteringDepartureDateTo = filteringDepartureDateTo;
            return this;
        }

        public TrainTicketQueryParams build() {
            validateParams();
            return new TrainTicketQueryParams(this);
        }

        private void validateParams() {
            if (searchingPassengerSurname != null) {
                Ticket.validatePassengerSurname(searchingPassengerSurname);
            }
            if (searchingPassportNumber != null) {
                Ticket.validatePassportNumber(searchingPassportNumber);
            }
            if (searchingCarriageNumber != null) {
                TrainTicket.validateCarriageNumber(searchingCarriageNumber);
            }
        }
    }
}
