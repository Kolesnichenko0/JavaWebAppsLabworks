package csit.semit.kde.javaspringwebappskdelab3.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling user-related requests.
 * <p>
 * This controller is mapped to the "/users" URL and provides methods for handling requests related to users.
 * </p>
 * <p>
 * The main functionalities provided by this controller include:
 * <ul>
 *   <li>Returning the base path for user-related views.</li>
 *   <li>Returning the entity name for user-related operations.</li>
 *   <li>Handling requests to view the user creation page.</li>
 * </ul>
 * </p>
 * <p>
 * This controller uses Spring MVC annotations to map requests and handle them appropriately.
 * </p>
 * <p>
 * Author: Kolesnychenko Denys Yevhenovych CS-222a
 * </p>
 *
 * @since 1.0.0
 */
@Controller
@RequestMapping("/users")
public class UserControllerEntity {

    @GetMapping("/create")
    public String createUser(Model model) {
        boolean isLoggedIn = true;
        model.addAttribute("isLoggedIn", isLoggedIn);
        return "user/user-create";
    }
}