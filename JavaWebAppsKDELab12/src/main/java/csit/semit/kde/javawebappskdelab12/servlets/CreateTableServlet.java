package csit.semit.kde.javawebappskdelab12.servlets;

import java.io.*;

import csit.semit.kde.javawebappskdelab12.model.EmployeeList;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

/**
 * The {@code CreateTableServlet} class is a servlet that handles HTTP requests to create a HTML table of employees.
 * It extends {@link jakarta.servlet.http.HttpServlet} and is mapped to the "/create-table" URL.
 * <p>
 * The class overrides the {@link #doGet(HttpServletRequest, HttpServletResponse)} and {@link #doPost(HttpServletRequest, HttpServletResponse)} methods to handle GET and POST requests.
 * Both methods generate a HTML page that includes a table of employees' details, and send it as the response.
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see jakarta.servlet.http.HttpServlet
 * @see csit.semit.kde.javawebappskdelab12.model.EmployeeList
 * @since 1.0.0
 */
@WebServlet("/create-table")
public class CreateTableServlet extends HttpServlet {
    /**
     * Handles a GET request to create a HTML table of employees.
     *
     * @param request  The {@link HttpServletRequest} object that contains the request the client made of the servlet.
     * @param response The {@link HttpServletResponse} object that contains the response the servlet sends to the client.
     * @throws IOException If an input or output error is detected when the servlet handles the GET request.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        String html = """
                <!doctype html>
                <html lang="en">
                <head>
                <meta charset="utf-8">
                <title>KDE - Hello Table</title>
                <style>
                    table {
                        border-collapse: collapse;
                        width: 100%;
                    }
                    th, td {
                        border: 1px solid black;
                        padding: 5px;
                    }
                </style>
                </head>
                <body>
                <h1>CREATED TABLE</h1>
                <br><br>
                """ + EmployeeList.getInstance().toHtmlTable() + """
                <br><br>
                <a href=".">To START</a>
                </body>
                </html>
                """;
        out.println(html);
    }

    /**
     * Handles a POST request to create a HTML table of employees.
     * This method simply calls {@link #doGet(HttpServletRequest, HttpServletResponse)} to handle the request.
     *
     * @param request  The {@link HttpServletRequest} object that contains the request the client made of the servlet.
     * @param response The {@link HttpServletResponse} object that contains the response the servlet sends to the client.
     * @throws IOException If an input or output error is detected when the servlet handles the POST request.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}