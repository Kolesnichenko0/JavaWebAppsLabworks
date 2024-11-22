package csit.semit.kde.javaspringwebappskdelab3.service.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Service implementation for local file storage operations.
 * <p>
 * This service provides functionality to save files locally with specific details such as username, format, and attachment name.
 * The file content is provided as a byte array. The service supports enabling or disabling local file saving through configuration.
 * </p>
 * <p>
 * The `LocalFileStorageService` class includes:
 * <ul>
 *   <li>Dependency injection of configuration properties for enabling local save and specifying the storage path.</li>
 *   <li>Method to save a file locally with the specified username, format, attachment name, and file content.</li>
 *   <li>Initialization of the storage directory if it does not exist.</li>
 * </ul>
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * @Autowired
 * private FileStorageService fileStorageService;
 *
 * public void someMethod() {
 *     byte[] fileData = ...; // Load or generate file data
 *     fileStorageService.saveFile("username", "pdf", "document.pdf", fileData);
 * }
 * }
 * </pre>
 * </p>
 * <p>
 * Dependencies:
 * <ul>
 *   <li>{@link Value}</li>
 *   <li>{@link Service}</li>
 *   <li>{@link File}</li>
 *   <li>{@link FileOutputStream}</li>
 *   <li>{@link IOException}</li>
 *   <li>{@link LocalDateTime}</li>
 *   <li>{@link DateTimeFormatter}</li>
 * </ul>
 * </p>
 * <p>
 * This class is annotated with {@link Service} to indicate that it's a Spring service component.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see FileStorageService
 * @see Value
 * @see Service
 * @see File
 * @see FileOutputStream
 * @see IOException
 * @see LocalDateTime
 * @see DateTimeFormatter
 * @since 1.0.0
 */
@Service
public class LocalFileStorageService implements FileStorageService {
    private final boolean isLocalSaveEnabled;
    private final String storagePath;

    public LocalFileStorageService(
            @Value("${export.local.save}") boolean isLocalSaveEnabled,
            @Value("${export.storage.path}") String storagePath) {
        this.isLocalSaveEnabled = isLocalSaveEnabled;
        this.storagePath = storagePath;
        initializeStorage();
    }

    private void initializeStorage() {
        if (!isLocalSaveEnabled) {
            return;
        }

        File directory = new File(storagePath);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("Created storage directory at: " + directory.getAbsolutePath());
            } else {
                System.err.println("Failed to create storage directory at: " + directory.getAbsolutePath());
            }
        }
    }

    @Override
    public void saveFile(String username, String format, String attachmentName, byte[] fileBytes) throws IOException {
        if (!isLocalSaveEnabled) {
            return;
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        String filename = String.format("%s_%s_%s", username, timestamp, attachmentName);

        File file = new File(storagePath + File.separator + filename);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(fileBytes);
            System.out.println("Saved file locally: " + file.getAbsolutePath());
        }
    }
}
