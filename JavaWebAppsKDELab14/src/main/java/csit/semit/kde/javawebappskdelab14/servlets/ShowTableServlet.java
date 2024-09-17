package csit.semit.kde.javawebappskdelab14.servlets;

import csit.semit.kde.javawebappskdelab14.entity.EmployeeList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * The {@code ShowTableServlet} class handles the display of the employee table in the web application.
 * It extends the {@link jakarta.servlet.http.HttpServlet} class, and overrides the {@code doGet} and {@code doPost} methods to handle GET and POST requests respectively.
 * <p>
 * The servlet is mapped to the "/show-table" URL pattern.
 * <p>
 * The {@code ShowTableServlet} class retrieves the list of employees and forwards the request and response objects to the table page.
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see jakarta.servlet.http.HttpServlet
 * @see jakarta.servlet.http.HttpServletRequest
 * @see jakarta.servlet.http.HttpServletResponse
 * @since 1.0.0
 */
@WebServlet("/show-table")
public class ShowTableServlet extends HttpServlet {

    private static final String TABLE_JSP_PATH = "/jsp/table.jsp";

    /**
     * Handles the HTTP GET method.
     * It retrieves the list of employees and forwards the request and response objects to the table page.
     *
     * @param request  the request object that contains client request data
     * @param response the response object to assist in sending a response to the client
     * @throws ServletException if an input or output error is detected when the servlet handles the GET request
     * @throws IOException      if the request for the GET could not be handled
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EmployeeList employeeList = EmployeeList.getInstance();
        request.setAttribute("employeeList", employeeList);
        request.getRequestDispatcher(TABLE_JSP_PATH).forward(request, response);
    }

    /**
     * Handles the HTTP POST method.
     * It retrieves the list of employees and forwards the request and response objects to the table page.
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