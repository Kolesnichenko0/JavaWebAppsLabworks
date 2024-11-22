package csit.semit.kde.javaspringwebappskdelab3.service.export;

import csit.semit.kde.javaspringwebappskdelab3.enums.export.ExportFormat;

import java.util.List;

/**
 * Service interface for exporting data in various formats.
 * <p>
 * This interface defines the contract for exporting data to different formats such as EXCEL, CSV, and PDF.
 * Implementations of this interface should handle the conversion of data into the specified format and return the
 * resulting byte array.
 * </p>
 * <p>
 * The `exportData` method takes a list of data and an export format as parameters and returns the exported data as a byte array.
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * @Autowired
 * private ExportService exportService;
 *
 * public void someMethod() {
 *     List<MyData> data = fetchData();
 *     byte[] exportedData = exportService.exportData(data, ExportFormat.EXCEL);
 *     // Save or send the exported data
 * }
 * }
 * </pre>
 * </p>
 * <p>
 * Dependencies:
 * <ul>
 *   <li>{@link ExportFormat}</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @since 1.0.0
 */
public interface ExportService {
    byte[] exportData(List<?> data, ExportFormat format) throws Exception;
}
