package csit.semit.kde.javaspringwebappskdelab3.repository.train;

import csit.semit.kde.javaspringwebappskdelab3.entity.train.Train;
import csit.semit.kde.javaspringwebappskdelab3.util.criteria.train.TrainQueryParams;
import csit.semit.kde.javaspringwebappskdelab3.util.fieldname.train.TrainFieldName;
import csit.semit.kde.javaspringwebappskdelab3.util.fieldname.train.TransportFieldName;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Specification class for querying {@link Train} entities based on various parameters.
 * <p>
 * This class provides a method to create a {@link Specification} for {@link Train} entities
 * using the provided {@link TrainQueryParams}. It allows for dynamic query construction
 * based on the specified search and filter criteria.
 * </p>
 * <p>
 * The main functionalities provided by this class include:
 * <ul>
 *   <li>Searching trains by number, departure station, and arrival station.</li>
 *   <li>Filtering trains by movement types, departure time, and duration.</li>
 *   <li>Sorting trains by number, duration, and departure time.</li>
 * </ul>
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * TrainQueryParams params = new TrainQueryParams();
 * params.setSearchingNumber("12345");
 * Specification<Train> spec = TrainSpecification.withQueryParams(params);
 * List<Train> trains = trainRepository.findAll(spec);
 * }
 * </pre>
 * </p>
 * <p>
 * Dependencies:
 * <ul>
 *   <li>{@link Train}</li>
 *   <li>{@link TrainQueryParams}</li>
 *   <li>{@link TrainFieldName}</li>
 *   <li>{@link TransportFieldName}</li>
 *   <li>{@link Specification}</li>
 *   <li>{@link CriteriaBuilder}</li>
 *   <li>{@link CriteriaQuery}</li>
 *   <li>{@link Predicate}</li>
 *   <li>{@link Root}</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @see Train
 * @see TrainQueryParams
 * @see TrainFieldName
 * @see TransportFieldName
 * @see Specification
 * @see CriteriaBuilder
 * @see CriteriaQuery
 * @see Predicate
 * @see Root
 * @since 1.0.0
 */
public class TrainSpecification {
    public static Specification<Train> withQueryParams(TrainQueryParams params) {
        return (Root<Train> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.getSearchingNumber() != null && !params.getSearchingNumber().isEmpty()) {
                predicates.add(cb.like(root.get(TrainFieldName.NUMBER.getRealName()),
                        "%" + params.getSearchingNumber().trim() + "%"));
            } else {
                if (params.getSearchingDepartureStation() != null && !params.getSearchingDepartureStation().isEmpty()) {
                    predicates.add(cb.like(cb.lower(root.get(TrainFieldName.DEPARTURE_STATION.getRealName())),
                            "%" + params.getSearchingDepartureStation().trim().toLowerCase() + "%"));
                }
                if (params.getSearchingArrivalStation() != null && !params.getSearchingArrivalStation().isEmpty()) {
                    predicates.add(cb.like(cb.lower(root.get(TrainFieldName.ARRIVAL_STATION.getRealName())),
                            "%" + params.getSearchingArrivalStation().trim().toLowerCase() + "%"));
                }
            }

            if (params.getFilteringMovementTypes() != null && !params.getFilteringMovementTypes().isEmpty()) {
                predicates.add(root.get(TransportFieldName.MOVEMENT_TYPE.getRealName()).in(params.getFilteringMovementTypes()));
            }

            if (params.getFilteringFrom() != null && params.getFilteringTo() != null) {
                predicates.add(cb.between(root.get(TransportFieldName.DEPARTURE_TIME.getRealName()),
                        params.getFilteringFrom(), params.getFilteringTo()));
            } else {
                if (params.getFilteringFrom() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get(TransportFieldName.DEPARTURE_TIME.getRealName()),
                            params.getFilteringFrom()));
                }
                if (params.getFilteringTo() != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get(TransportFieldName.DEPARTURE_TIME.getRealName()),
                            params.getFilteringTo()));
                }
            }

            if (params.getFilteringMinDuration() != null && params.getFilteringMaxDuration() != null) {
                predicates.add(cb.between(root.get(TransportFieldName.DURATION.getRealName()),
                        params.getFilteringMinDuration(), params.getFilteringMaxDuration()));
            } else {
                if (params.getFilteringMinDuration() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get(TransportFieldName.DURATION.getRealName()),
                            params.getFilteringMinDuration()));
                }
                if (params.getFilteringMaxDuration() != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get(TransportFieldName.DURATION.getRealName()),
                            params.getFilteringMaxDuration()));
                }
            }

            if (params.getSortedByTrainNumberAsc() != null) {
                query.orderBy(params.getSortedByTrainNumberAsc() ?
                        cb.asc(root.get(TrainFieldName.NUMBER.getRealName())) : cb.desc(root.get(TrainFieldName.NUMBER.getRealName())));
            } else if (params.getSortedByDurationAsc() != null) {
                query.orderBy(params.getSortedByDurationAsc() ?
                        cb.asc(root.get(TransportFieldName.DURATION.getRealName())) : cb.desc(root.get(TransportFieldName.DURATION.getRealName())));
            } else if (params.getSortedByDepartureTimeAsc() != null) {
                query.orderBy(params.getSortedByDepartureTimeAsc() ?
                        cb.asc(root.get(TransportFieldName.DEPARTURE_TIME.getRealName())) : cb.desc(root.get(TransportFieldName.DEPARTURE_TIME.getRealName())));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}