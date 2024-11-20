package csit.semit.kde.javaspringwebappskdelab3.controller.main;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * MainController is a Spring MVC controller that handles requests for the home page and project description page.
 * <p>
 * This controller provides two main endpoints:
 * <ul>
 *     <li>{@code /home} or {@code /} - Displays the home page with a model attribute indicating the user is logged in.</li>
 *     <li>{@code /project-description} - Displays the project description page with a model attribute indicating the user is not logged in.</li>
 * </ul>
 * <p>
 * The {@code home} method sets a model attribute {@code isLoggedIn} to {@code true} and returns the view name {@code main/home}.
 * The {@code projectDescription} method sets a model attribute {@code isLoggedIn} to {@code false} and returns the view name {@code main/project-description}.
 * </p>
 * <p>
 * This controller uses the {@link org.springframework.stereotype.Controller} annotation to indicate that it is a Spring MVC controller.
 * The {@link org.springframework.web.bind.annotation.GetMapping} annotation is used to map HTTP GET requests to handler methods.
 * </p>
 * <p>
 * In a real-world application, the {@code isLoggedIn} attribute would typically be determined based on the user's session or authentication status.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see org.springframework.stereotype.Controller
 * @see org.springframework.ui.Model
 * @see org.springframework.web.bind.annotation.GetMapping
 * @since 1.0.0
 */
@Controller
public class MainController {
    @GetMapping({"/home", "/"})
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Current authorities: " + auth.getAuthorities());
        boolean isLoggedIn = true;
        model.addAttribute("isLoggedIn", isLoggedIn);
        return "main/home";
    }

    @GetMapping("/project-description")
    public String projectDescription(Model model) {
        boolean isLoggedIn = false;
        model.addAttribute("isLoggedIn", isLoggedIn);
        return "main/project-description";
    }
}