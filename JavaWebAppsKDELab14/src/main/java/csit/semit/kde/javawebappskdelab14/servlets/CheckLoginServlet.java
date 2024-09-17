package csit.semit.kde.javawebappskdelab14.servlets;

import org.json.JSONObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet that handles login requests.
 * <p>
 * This servlet is mapped to "/check-login" URL and provides handling of login requests.
 * It checks if the provided username and password match the valid ones.
 * If they do, it responds with a JSON object containing a redirect URL.
 * If they don't, it responds with a JSON object containing an error message.
 * </p>
 * <p>
 * The valid username and password are hardcoded in this example.
 * In a real-world application, you would typically check the provided credentials against a database.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see jakarta.servlet.http.HttpServlet
 * @see jakarta.servlet.http.HttpServletRequest
 * @see jakarta.servlet.http.HttpServletResponse
 * @since 1.0.0
 */


@WebServlet("/check-login")
public class CheckLoginServlet extends HttpServlet {
    private static final String LOGIN_JSP_PATH = "/jsp/login.jsp";
    private static final String VALID_USERNAME = "user";
    private static final String VALID_PASSWORD = "123456";

    private String mainPath;

    /**
     * Initializes the servlet. This method is called by the servlet container before the servlet can service any request.
     * It sets the path to the main page of the web application.
     */
    @Override
    public void init() {
        mainPath = getServletContext().getContextPath() + "/main";
    }

    /**
     * Handles the HTTP GET method.
     * It forwards the request and response objects to the login page.
     *
     * @param request  the request object that contains client request data
     * @param response the response object to assist in sending a response to the client
     * @throws ServletException if an input or output error is detected when the servlet handles the GET request
     * @throws IOException      if the request for the GET could not be handled
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(LOGIN_JSP_PATH).forward(request, response);
    }

    /**
     * Handles the HTTP POST method.
     * <p>
     * It checks if the provided username and password match the valid ones.
     * If they do, it responds with a JSON object containing a redirect URL.
     * If they don't, it responds with a JSON object containing an error message.
     * </p>
     *
     * @param request  the request object that contains client request data
     * @param response the response object to assist in sending a response to the client
     * @throws IOException if the request for the POST could not be handled
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        response.setContentType("application/json");

        JSONObject jsonResponse = new JSONObject();
        if (VALID_USERNAME.equals(username) && VALID_PASSWORD.equals(password)) {
            response.setStatus(HttpServletResponse.SC_OK);
            jsonResponse.put("redirect", mainPath);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            jsonResponse.put("error", "Invalid username or password");
        }
        response.getWriter().write(jsonResponse.toString());
    }
}
