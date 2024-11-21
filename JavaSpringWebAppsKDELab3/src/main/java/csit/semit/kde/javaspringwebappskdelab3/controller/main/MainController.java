package csit.semit.kde.javaspringwebappskdelab3.controller.main;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * MainController is a Spring MVC controller that handles requests for the home page and project description page.
 * <p>
 * This controller provides two main endpoints:
 * <ul>
 *     <li>{@code /home} or {@code /} - Displays the home page with a model attribute indicating the user is logged in.</li>
 *     <li>{@code /project-description} - Displays the project description page with a model attribute indicating the user is not logged in.</li>
 * </ul>
 * <p>
 * This controller uses the {@link org.springframework.stereotype.Controller} annotation to indicate that it is a Spring MVC controller.
 * The {@link org.springframework.web.bind.annotation.GetMapping} annotation is used to map HTTP GET requests to handler methods.
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
    public String home(@RequestParam(value = "successMessage", required = false) String successMessage, Model model) {
        if ("employee-added".equals(successMessage)) {
            model.addAttribute("successMessage", "Employee successfully added");
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return "main/home";
    }

    @GetMapping("/project-description")
    public String projectDescription(Model model) {
        return "main/project-description";
    }
}