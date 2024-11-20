package csit.semit.kde.javaspringwebappskdelab3.util.mapper.user;

import csit.semit.kde.javaspringwebappskdelab3.dto.user.UserCreateDTO;
import csit.semit.kde.javaspringwebappskdelab3.dto.user.UserDTO;
import csit.semit.kde.javaspringwebappskdelab3.entity.user.User;
import csit.semit.kde.javaspringwebappskdelab3.enums.user.Role;
import lombok.NonNull;

/**
 * Utility class for mapping between {@link User} entities and {@link UserDTO} and {@link UserCreateDTO} data transfer objects.
 * <p>
 * This class provides static methods to convert {@link UserDTO} and {@link UserCreateDTO} objects to {@link User} entities and vice versa.
 * It ensures that the data is correctly mapped between the two representations, facilitating the transfer of data
 * between different layers of the application.
 * </p>
 * <p>
 * The `UserMapper` class includes:
 * <ul>
 *   <li>Conversion of {@link UserDTO} to {@link User} in the {@code toEntity} method</li>
 *   <li>Conversion of {@link UserCreateDTO} to {@link User} in the {@code toEntity} method</li>
 *   <li>Conversion of {@link User} to {@link UserDTO} in the {@code toDTO} method</li>
 * </ul>
 * </p>
 * <p>
 * The class uses Lombok's {@link NonNull} annotation to ensure that the input parameters are not null.
 * </p>
 * <p>
 * Author: Kolesnychenko Denys Yevhenovych CS-222a
 * </p>
 *
 * @see User
 * @see UserDTO
 * @see UserCreateDTO
 * @since 1.0.0
 */
public class UserMapper {
    public static User toEntity(@NonNull UserCreateDTO userCreateDTO, @NonNull Role role) {
        return new User(
                userCreateDTO.getUsername(),
                userCreateDTO.getEmail(),
                userCreateDTO.getPassword(),
                userCreateDTO.getName(),
                role
        );
    }

    public static UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(
                user.getId(),
                user.getRole(),
                user.getUsername(),
                user.getEmail(),
                user.getName()
        );
    }
}