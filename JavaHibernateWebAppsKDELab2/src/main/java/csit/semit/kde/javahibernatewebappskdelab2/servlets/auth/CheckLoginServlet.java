package csit.semit.kde.javahibernatewebappskdelab2.servlets.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

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
 * @see HttpServlet
 * @see HttpServletRequest
 * @see HttpServletResponse
 * @since 1.0.0
 */
public class CheckLoginServlet extends HttpServlet {
    private static final String LOGIN_JSP_PATH = "/WEB-INF/views/auth/login.jsp";
    private static final String VALID_USERNAME = "user";
    private static final String VALID_PASSWORD = "123456";

    private String mainPath;

    @Override
    public void init() {
        mainPath = getServletContext().getContextPath() + "/main";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(LOGIN_JSP_PATH).forward(request, response);
    }

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
