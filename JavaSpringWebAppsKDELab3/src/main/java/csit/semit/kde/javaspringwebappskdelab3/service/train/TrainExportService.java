package csit.semit.kde.javaspringwebappskdelab3.service.train;

import csit.semit.kde.javaspringwebappskdelab3.dto.train.TrainExportRequestDTO;
import csit.semit.kde.javaspringwebappskdelab3.dto.train.TrainDTO;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceResult;

/**
 * Service interface for exporting and sending train data.
 * <p>
 * This service provides functionality to export train data based on the provided request parameters and send the exported data via email.
 * The service supports different export formats and handles the entire process from data retrieval to email sending.
 * </p>
 * <p>
 * The `TrainExportService` interface includes:
 * <ul>
 *   <li>Method to export train data and send it via email, specifying the request parameters and the username of the requester.</li>
 * </ul>
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * @Autowired
 * private TrainExportService trainExportService;
 *
 * public void someMethod() {
 *     TrainExportRequestDTO request = new TrainExportRequestDTO();
 *     // Set request parameters
 *     ServiceResult<TrainDTO> result = trainExportService.exportAndSendTrains(request, "username");
 *     // Handle the result
 * }
 * }
 * </pre>
 * </p>
 * <p>
 * Dependencies:
 * <ul>
 *   <li>{@link TrainExportRequestDTO}</li>
 *   <li>{@link TrainDTO}</li>
 *   <li>{@link ServiceResult}</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see TrainExportRequestDTO
 * @see TrainDTO
 * @see ServiceResult
 * @since 1.0.0
 */
public interface TrainExportService {
    ServiceResult<TrainDTO> exportAndSendTrains(TrainExportRequestDTO request, String username);
}
