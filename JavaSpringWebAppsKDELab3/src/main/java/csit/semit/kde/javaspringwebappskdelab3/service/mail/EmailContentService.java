package csit.semit.kde.javaspringwebappskdelab3.service.mail;

import java.util.Map;

/**
 * Interface for generating email content using Thymeleaf templates.
 * <p>
 * This interface defines the contract for generating HTML content for emails based on Thymeleaf templates.
 * Implementations of this interface should handle the processing of templates with the provided variables.
 * </p>
 * <p>
 * The `EmailContentService` interface includes:
 * <ul>
 *   <li>Method to generate email content from a template and variables.</li>
 * </ul>
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * @Autowired
 * private EmailContentService emailContentService;
 *
 * public void someMethod() {
 *     Map<String, Object> variables = new HashMap<>();
 *     variables.put("name", "John Doe");
 *     String content = emailContentService.generateEmailContent("email/template", variables);
 *     // Use the generated content
 * }
 * }
 * </pre>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @since 1.0.0
 */
public interface EmailContentService {

    String generateEmailContent(String templateName, Map<String, Object> variables);
}