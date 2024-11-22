package csit.semit.kde.javaspringwebappskdelab3.dto.train;

import csit.semit.kde.javaspringwebappskdelab3.enums.export.ExportFormat;
import csit.semit.kde.javaspringwebappskdelab3.util.criteria.train.TrainQueryParams;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for exporting train data.
 * <p>
 * This class is used to encapsulate the parameters required for exporting train data.
 * It includes the export format and query parameters for filtering the train data.
 * </p>
 * <p>
 * The `TrainExportRequestDTO` class includes:
 * <ul>
 *   <li>Export format (default is EXCEL)</li>
 *   <li>Query parameters for filtering train data</li>
 * </ul>
 * </p>
 * <p>
 * This class uses Lombok annotations to generate boilerplate code such as getters, setters, constructors, and `toString` method.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see ExportFormat
 * @see TrainQueryParams
 * @see Data
 * @see NoArgsConstructor
 * @see AllArgsConstructor
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainExportRequestDTO {
    private ExportFormat format = ExportFormat.EXCEL;
    private TrainQueryParams queryParams;
}
