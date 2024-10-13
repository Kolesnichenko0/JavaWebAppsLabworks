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

    /**
     * Handles the HTTP GET method.
     * It forwards the request and response objects to the main page.
     *
     * @param request  the request object that contains client request data
     * @param response the response object to assist in sending a response to the client
     * @throws ServletException if an input or output error is detected when the servlet
     *                          handles the GET request
     * @throws IOException      if the request for the GET could not be handled
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(MAIN_JSP_PATH).forward(request, response);
    }

    /**
     * Handles the HTTP POST method.
     * It forwards the request and response objects to the main page.
     *
     * @param request  the request object that contains client request data
     * @param response the response object to assist in sending a response to the client
     * @throws ServletException if an input or output error is detected when the servlet handles the POST request
     * @throws IOException      if the request for the POST could not be handled
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
