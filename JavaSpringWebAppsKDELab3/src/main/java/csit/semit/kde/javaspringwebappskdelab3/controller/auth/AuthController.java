package csit.semit.kde.javaspringwebappskdelab3.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for handling authentication-related requests.
 * <p>
 * This controller provides endpoints for login functionality, including handling login errors,
 * logout messages, session expiration, and session invalidation.
 * </p>
 * <p>
 * The `login` method is mapped to the `/login` URL and processes various request parameters to
 * display appropriate messages on the login page.
 * </p>
 * <p>
 * The method checks for the presence of the following request parameters:
 * <ul>
 *   <li>`error`: Indicates an invalid username or password.</li>
 *   <li>`logout`: Indicates a successful logout.</li>
 *   <li>`sessionExpired`: Indicates that the session has expired and the user needs to log in again.</li>
 *   <li>`sessionInvalidated`: Indicates that the session has been invalidated due to a new login.</li>
 * </ul>
 * </p>
 * <p>
 * Based on these parameters, the method adds corresponding messages to the model to be displayed
 * on the login page.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see org.springframework.stereotype.Controller
 * @see org.springframework.ui.Model
 * @see org.springframework.web.bind.annotation.GetMapping
 * @see org.springframework.web.bind.annotation.RequestParam
 * @since 1.0.0
 */
@Controller
public class AuthController {
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        @RequestParam(value = "sessionExpired", required = false) String sessionExpired,
                        @RequestParam(value = "sessionInvalidated", required = false) String sessionInvalidated,
                        Model model) {

        if (error != null) {
            model.addAttribute("errorMsg", "Invalid username or password!");
        }

        if (logout != null) {
            model.addAttribute("logoutMsg", "You have been logged out successfully.");
        }

        if (sessionExpired != null) {
            model.addAttribute("sessionExpiredMsg", "Your session has expired. Please log in again.");
        }

        if (sessionInvalidated != null) {
            model.addAttribute("sessionInvalidatedMsg", "Your session has been invalidated due to a new login. Please log in again.");
        }

        return "auth/login";
    }
}