package csit.semit.kde.javaspringwebappskdelab3.controller.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

/**
 * GlobalErrorController is a Spring MVC controller that handles error responses.
 * <p>
 * This controller is mapped to handle general errors and specific error codes.
 * It provides methods to handle errors by returning appropriate error messages
 * and status codes to the client.
 * </p>
 * <p>
 * The controller uses the {@link ErrorController} interface to handle errors
 * and provides two main request mappings:
 * <ul>
 *     <li>{@code /error} - Handles general errors and determines the status code from the request.</li>
 *     <li>{@code /error/{errorCode}} - Handles specific error codes provided in the URL path.</li>
 * </ul>
 * </p>
 * <p>
 * The error messages are determined based on the status code and are added to the model
 * to be displayed on the error page.
 * </p>
 * <p>
 * This controller is annotated with {@link Controller} to indicate that it is a Spring MVC controller.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see ErrorController
 * @see HttpServletRequest
 * @see Model
 * @since 1.0.0
 */
@Controller
public class GlobalErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int statusCode = (status != null) ? Integer.parseInt(status.toString()) : 500;
        return prepareErrorResponse(statusCode, model);
    }

    @RequestMapping("/error/{errorCode}")
    public String handleSpecificError(@PathVariable int errorCode, Model model) {
        return prepareErrorResponse(errorCode, model);
    }

    private String prepareErrorResponse(int statusCode, Model model) {
        String message = switch (statusCode) {
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Page Not Found";
            case 500 -> "Internal Server Error";
            default -> "Unknown Error";
        };

        model.addAttribute("statusCode", statusCode);
        model.addAttribute("message", message);

        return "error/general-error";
    }
}