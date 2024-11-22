package csit.semit.kde.javaspringwebappskdelab3.controller.user;

import csit.semit.kde.javaspringwebappskdelab3.dto.user.UserCreateDTO;
import csit.semit.kde.javaspringwebappskdelab3.dto.user.UserDTO;
import csit.semit.kde.javaspringwebappskdelab3.enums.user.Role;
import csit.semit.kde.javaspringwebappskdelab3.service.user.UserService;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceResult;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceResultHandler;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * REST controller for handling user-related requests.
 * <p>
 * This controller is mapped to the "/api/users" URL and provides methods for handling user creation.
 * </p>
 * <p>
 * The main functionalities provided by this controller include:
 * <ul>
 *   <li>Saving a new user with the role automatically set to USER.</li>
 * </ul>
 * </p>
 * <p>
 * This controller uses Spring MVC annotations to map requests and handle them appropriately.
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * @RestController
 * @RequestMapping("/api/users")
 * public class UserRestController {
 *     // methods and logic
 * }
 * }
 * </pre>
 * </p>
 * <p>
 * Dependencies:
 * <ul>
 *   <li>{@link UserService}</li>
 *   <li>{@link ServiceResult}</li>
 *   <li>{@link ServiceResultHandler}</li>
 *   <li>{@link HttpServletRequest}</li>
 *   <li>{@link HttpHeaders}</li>
 *   <li>{@link ResponseEntity}</li>
 *   <li>{@link ServletUriComponentsBuilder}</li>
 * </ul>
 * </p>
 * <p>
 * Annotations used:
 * <ul>
 *   <li>{@link RestController}</li>
 *   <li>{@link RequestMapping}</li>
 *   <li>{@link PostMapping}</li>
 *   <li>{@link RequestBody}</li>
 * </ul>
 * </p>
 * <p>
 * Note: This controller assumes that the base path for API endpoints is "/api/users".
 * </p>
 * <p>
 * Error handling:
 * <ul>
 *   <li>Handles service result statuses and responds with appropriate HTTP status codes and messages.</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see UserService
 * @see ServiceResult
 * @see ServiceResultHandler
 * @see HttpServletRequest
 * @see HttpHeaders
 * @see ResponseEntity
 * @see ServletUriComponentsBuilder
 * @see RestController
 * @see RequestMapping
 * @see PostMapping
 * @see RequestBody
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody UserCreateDTO userCreateDTO) {
        ServiceResult<UserDTO> result = userService.save(userCreateDTO);
        if (result.getStatus() == ServiceStatus.SUCCESS) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Success-Message", "Resource created successfully");

            return ResponseEntity.status(HttpStatus.CREATED)
                    .headers(headers)
                    .body(result.getEntity());
        }
        return ServiceResultHandler.handleServiceResult(result);
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ServiceResult<UserDTO> result = userService.findByUsername(username);
        return ServiceResultHandler.handleServiceResult(result);
    }
}