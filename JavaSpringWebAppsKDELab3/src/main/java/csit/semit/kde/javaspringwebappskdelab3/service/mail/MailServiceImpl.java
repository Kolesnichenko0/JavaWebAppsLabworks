package csit.semit.kde.javaspringwebappskdelab3.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * Service implementation for sending emails with attachments.
 * <p>
 * This service provides functionality to send emails with attachments asynchronously.
 * It uses the {@link JavaMailSender} to create and send MIME messages.
 * The service supports sending emails with both plain text and HTML content.
 * </p>
 * <p>
 * The `MailServiceImpl` class includes:
 * <ul>
 *   <li>Dependency injection of the {@link JavaMailSender}.</li>
 *   <li>Method to send an email with an attachment, specifying the recipient's email address, subject, email content, and attachment details.</li>
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
 *   <li>{@link JavaMailSender}</li>
 *   <li>{@link MimeMessage}</li>
 *   <li>{@link MimeMessageHelper}</li>
 *   <li>{@link ByteArrayResource}</li>
 *   <li>{@link MessagingException}</li>
 * </ul>
 * </p>
 * <p>
 * This class is annotated with {@link Service} to indicate that it's a Spring service component.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see MailService
 * @see JavaMailSender
 * @see MimeMessage
 * @see MimeMessageHelper
 * @see ByteArrayResource
 * @see MessagingException
 * @since 1.0.0
 */
@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmailWithAttachment(String toEmail, String subject, String text, boolean isHtmlContent, String attachmentName, byte[] attachmentBytes) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(text, isHtmlContent);

        ByteArrayResource resource = new ByteArrayResource(attachmentBytes);
        helper.addAttachment(attachmentName, resource);

        mailSender.send(message);
    }
}
