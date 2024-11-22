package csit.semit.kde.javaspringwebappskdelab3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for setting up password encoding in the application.
 * <p>
 * This class is annotated with `@Configuration`, indicating that it is a source of bean definitions.
 * It provides a bean of type `PasswordEncoder` that uses the `BCryptPasswordEncoder` implementation.
 * </p>
 * <p>
 * The `BCryptPasswordEncoder` is a password hashing function designed for secure password storage.
 * It is a one-way hashing algorithm that incorporates a salt to protect against rainbow table attacks.
 * </p>
 * <p>
 * The `passwordEncoder` method is annotated with `@Bean`, which tells Spring to manage it as a bean
 * and make it available for dependency injection throughout the application.
 * </p>
 * <p>
 * This configuration ensures that any component in the application that requires a `PasswordEncoder`
 * will receive an instance of `BCryptPasswordEncoder`, promoting secure password handling practices.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @see org.springframework.context.annotation.Configuration
 * @see org.springframework.security.crypto.password.PasswordEncoder
 * @see org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
 * @since 1.0.0
 */
@Configuration
public class EncoderConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
