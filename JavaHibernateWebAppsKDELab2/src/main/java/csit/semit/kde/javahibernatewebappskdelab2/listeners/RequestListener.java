package csit.semit.kde.javahibernatewebappskdelab2.listeners;

import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Listener for HTTP request lifecycle events.
 * <p>
 * This listener logs the initialization and destruction of HTTP requests. It captures details such as the HTTP method
 * and request URI when a request is received and logs the completion of request processing.
 * </p>
 * <p>
 * The `RequestListener` class includes:
 * <ul>
 *   <li>Logging of request initialization in the {@code requestInitialized} method</li>
 *   <li>Logging of request destruction in the {@code requestDestroyed} method</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @see ServletRequestListener
 * @see HttpServletRequest
 * @since 1.0.0
 */
public class RequestListener implements ServletRequestListener {

    private static final Logger logger = LogManager.getLogger(RequestListener.class);

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();

        String method = request.getMethod();
        String requestURI = request.getRequestURI();

        logger.info("Request received: Method = {}, URI = {}", method, requestURI);
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        String requestURI = request.getRequestURI();

        logger.info("Request processing completed for URI: {}", requestURI);
    }
}
