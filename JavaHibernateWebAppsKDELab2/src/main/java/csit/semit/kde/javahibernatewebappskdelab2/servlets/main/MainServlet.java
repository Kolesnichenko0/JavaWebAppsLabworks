package csit.semit.kde.javahibernatewebappskdelab2.servlets.main;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * The {@code MainServlet} class handles the main page functionality of the web application.
 * It extends the {@link jakarta.servlet.http.HttpServlet} class, and overrides the {@code doGet} and {@code doPost} methods to handle GET and POST requests respectively.
 * <p>
 * The servlet is mapped to the "/main" URL pattern.
 * <p>
 * The {@code MainServlet} class forwards the request and response objects to the main page.
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see jakarta.servlet.http.HttpServlet
 * @see jakarta.servlet.http.HttpServletRequest
 * @see jakarta.servlet.http.HttpServletResponse
 * @since 1.0.0
 */
public class MainServlet extends HttpServlet {
    private static final String MAIN_JSP_PATH = "/WEB-INF/views/main/main.jsp";

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(MAIN_JSP_PATH).forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
