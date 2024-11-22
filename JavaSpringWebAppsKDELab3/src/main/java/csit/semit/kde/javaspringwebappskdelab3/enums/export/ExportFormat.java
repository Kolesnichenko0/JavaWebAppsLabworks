package csit.semit.kde.javaspringwebappskdelab3.enums.export;

import lombok.Getter;

/**
 * Enumeration representing different export formats.
 * <p>
 * This enum defines the supported export formats for data export functionality in the application.
 * Each format is associated with a file extension.
 * </p>
 * <p>
 * The `ExportFormat` enum includes:
 * <ul>
 *   <li>EXCEL: Represents the Excel format with the file extension "xlsx".</li>
 *   <li>CSV: Represents the CSV format with the file extension "csv".</li>
 *   <li>PDF: Represents the PDF format with the file extension "pdf".</li>
 * </ul>
 * </p>
 * <p>
 * This enum uses the Lombok `@Getter` annotation to generate getter methods for the `extension` field.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see lombok.Getter
 * @since 1.0.0
 */
@Getter
public enum ExportFormat {
    EXCEL("xlsx"),
    CSV("csv"),
    PDF("pdf");

    private final String extension;

    ExportFormat(String extension) {
        this.extension = extension;
    }
}