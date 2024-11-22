package csit.semit.kde.javaspringwebappskdelab3.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

/**
 * Service for generating email content using Thymeleaf templates.
 * <p>
 * This service provides functionality to generate HTML content for emails based on Thymeleaf templates.
 * It uses the `TemplateEngine` to process the templates with the provided variables.
 * </p>
 * <p>
 * The `EmailContentService` class includes:
 * <ul>
 *   <li>Dependency injection of the `TemplateEngine`.</li>
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
 * <p>
 * Dependencies:
 * <ul>
 *   <li>{@link TemplateEngine}</li>
 *   <li>{@link Context}</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see TemplateEngine
 * @see Context
 */
@Service
public class EmailContentServiceImpl implements EmailContentService {

    private final TemplateEngine templateEngine;

    @Autowired
    public EmailContentServiceImpl(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String generateEmailContent(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(templateName, context);
    }
}
