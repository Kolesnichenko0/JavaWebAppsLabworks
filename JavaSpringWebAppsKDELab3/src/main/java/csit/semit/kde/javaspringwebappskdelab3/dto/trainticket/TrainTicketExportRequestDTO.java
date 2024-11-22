package csit.semit.kde.javaspringwebappskdelab3.dto.trainticket;

import csit.semit.kde.javaspringwebappskdelab3.enums.export.ExportFormat;
import csit.semit.kde.javaspringwebappskdelab3.util.criteria.trainticket.TrainTicketQueryParams;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for exporting train ticket data.
 * <p>
 * This class is used to encapsulate the parameters required for exporting train ticket data.
 * It includes the export format and query parameters for filtering the train ticket data.
 * </p>
 * <p>
 * The `TrainTicketExportRequestDTO` class includes:
 * <ul>
 *   <li>Export format (default is EXCEL)</li>
 *   <li>Query parameters for filtering train ticket data</li>
 * </ul>
 * </p>
 * <p>
 * This class uses Lombok annotations to generate boilerplate code such as getters, setters, constructors, and `toString` method.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see ExportFormat
 * @see TrainTicketQueryParams
 * @see lombok.Data
 * @see lombok.NoArgsConstructor
 * @see lombok.AllArgsConstructor
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainTicketExportRequestDTO {
    private ExportFormat format = ExportFormat.EXCEL;
    private TrainTicketQueryParams queryParams;
}
