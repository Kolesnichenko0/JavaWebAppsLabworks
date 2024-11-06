package csit.semit.kde.javaspringwebappskdelab3.repository.trainticket;

import csit.semit.kde.javaspringwebappskdelab3.entity.trainticket.TrainTicket;
import csit.semit.kde.javaspringwebappskdelab3.util.fieldname.trainticket.TicketFieldName;
import csit.semit.kde.javaspringwebappskdelab3.util.fieldname.trainticket.TrainTicketFieldName;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
/**
 * Repository interface for managing {@link TrainTicket} entities.
 * <p>
 * This interface extends {@link JpaRepository} and {@link JpaSpecificationExecutor} to provide CRUD operations
 * and specification-based querying capabilities for {@link TrainTicket} entities.
 * </p>
 * <p>
 * The main functionalities provided by this repository include:
 * <ul>
 *   <li>Finding a train ticket by train ID, seat number, carriage number, and departure date.</li>
 *   <li>Checking if a train ticket exists by train ID, seat number, carriage number, and departure date.</li>
 *   <li>Retrieving all train tickets with a default sort order.</li>
 * </ul>
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * @Autowired
 * private TrainTicketRepository trainTicketRepository;
 *
 * public void someMethod() {
 *     Optional<TrainTicket> ticket = trainTicketRepository.findByTrainIdAndSeatNumberAndCarriageNumberAndDepartureDate(1L, 10, 2, LocalDate.now());
 *     boolean exists = trainTicketRepository.existsByTrainIdAndSeatNumberAndCarriageNumberAndDepartureDate(1L, 10, 2, LocalDate.now());
 *     List<TrainTicket> tickets = trainTicketRepository.findAllWithDefaultSort();
 * }
 * }
 * </pre>
 * </p>
 * <p>
 * Dependencies:
 * <ul>
 *   <li>{@link TrainTicket}</li>
 *   <li>{@link JpaRepository}</li>
 *   <li>{@link JpaSpecificationExecutor}</li>
 *   <li>{@link Specification}</li>
 *   <li>{@link Sort}</li>
 *   <li>{@link Optional}</li>
 *   <li>{@link LocalDate}</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @see TrainTicket
 * @see JpaRepository
 * @see JpaSpecificationExecutor
 * @see Specification
 * @see Sort
 * @see Optional
 * @see LocalDate
 * @since 1.0.0
 */
public interface TrainTicketRepository extends JpaRepository<TrainTicket, Long>, JpaSpecificationExecutor<TrainTicket> {
    Optional<TrainTicket> findByTrainIdAndSeatNumberAndCarriageNumberAndDepartureDate(Long trainId, Integer seatNumber, Integer carriageNumber, LocalDate departureDate);

    boolean existsByTrainIdAndSeatNumberAndCarriageNumberAndDepartureDate(Long trainId, Integer seatNumber, Integer carriageNumber, LocalDate departureDate);

    default List<TrainTicket> findAllWithDefaultSort() {
        return findAll(getDefaultSort());
    }

    default List<TrainTicket> findAllWithDefaultSort(Specification<TrainTicket> spec) {
        return findAll(spec, getDefaultSort());
    }

    private Sort getDefaultSort() {
        return Sort.by(
                Sort.Order.asc(TicketFieldName.DEPARTURE_DATE.getRealName()),
                Sort.Order.asc(TrainTicketFieldName.CARRIAGE_NUMBER.getRealName()),
                Sort.Order.asc(TicketFieldName.SEAT_NUMBER.getRealName())
        );
    }
}