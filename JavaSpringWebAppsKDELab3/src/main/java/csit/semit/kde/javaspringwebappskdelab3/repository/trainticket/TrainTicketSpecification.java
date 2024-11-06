package csit.semit.kde.javaspringwebappskdelab3.repository.trainticket;

import csit.semit.kde.javaspringwebappskdelab3.entity.trainticket.TrainTicket;
import csit.semit.kde.javaspringwebappskdelab3.util.criteria.trainticket.TrainTicketQueryParams;
import csit.semit.kde.javaspringwebappskdelab3.util.fieldname.trainticket.TicketFieldName;
import csit.semit.kde.javaspringwebappskdelab3.util.fieldname.trainticket.TrainTicketFieldName;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Specification class for querying {@link TrainTicket} entities based on various parameters.
 * <p>
 * This class provides a method to create a {@link Specification} for {@link TrainTicket} entities
 * using the provided {@link TrainTicketQueryParams}. It allows for dynamic query construction
 * based on the specified search and filter criteria.
 * </p>
 * <p>
 * The main functionalities provided by this class include:
 * <ul>
 *   <li>Searching train tickets by train ID, departure date, carriage number, passenger surname, and passport number.</li>
 *   <li>Filtering train tickets by departure date range.</li>
 * </ul>
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * TrainTicketQueryParams params = new TrainTicketQueryParams();
 * params.setSearchingTrainId(1L);
 * Specification<TrainTicket> spec = TrainTicketSpecification.withQueryParams(params);
 * List<TrainTicket> tickets = trainTicketRepository.findAll(spec);
 * }
 * </pre>
 * </p>
 * <p>
 * Dependencies:
 * <ul>
 *   <li>{@link TrainTicket}</li>
 *   <li>{@link TrainTicketQueryParams}</li>
 *   <li>{@link TicketFieldName}</li>
 *   <li>{@link TrainTicketFieldName}</li>
 *   <li>{@link Specification}</li>
 *   <li>{@link CriteriaBuilder}</li>
 *   <li>{@link CriteriaQuery}</li>
 *   <li>{@link Predicate}</li>
 *   <li>{@link Root}</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @see TrainTicket
 * @see TrainTicketQueryParams
 * @see TicketFieldName
 * @see TrainTicketFieldName
 * @see Specification
 * @see CriteriaBuilder
 * @see CriteriaQuery
 * @see Predicate
 * @see Root
 * @since 1.0.0
 */
public class TrainTicketSpecification {
    public static Specification<TrainTicket> withQueryParams(TrainTicketQueryParams params) {
        return (Root<TrainTicket> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.getSearchingTrainId() != null) {
                predicates.add(cb.equal(root.join(TrainTicketFieldName.TRAIN.getRealName()).get("id"), params.getSearchingTrainId()));
            }

            if (params.getSearchingDepartureDate() != null && params.getSearchingCarriageNumber() != null) {
                predicates.add(cb.equal(root.get(TicketFieldName.DEPARTURE_DATE.getRealName()), params.getSearchingDepartureDate()));
                predicates.add(cb.equal(root.get(TrainTicketFieldName.CARRIAGE_NUMBER.getRealName()), params.getSearchingCarriageNumber()));
            } else {
                if (params.getSearchingPassengerSurname() != null && !params.getSearchingPassengerSurname().isEmpty()) {
                    predicates.add(cb.like(cb.lower(root.get(TicketFieldName.PASSENGER_SURNAME.getRealName())),
                            params.getSearchingPassengerSurname().trim().toLowerCase() + "%"));
                }
                if (params.getSearchingPassportNumber() != null && !params.getSearchingPassportNumber().isEmpty()) {
                    predicates.add(cb.equal(cb.lower(root.get(TicketFieldName.PASSPORT_NUMBER.getRealName())),
                            params.getSearchingPassportNumber().trim().toLowerCase()));
                }
                if (params.getSearchingDepartureDate() != null) {
                    predicates.add(cb.equal(root.get(TicketFieldName.DEPARTURE_DATE.getRealName()), params.getSearchingDepartureDate()));
                }

                if (params.getFilteringDepartureDateFrom() != null && params.getFilteringDepartureDateTo() != null) {
                    predicates.add(cb.between(root.get(TicketFieldName.DEPARTURE_DATE.getRealName()),
                            params.getFilteringDepartureDateFrom(), params.getFilteringDepartureDateTo()));
                } else {
                    if (params.getFilteringDepartureDateFrom() != null) {
                        predicates.add(cb.greaterThanOrEqualTo(root.get(TicketFieldName.DEPARTURE_DATE.getRealName()),
                                params.getFilteringDepartureDateFrom()));
                    }
                    if (params.getFilteringDepartureDateTo() != null) {
                        predicates.add(cb.lessThanOrEqualTo(root.get(TicketFieldName.DEPARTURE_DATE.getRealName()),
                                params.getFilteringDepartureDateTo()));
                    }
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}