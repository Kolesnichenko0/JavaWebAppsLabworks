package csit.semit.kde.javaspringwebappskdelab3.service.export;

import csit.semit.kde.javaspringwebappskdelab3.enums.export.ExportFormat;
import csit.semit.kde.javaspringwebappskdelab3.entity.train.Train;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service implementation for exporting data in various formats.
 * <p>
 * This class provides the functionality to export data to different formats such as EXCEL, CSV, and PDF.
 * It uses Apache POI for EXCEL export and can be extended to support other formats.
 * </p>
 * <p>
 * The `ExportServiceImpl` class includes methods for:
 * <ul>
 *   <li>Exporting data to EXCEL format</li>
 *   <li>Exporting data to CSV format</li>
 *   <li>Exporting data to PDF format (not yet implemented)</li>
 * </ul>
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * @Autowired
 * private ExportService exportService;
 *
 * public void someMethod() {
 *     List<Train> trains = fetchTrains();
 *     byte[] excelData = exportService.exportData(trains, ExportFormat.EXCEL);
 *     // Save or send the exported data
 * }
 * }
 * </pre>
 * </p>
 * <p>
 * Dependencies:
 * <ul>
 *   <li>{@link ExportFormat}</li>
 *   <li>{@link Train}</li>
 *   <li>Apache POI</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see ExportFormat
 * @see Train
 * @since 1.0.0
 */
@Service
public class ExportServiceImpl implements ExportService {

    @Override
    public byte[] exportData(List<?> data, ExportFormat format) throws Exception {
        return switch (format) {
            case EXCEL -> exportToExcel(data);
            case CSV -> exportToCsv(data);
            case PDF -> exportToPdf(data);
            default -> throw new UnsupportedOperationException("Unsupported export format: " + format);
        };
    }

    private byte[] exportToExcel(List<?> data) throws IOException, IllegalAccessException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Exported Data");

        if (data == null || data.isEmpty()) {
            workbook.close();
            return new byte[0];
        }

        Object firstItem = data.get(0);
        Field[] fields = firstItem.getClass().getDeclaredFields();

        Row headerRow = sheet.createRow(0);
        int headerCol = 0;
        for (Field field : fields) {
            field.setAccessible(true);
            Cell cell = headerRow.createCell(headerCol++);
            cell.setCellValue(getFieldDisplayName(field));
        }

        int rowNum = 1;
        for (Object item : data) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (Field field : fields) {
                field.setAccessible(true);
                Cell cell = row.createCell(colNum++);
                Object value = field.get(item);
                cell.setCellValue(formatFieldValue(field, value));
            }
        }

        for (int i = 0; i < fields.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();
        return bos.toByteArray();
    }

    private byte[] exportToCsv(List<?> data) throws IOException, IllegalAccessException {
        if (data == null || data.isEmpty()) {
            return new byte[0];
        }

        StringBuilder sb = new StringBuilder();
        Object firstItem = data.get(0);
        Field[] fields = firstItem.getClass().getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            sb.append(getFieldDisplayName(fields[i]));
            if (i < fields.length - 1) {
                sb.append(",");
            }
        }
        sb.append("\n");

        for (Object item : data) {
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                Object value = field.get(item);
                sb.append(formatFieldValue(field, value));
                if (i < fields.length - 1) {
                    sb.append(",");
                }
            }
            sb.append("\n");
        }

        return sb.toString().getBytes();
    }

    private byte[] exportToPdf(List<?> data) {
        // iText или Apache PDFBox
        throw new UnsupportedOperationException("PDF export is not implemented yet.");
    }


    private String getFieldDisplayName(Field field) {
        String name = field.getName();
        StringBuilder displayName = new StringBuilder();
        for (char c : name.toCharArray()) {
            if (Character.isUpperCase(c)) {
                displayName.append(' ');
            }
            displayName.append(c);
        }
        return displayName.toString();
    }

    private String formatFieldValue(Field field, Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof LocalDate) {
            return ((LocalDate) value).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        }
        if (value instanceof LocalTime) {
            return ((LocalTime) value).format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return value.toString();
    }
}
