package csit.semit.kde.javawebappskdelab12.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * The {@code ForwardToTableServlet} class is a servlet that handles HTTP requests to forward the client to a HTML table of employees.
 * It extends {@link jakarta.servlet.http.HttpServlet} and is mapped to the "/forward-to-table" URL.
 * <p>
 * The class overrides the {@link #doGet(HttpServletRequest, HttpServletResponse)} and {@link #doPost(HttpServletRequest, HttpServletResponse)} methods to handle GET and POST requests.
 * Both methods forward the client to the "/MyTable.html" URL.
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see jakarta.servlet.http.HttpServlet
 * @since 1.0.0
 */
@WebServlet("/forward-to-table")
public class ForwardToTableServlet extends HttpServlet {

    private static final String TABLE_PATH = "/MyTable.html";
    /**
     * Handles a GET request to forward the client to a HTML table of employees.
     *
     * @param request The {@link HttpServletRequest} object that contains the request the client made of the servlet.
     * @param response The {@link HttpServletResponse} object that contains the response the servlet sends to the client.
     * @throws ServletException If the request for the GET could not be handled.
     * @throws IOException If an input or output error is detected when the servlet handles the GET request.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(TABLE_PATH);
        requestDispatcher.forward(request, response);
    }
    /**
     * Handles a POST request to forward the client to a HTML table of employees.
     * This method simply calls {@link #doGet(HttpServletRequest, HttpServletResponse)} to handle the request.
     *
     * @param request The {@link HttpServletRequest} object that contains the request the client made of the servlet.
     * @param response The {@link HttpServletResponse} object that contains the response the servlet sends to the client.
     * @throws ServletException If the request for the POST could not be handled.
     * @throws IOException If an input or output error is detected when the servlet handles the POST request.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}