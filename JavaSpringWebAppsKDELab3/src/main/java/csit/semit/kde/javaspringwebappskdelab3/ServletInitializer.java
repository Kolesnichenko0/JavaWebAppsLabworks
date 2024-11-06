package csit.semit.kde.javaspringwebappskdelab3;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Servlet initializer for the Java Spring Web Application.
 * <p>
 * This class extends {@link SpringBootServletInitializer} and is used to configure the application when it is launched
 * by a servlet container. It overrides the {@link #configure(SpringApplicationBuilder)} method to specify the primary
 * source for the application.
 * </p>
 * <p>
 * The main functionalities provided by this class include:
 * <ul>
 *   <li>Configuring the application context for the servlet container.</li>
 *   <li>Specifying the primary source for the Spring Boot application.</li>
 * </ul>
 * </p>
 * <p>
 *
 * @since 1.0.0
 */
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(JavaSpringWebAppsKdeLab3Application.class);
    }

}
