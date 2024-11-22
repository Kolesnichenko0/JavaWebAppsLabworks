package csit.semit.kde.javaspringwebappskdelab3.service.user;

import csit.semit.kde.javaspringwebappskdelab3.dto.user.UserDTO;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceResult;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of {@link UserDetailsService} for loading user-specific data.
 * <p>
 * This service provides functionality to load user details by username or email for authentication purposes.
 * It interacts with the {@link UserService} to retrieve user information and constructs a {@link UserDetails} object.
 * </p>
 * <p>
 * The `CustomUserDetailsService` class includes:
 * <ul>
 *   <li>Dependency injection of {@link UserService} to access user data.</li>
 *   <li>Method to load user details by username or email, throwing {@link UsernameNotFoundException} if the user is not found.</li>
 * </ul>
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * @Autowired
 * private CustomUserDetailsService customUserDetailsService;
 *
 * public void someMethod() {
 *     UserDetails userDetails = customUserDetailsService.loadUserByUsername("usernameOrEmail");
 *     // Use the userDetails object
 * }
 * }
 * </pre>
 * </p>
 * <p>
 * Dependencies:
 * <ul>
 *   <li>{@link UserService}</li>
 *   <li>{@link UserDTO}</li>
 *   <li>{@link ServiceResult}</li>
 *   <li>{@link ServiceStatus}</li>
 *   <li>{@link UserDetails}</li>
 *   <li>{@link UsernameNotFoundException}</li>
 *   <li>{@link User}</li>
 * </ul>
 * </p>
 * <p>
 * This class is annotated with {@link Service} to indicate that it's a Spring service component.
 * </p>
 *
 * @author Kolesnychenko
 * Denys Yevhenovych CS-222a
 * @see UserService
 * @see UserDTO
 * @see ServiceResult
 * @see ServiceStatus
 * @see UserDetails
 * @see UsernameNotFoundException
 * @see User
 * @since 1.0.0
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        ServiceResult<UserDTO> result = userService.findByUsernameOrEmail(usernameOrEmail);
        if (result.getStatus() != ServiceStatus.SUCCESS) {
            throw new UsernameNotFoundException("User not found");
        }

        UserDTO userDTO = result.getEntity();
        String password = userService.findPasswordById(userDTO.getId());

//        Role[] roles = switch (userDTO.getRole()) {
//            case ADMIN -> new Role[]{Role.USER, Role.ADMIN};
//            case CASHIER -> new Role[]{Role.USER, Role.CASHIER};
//            case TRAIN_MANAGER -> new Role[]{Role.USER, Role.TRAIN_MANAGER};
//            default -> new Role[]{userDTO.getRole()};
//        };

        return User.builder()
                .username(userDTO.getUsername())
                .password(password)
                .roles(userDTO.getRole().name())
//                .roles(roles.toArray(new String[0]))
                .build();
    }
}