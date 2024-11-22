package csit.semit.kde.javaspringwebappskdelab3.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Configuration class for setting up email sending capabilities in the application.
 * <p>
 * This class is annotated with `@Configuration`, indicating that it is a source of bean definitions.
 * It provides a bean of type `JavaMailSender` that is configured to use Gmail's SMTP server.
 * </p>
 * <p>
 * The `getJavaMailSender` method is annotated with `@Bean`, which tells Spring to manage it as a bean
 * and make it available for dependency injection throughout the application.
 * </p>
 * <p>
 * The method uses the `Dotenv` library to load environment variables from a `.env` file, allowing
 * for secure storage of sensitive information such as email username and password.
 * </p>
 * <p>
 * The `JavaMailSenderImpl` is configured with properties for SMTP authentication, STARTTLS, and debug mode.
 * </p>
 * <p>
 * This configuration ensures that any component in the application that requires a `JavaMailSender`
 * will receive a properly configured instance, promoting secure and reliable email sending practices.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @see org.springframework.context.annotation.Configuration
 * @see org.springframework.mail.javamail.JavaMailSender
 * @see org.springframework.mail.javamail.JavaMailSenderImpl
 * @since 1.0.0
 */
@Configuration
public class MailConfig {
    @Bean
    public JavaMailSender getJavaMailSender() {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername(dotenv.get("MAIL_USERNAME"));
        mailSender.setPassword(dotenv.get("MAIL_PASSWORD"));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}