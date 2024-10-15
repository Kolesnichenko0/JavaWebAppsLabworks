package csit.semit.kde.javahibernatewebappskdelab2.servlets.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet that handles login requests.
 * <p>
 * This servlet is mapped to "/login" URL and provides handling of login requests.
 * It forwards GET requests to the login JSP page and handles POST requests by forwarding them to the same page.
 * </p>
 * <p>
 * The `LoginServlet` class includes:
 * <ul>
 *   <li>Forwarding GET requests to the login JSP page in the {@code doGet} method</li>
 *   <li>Handling POST requests by forwarding them to the {@code doGet} method</li>
 * </ul>
 * </p>
 * <p>
 * In a real-world application, you would typically handle login logic in the {@code doPost} method.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @see HttpServlet
 * @see HttpServletRequest
 * @see HttpServletResponse
 * @since 1.0.0
 */
public class LoginServlet extends HttpServlet {
    private static final String LOGIN_JSP_PATH = "/WEB-INF/views/auth/login.jsp";

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(LOGIN_JSP_PATH).forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
