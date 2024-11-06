package csit.semit.kde.javaspringwebappskdelab3.service.trainticket;

import csit.semit.kde.javaspringwebappskdelab3.dto.trainticket.TrainTicketDTO;
import csit.semit.kde.javaspringwebappskdelab3.util.criteria.trainticket.TrainTicketQueryParams;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceResult;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Service interface for managing train ticket-related operations.
 * <p>
 * This interface defines methods for performing CRUD operations and other business logic
 * related to {@link TrainTicketDTO} entities. It also provides methods for querying train tickets
 * based on various parameters.
 * </p>
 * <p>
 * The main functionalities provided by this service include:
 * <ul>
 *   <li>Finding a train ticket by its ID.</li>
 *   <li>Finding a train ticket by a combination of train ID, seat number, carriage number, and departure date.</li>
 *   <li>Querying train tickets based on various parameters.</li>
 *   <li>Saving and updating train ticket information.</li>
 *   <li>Deleting train tickets individually or in bulk.</li>
 * </ul>
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * @Autowired
 * private TrainTicketService trainTicketService;
 *
 * public void someMethod() {
 *     ServiceResult<TrainTicketDTO> result = trainTicketService.findById(1L);
 *     if (result.isSuccess()) {
 *         TrainTicketDTO trainTicket = result.getData();
 *         // Process the train ticket data
 *     }
 * }
 * }
 * </pre>
 * </p>
 * <p>
 * Dependencies:
 * <ul>
 *   <li>{@link TrainTicketDTO}</li>
 *   <li>{@link TrainTicketQueryParams}</li>
 *   <li>{@link ServiceResult}</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @see TrainTicketDTO
 * @see TrainTicketQueryParams
 * @see ServiceResult
 * @since 1.0.0
 */
public interface TrainTicketService {
    ServiceResult<TrainTicketDTO> findById(Long id);

    ServiceResult<TrainTicketDTO> findByKeySet(Long trainId, Integer seatNumber, Integer carriageNumber, LocalDate departureDate);

    ServiceResult<TrainTicketDTO> findByTrainIdAndSeatNumberAndCarriageNumberAndDepartureDate(Long trainId, Integer seatNumber, Integer carriageNumber, LocalDate departureDate);

    ServiceResult<TrainTicketDTO> findTicketTrains(TrainTicketQueryParams queryParams);

    ServiceResult<TrainTicketDTO> save(TrainTicketDTO trainTicketDTO);

    ServiceResult<TrainTicketDTO> saveAll(List<TrainTicketDTO> trainTicketDTOs);

    ServiceResult<TrainTicketDTO> updatePartial(Long id, Map<String, Object> updates);

    ServiceResult<Void> delete(Long id);

    ServiceResult<Void> deleteAll();
}