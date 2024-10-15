package csit.semit.kde.javahibernatewebappskdelab2.listeners;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Listener for HTTP session lifecycle events.
 * <p>
 * This listener logs the creation and destruction of HTTP sessions. It captures details such as the session ID when a session is created and logs the session ID when a session is destroyed.
 * </p>
 * <p>
 * The `SessionListener` class includes:
 * <ul>
 *   <li>Logging of session creation in the {@code sessionCreated} method</li>
 *   <li>Logging of session destruction in the {@code sessionDestroyed} method</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @see HttpSessionListener
 * @see HttpSessionEvent
 * @since 1.0.0
 */
public class SessionListener implements HttpSessionListener {

    private static final Logger logger = LogManager.getLogger(SessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        logger.info("Session created with ID: " + se.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        logger.info("Session destroyed with ID: " + se.getSession().getId());
    }
}
