package csit.semit.kde.javaspringwebappskdelab3.dto.user;

import csit.semit.kde.javaspringwebappskdelab3.dto.Identifiable;
import csit.semit.kde.javaspringwebappskdelab3.enums.user.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Data Transfer Object (DTO) for `User` entities.
 * <p>
 * This class is used to transfer data between different layers of the application.
 * It encapsulates the details of a user, including its ID, username, email, name, and role.
 * </p>
 * <p>
 * The `UserDTO` class includes:
 * <ul>
 *   <li>ID of the user</li>
 *   <li>Username</li>
 *   <li>Email</li>
 *   <li>Name</li>
 *   <li>Role (e.g., USER, ADMIN)</li>
 * </ul>
 * </p>
 * <p>
 * This class uses Lombok annotations to generate boilerplate code such as getters, setters, constructors, and `toString` method.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see Role
 * @see Data
 * @see NoArgsConstructor
 * @see AllArgsConstructor
 * @see NonNull
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Identifiable {
    private Long id;
    private Role role;
    @NonNull
    private String username;
    @NonNull
    private String email;
    @NonNull
    private String name;
}