package csit.semit.kde.javaspringwebappskdelab3.service.storage;

/**
 * Service interface for file storage operations.
 * <p>
 * This service provides functionality to save files with specific details such as username, format, and attachment name.
 * The file content is provided as a byte array.
 * </p>
 * <p>
 * The `FileStorageService` interface includes:
 * <ul>
 *   <li>Method to save a file with the specified username, format, attachment name, and file content.</li>
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
 *   <li>None</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see Exception
 * @since 1.0.0
 */
public interface FileStorageService {
    void saveFile(String username, String format, String attachmentName, byte[] fileBytes) throws Exception;
}