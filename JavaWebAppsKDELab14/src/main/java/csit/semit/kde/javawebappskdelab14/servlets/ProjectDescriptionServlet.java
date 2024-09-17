package csit.semit.kde.javawebappskdelab14.servlets;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * The {@code ProjectDescriptionServlet} class handles the project description page functionality of the web application.
 * It extends the {@link jakarta.servlet.http.HttpServlet} class, and overrides the {@code doGet} and {@code doPost} methods to handle GET and POST requests respectively.
 * <p>
 * The servlet is mapped to the "/project-description" URL pattern.
 * <p>
 * The {@code ProjectDescriptionServlet} class forwards the request and response objects to the project description page.
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see jakarta.servlet.http.HttpServlet
 * @see jakarta.servlet.http.HttpServletRequest
 * @see jakarta.servlet.http.HttpServletResponse
 * @since 1.0.0
 */
@WebServlet("/project-description")
public class ProjectDescriptionServlet extends HttpServlet {

    private static final String PROJECT_DESCRIPTION_JSP_PATH = "/jsp/project-description.jsp";

    /**
     * Handles the HTTP GET method.
     * It forwards the request and response objects to the project description page.
     *
     * @param request  the request object that contains client request data
     * @param response the response object to assist in sending a response to the client
     * @throws ServletException if an input or output error is detected when the servlet handles the GET request
     * @throws IOException      if the request for the GET could not be handled
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(PROJECT_DESCRIPTION_JSP_PATH).forward(request, response);
    }

    /**
     * Handles the HTTP POST method.
     * It forwards the request and response objects to the project description page.
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

