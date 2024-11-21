package csit.semit.kde.javaspringwebappskdelab3.dto.user;

import csit.semit.kde.javaspringwebappskdelab3.enums.user.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Data Transfer Object (DTO) for creating `User` entities.
 * <p>
 * This class is used to transfer data when creating a new user.
 * It encapsulates the details of a user, including its username, email, password, and name.
 * </p>
 * <p>
 * The `UserCreateDTO` class includes:
 * <ul>
 *   <li>Username</li>
 *   <li>Email</li>
 *   <li>Password</li>
 *   <li>Name</li>
 * </ul>
 * </p>
 * <p>
 * This class uses Lombok annotations to generate boilerplate code such as getters, setters, constructors, and `toString` method.
 * </p>
 * <p>
 * Author: Kolesnychenko Denys Yevhenovych CS-222a
 * </p>
 *
 * @see Data
 * @see NoArgsConstructor
 * @see AllArgsConstructor
 * @see NonNull
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDTO {
    @NonNull
    private String username;
    @NonNull
    private String email;
    @NonNull
    private String password;
    @NonNull
    private String name;
    @NonNull
    private Role role;
}