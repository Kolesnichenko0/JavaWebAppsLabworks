package csit.semit.kde.javaspringwebappskdelab3.service.user;

import csit.semit.kde.javaspringwebappskdelab3.dto.train.TrainDTO;
import csit.semit.kde.javaspringwebappskdelab3.dto.user.UserCreateDTO;
import csit.semit.kde.javaspringwebappskdelab3.dto.user.UserDTO;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceResult;

import java.util.List;

/**
 * Service interface for managing user-related operations.
 * <p>
 * This interface defines methods for performing CRUD operations and other business logic
 * related to {@link UserDTO} entities.
 * </p>
 * <p>
 * The main functionalities provided by this service include:
 * <ul>
 *   <li>Finding a user by its ID, username, or email.</li>
 *   <li>Saving and updating user information.</li>
 *   <li>Deleting users individually or in bulk.</li>
 * </ul>
 * </p>
 * <p>
 * Dependencies:
 * <ul>
 *   <li>{@link UserDTO}</li>
 *   <li>{@link ServiceResult}</li>
 * </ul>
 * </p>
 * <p>
 * Author: Kolesnychenko Denys Yevhenovych CS-222a
 * </p>
 *
 * @see UserDTO
 * @see ServiceResult
 * @since 1.0.0
 */
public interface UserService {
    ServiceResult<UserDTO> findByKeySet(String username, String email);

    ServiceResult<UserDTO> findByUsernameOrEmail(String usernameOrEmail);

    ServiceResult<UserDTO> findByUsername(String username);

    ServiceResult<UserDTO> save(UserCreateDTO userCreateDTO);

    ServiceResult<UserDTO> saveAll(List<UserCreateDTO> userCreateDTOs);

    String findPasswordById(Long userId);
}