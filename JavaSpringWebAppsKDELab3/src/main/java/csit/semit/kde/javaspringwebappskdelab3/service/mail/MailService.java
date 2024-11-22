package csit.semit.kde.javaspringwebappskdelab3.service.mail;

import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Async;

/**
 * Service interface for sending emails with attachments.
 * <p>
 * This interface defines the contract for sending emails with attachments asynchronously.
 * It includes a method to send an email with an attachment, specifying the recipient's email address,
 * subject, email content, and attachment details.
 * </p>
 * <p>
 * The `MailService` interface includes:
 * <ul>
 *   <li>Asynchronous method to send an email with an attachment.</li>
 * </ul>
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * @Autowired
 * private MailService mailService;
 *
 * public void someMethod() {
 *     byte[] attachmentData = ...; // Load or generate attachment data
 *     mailService.sendEmailWithAttachment("recipient@example.com", "Subject", "Email content", true, "attachment.pdf", attachmentData);
 * }
 * }
 * </pre>
 * </p>
 * <p>
 * Dependencies:
 * <ul>
 *   <li>{@link jakarta.mail.MessagingException}</li>
 *   <li>{@link org.springframework.scheduling.annotation.Async}</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @since 1.0.0
 */
public interface MailService {
    @Async
    void sendEmailWithAttachment(String toEmail, String subject, String text, boolean isHtmlContent, String attachmentName, byte[] attachmentBytes) throws MessagingException;

}