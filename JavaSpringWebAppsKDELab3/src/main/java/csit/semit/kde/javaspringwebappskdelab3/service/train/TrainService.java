package csit.semit.kde.javaspringwebappskdelab3.service.train;

import csit.semit.kde.javaspringwebappskdelab3.dto.train.TrainDTO;
import csit.semit.kde.javaspringwebappskdelab3.dto.trainticket.TrainTicketDTO;
import csit.semit.kde.javaspringwebappskdelab3.util.criteria.train.TrainQueryParams;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceResult;

import java.util.List;

/**
 * Service interface for managing train-related operations.
 * <p>
 * This interface defines methods for performing CRUD operations and other business logic
 * related to {@link TrainDTO} entities. It also provides methods for querying train tickets
 * associated with a specific train.
 * </p>
 * <p>
 * The main functionalities provided by this service include:
 * <ul>
 *   <li>Finding a train by its ID, key set, or number.</li>
 *   <li>Querying trains based on various parameters.</li>
 *   <li>Saving and updating train information.</li>
 *   <li>Deleting trains individually or in bulk.</li>
 *   <li>Retrieving train tickets associated with a specific train.</li>
 * </ul>
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * @Autowired
 * private TrainService trainService;
 *
 * public void someMethod() {
 *     ServiceResult<TrainDTO> result = trainService.findById(1L);
 *     if (result.isSuccess()) {
 *         TrainDTO train = result.getData();
 *         // Process the train data
 *     }
 * }
 * }
 * </pre>
 * </p>
 * <p>
 * Dependencies:
 * <ul>
 *   <li>{@link TrainDTO}</li>
 *   <li>{@link TrainTicketDTO}</li>
 *   <li>{@link TrainQueryParams}</li>
 *   <li>{@link ServiceResult}</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @see TrainDTO
 * @see TrainTicketDTO
 * @see TrainQueryParams
 * @see ServiceResult
 * @since 1.0.0
 */
public interface TrainService {
    ServiceResult<TrainDTO> findById(Long id);

    ServiceResult<TrainDTO> findByKeySet(String number);

    ServiceResult<TrainDTO> findByNumber(String number);

    ServiceResult<TrainDTO> findTrains(TrainQueryParams queryParams);

    ServiceResult<TrainDTO> save(TrainDTO trainDTO);

    ServiceResult<TrainDTO> saveAll(List<TrainDTO> trainDTOs);

    ServiceResult<TrainDTO> update(Long id, TrainDTO trainDTO);

    ServiceResult<Void> delete(Long id);

    ServiceResult<Void> deleteAll();

    ServiceResult<TrainTicketDTO> getTrainTickets(Long id);
}