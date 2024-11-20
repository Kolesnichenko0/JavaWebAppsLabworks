package csit.semit.kde.javaspringwebappskdelab3.controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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