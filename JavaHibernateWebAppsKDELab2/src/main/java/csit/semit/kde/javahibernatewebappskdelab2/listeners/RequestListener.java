package csit.semit.kde.javahibernatewebappskdelab2.listeners;

import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
