package csit.semit.kde.javawebappskdelab12.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
/**
 * The {@code RedirectToTableServlet} class is a servlet that handles HTTP requests to redirect the client to a HTML table of employees.
 * It extends {@link jakarta.servlet.http.HttpServlet} and is mapped to the "/redirect-to-table" URL.
 *
 * The class overrides the {@link #doGet(HttpServletRequest, HttpServletResponse)} and {@link #doPost(HttpServletRequest, HttpServletResponse)} methods to handle GET and POST requests.
 * Both methods redirect the client to the "/MyTable.html" URL.
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see jakarta.servlet.http.HttpServlet
 * @since 1.0.0
 */
@WebServlet("/redirect-to-table")
public class RedirectToTableServlet extends HttpServlet {

    private String tablePath;
    /**
     * Initializes the servlet. This method is called once when the servlet is loaded.
     * It sets the path to the HTML table of employees.
     */
    @Override
    public void init() {
        tablePath = getServletContext().getContextPath() + "/MyTable.html";
    }
    /**
     * Handles a GET request to redirect the client to a HTML table of employees.
     *
     * @param request The {@link HttpServletRequest} object that contains the request the client made of the servlet.
     * @param response The {@link HttpServletResponse} object that contains the response the servlet sends to the client.
     * @throws IOException If an input or output error is detected when the servlet handles the GET request.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(tablePath);
    }
    /**
     * Handles a POST request to redirect the client to a HTML table of employees.
     * This method simply calls {@link #doGet(HttpServletRequest, HttpServletResponse)} to handle the request.
     *
     * @param request The {@link HttpServletRequest} object that contains the request the client made of the servlet.
     * @param response The {@link HttpServletResponse} object that contains the response the servlet sends to the client.
     * @throws IOException If an input or output error is detected when the servlet handles the POST request.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}