package csit.semit.kde.javaspringwebappskdelab3.service.trainticket;

import csit.semit.kde.javaspringwebappskdelab3.dto.trainticket.TrainTicketDTO;
import csit.semit.kde.javaspringwebappskdelab3.dto.trainticket.TrainTicketExportRequestDTO;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceResult;

/**
 * Service interface for exporting and sending train ticket data.
 * <p>
 * This service provides functionality to export train ticket data based on the provided request parameters and send the exported data via email.
 * The service supports different export formats and handles the entire process from data retrieval to email sending.
 * </p>
 * <p>
 * The `TrainTicketExportService` interface includes:
 * <ul>
 *   <li>Method to export train ticket data and send it via email, specifying the request parameters, train ID, and the username of the requester.</li>
 * </ul>
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * @Autowired
 * private TrainTicketExportService trainTicketExportService;
 *
 * public void someMethod() {
 *     TrainTicketExportRequestDTO request = new TrainTicketExportRequestDTO();
 *     // Set request parameters
 *     ServiceResult<TrainTicketDTO> result = trainTicketExportService.exportAndSendTrainTickets(request, 123L, "username");
 *     // Handle the result
 * }
 * }
 * </pre>
 * </p>
 * <p>
 * Dependencies:
 * <ul>
 *   <li>{@link TrainTicketExportRequestDTO}</li>
 *   <li>{@link TrainTicketDTO}</li>
 *   <li>{@link ServiceResult}</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see TrainTicketExportRequestDTO
 * @see TrainTicketDTO
 * @see ServiceResult
 * @since 1.0.0
 */
public interface TrainTicketExportService {

    ServiceResult<TrainTicketDTO> exportAndSendTrainTickets(TrainTicketExportRequestDTO request, Long trainId, String username);
}
