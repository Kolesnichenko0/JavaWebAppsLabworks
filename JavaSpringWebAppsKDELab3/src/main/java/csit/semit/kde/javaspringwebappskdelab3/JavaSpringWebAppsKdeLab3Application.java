package csit.semit.kde.javaspringwebappskdelab3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main class for the Java Spring Web Application.
 * <p>
 * This is the entry point for the Spring Boot application. It contains the main method which uses {@link SpringApplication#run(Class, String...)}
 * to launch the application.
 * </p>
 * <p>
 * The main functionalities provided by this class include:
 * <ul>
 *   <li>Launching the Spring Boot application.</li>
 *   <li>Configuring the application context and initializing beans.</li>
 * </ul>
 * </p>
 * <p>
 *
 * @since 1.0.0
 */
@SpringBootApplication
@EnableAsync
public class JavaSpringWebAppsKdeLab3Application {
    public static void main(String[] args) {
        SpringApplication.run(JavaSpringWebAppsKdeLab3Application.class, args);
    }
}
