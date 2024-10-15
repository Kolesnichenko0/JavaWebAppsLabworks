package csit.semit.kde.javahibernatewebappskdelab2.servlets.main;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * The {@code ProjectDescriptionServlet} class handles the project description page functionality of the web application.
 * It extends the {@link HttpServlet} class, and overrides the {@code doGet} and {@code doPost} methods to handle GET and POST requests respectively.
 * <p>
 * The servlet is mapped to the "/project-description" URL pattern.
 * <p>
 * The {@code ProjectDescriptionServlet} class forwards the request and response objects to the project description page.
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see HttpServlet
 * @see HttpServletRequest
 * @see HttpServletResponse
 * @since 1.0.0
 */
public class ProjectDescriptionServlet extends HttpServlet {

    private static final String PROJECT_DESCRIPTION_JSP_PATH = "/WEB-INF/views/main/project-description.jsp";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(PROJECT_DESCRIPTION_JSP_PATH).forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}

