package csit.semit.kde.javaspringwebappskdelab3.service.user;

import csit.semit.kde.javaspringwebappskdelab3.dto.user.UserCreateDTO;
import csit.semit.kde.javaspringwebappskdelab3.dto.user.UserDTO;
import csit.semit.kde.javaspringwebappskdelab3.entity.user.User;
import csit.semit.kde.javaspringwebappskdelab3.repository.user.UserRepository;
import csit.semit.kde.javaspringwebappskdelab3.util.mapper.user.UserMapper;
import csit.semit.kde.javaspringwebappskdelab3.util.result.entity.FieldValidationException;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceResult;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceStatus;
import csit.semit.kde.javaspringwebappskdelab3.util.fieldname.user.UserFieldName;
import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of {@link UserService} for managing user-related operations.
 * <p>
 * This service provides functionality to manage user data, including finding users by various criteria,
 * saving new users, updating existing users, and deleting users. It interacts with the {@link UserRepository}
 * to perform database operations and uses {@link UserMapper} to convert between entity and DTO objects.
 * </p>
 * <p>
 * The `UserServiceImpl` class includes:
 * <ul>
 *   <li>Dependency injection of {@link UserRepository} and {@link PasswordEncoder}.</li>
 *   <li>Methods to find users by username, email, or both.</li>
 *   <li>Methods to save and update user information, ensuring unique fields are validated.</li>
 *   <li>Methods to delete users individually or in bulk.</li>
 * </ul>
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * @Autowired
 * private UserService userService;
 *
 * public void someMethod() {
 *     UserCreateDTO userCreateDTO = new UserCreateDTO();
 *     // Set userCreateDTO properties
 *     ServiceResult<UserDTO> result = userService.save(userCreateDTO);
 *     // Handle the result
 * }
 * }
 * </pre>
 * </p>
 * <p>
 * Dependencies:
 * <ul>
 *   <li>{@link UserRepository}</li>
 *   <li>{@link PasswordEncoder}</li>
 *   <li>{@link UserMapper}</li>
 *   <li>{@link UserDTO}</li>
 *   <li>{@link UserCreateDTO}</li>
 *   <li>{@link ServiceResult}</li>
 *   <li>{@link ServiceStatus}</li>
 *   <li>{@link FieldValidationException}</li>
 *   <li>{@link ConstraintViolationException}</li>
 *   <li>{@link UserFieldName}</li>
 * </ul>
 * </p>
 * <p>
 * This class is annotated with {@link Service} to indicate that it's a Spring service component.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see UserService
 * @see UserRepository
 * @see PasswordEncoder
 * @see UserMapper
 * @see UserDTO
 * @see UserCreateDTO
 * @see ServiceResult
 * @see ServiceStatus
 * @see FieldValidationException
 * @see ConstraintViolationException
 * @see UserFieldName
 * @since 1.0.0
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ServiceResult<UserDTO> findByKeySet(String username, String email) {
        try {
            if (username != null) {
                User.validateUsername(username);
            }
            if (email != null) {
                User.validateEmail(email);
            }
        } catch (FieldValidationException e) {
            return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, e.getFieldName());
        }
        Optional<User> user = userRepository.findByUsernameOrEmail(username, email);
        return user.map(value -> new ServiceResult<>(ServiceStatus.SUCCESS, UserMapper.toDTO(value)))
                .orElseGet(() -> new ServiceResult<>(ServiceStatus.ENTITY_NOT_FOUND));
    }

    @Override
    public ServiceResult<UserDTO> findByUsernameOrEmail(@NonNull String usernameOrEmail) {
        Optional<User> user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        return user.map(value -> new ServiceResult<>(ServiceStatus.SUCCESS, UserMapper.toDTO(value)))
                .orElseGet(() -> new ServiceResult<>(ServiceStatus.ENTITY_NOT_FOUND));
    }

    private ServiceResult<Void> validateUniqueFields(User user, Long excludeId) {
        ServiceResult<UserDTO> existingUserResultByUsername = findByKeySet(user.getUsername(), null);
        if (existingUserResultByUsername.getStatus() == ServiceStatus.SUCCESS) {
            if (excludeId == null || !existingUserResultByUsername.getEntity().getId().equals(excludeId)) {
                return new ServiceResult<>(ServiceStatus.DUPLICATE_ENTRY, UserFieldName.USERNAME.getRealName());
            }
        } else if (existingUserResultByUsername.getStatus() == ServiceStatus.VALIDATION_ERROR) {
            return new ServiceResult<>(existingUserResultByUsername.getStatus(), existingUserResultByUsername.getFieldName());
        }

        ServiceResult<UserDTO> existingUserResultByEmail = findByKeySet(null, user.getEmail());
        if (existingUserResultByEmail.getStatus() == ServiceStatus.SUCCESS) {
            if (excludeId == null || !existingUserResultByEmail.getEntity().getId().equals(excludeId)) {
                return new ServiceResult<>(ServiceStatus.DUPLICATE_ENTRY, UserFieldName.EMAIL.getRealName());
            }
        } else if (existingUserResultByEmail.getStatus() == ServiceStatus.VALIDATION_ERROR) {
            return new ServiceResult<>(existingUserResultByEmail.getStatus(), existingUserResultByEmail.getFieldName());
        }

        return new ServiceResult<>(ServiceStatus.SUCCESS);
    }

    @Override
    public ServiceResult<UserDTO> findByUsername(String username) {
        try {
            username = User.validateUsername(username);
        } catch (FieldValidationException e) {
            return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, e.getFieldName());
        }
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(value -> new ServiceResult<>(ServiceStatus.SUCCESS, UserMapper.toDTO(value)))
                .orElseGet(() -> new ServiceResult<>(ServiceStatus.ENTITY_NOT_FOUND));
    }

    @Override
    public ServiceResult<UserDTO> save(@NonNull UserCreateDTO userCreateDTO) {
        try {
            User.validatePassword(userCreateDTO.getPassword());
            String hashedPassword = passwordEncoder.encode(userCreateDTO.getPassword());
            User user = new User(
                    userCreateDTO.getUsername(),
                    userCreateDTO.getEmail(),
                    hashedPassword,
                    userCreateDTO.getName(),
                    userCreateDTO.getRole()
            );

            ServiceResult<Void> validationResult = validateUniqueFields(user, null);
            if (validationResult.getStatus() != ServiceStatus.SUCCESS) {
                return new ServiceResult<>(validationResult.getStatus(),
                        validationResult.getFieldName());
            }

            User savedUser = userRepository.save(user);
            return new ServiceResult<>(ServiceStatus.SUCCESS, UserMapper.toDTO(savedUser));
        } catch (FieldValidationException e) {
            return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, e.getFieldName());
        } catch (ConstraintViolationException e) {
            String fieldName = e.getConstraintViolations().iterator().next().getPropertyPath().toString();
            return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, fieldName);
        }
    }

    @Override
    public ServiceResult<UserDTO> saveAll(@NonNull List<UserCreateDTO> userCreateDTOs) {
        try {
            List<String> usernames = userCreateDTOs.stream()
                    .map(UserCreateDTO::getUsername)
                    .toList();
            List<String> emails = userCreateDTOs.stream()
                    .map(UserCreateDTO::getEmail)
                    .toList();

            Set<String> uniqueUsernames = new HashSet<>(usernames);
            Set<String> uniqueEmails = new HashSet<>(emails);

            if (uniqueUsernames.size() < usernames.size()) {
                return new ServiceResult<>(ServiceStatus.DUPLICATE_ENTRY, UserFieldName.USERNAME.getRealName());
            }
            if (uniqueEmails.size() < emails.size()) {
                return new ServiceResult<>(ServiceStatus.DUPLICATE_ENTRY, UserFieldName.EMAIL.getRealName());
            }

            List<User> users = userCreateDTOs.stream()
                    .map(dto -> {
                        User.validatePassword(dto.getPassword());
                        String hashedPassword = passwordEncoder.encode(dto.getPassword());
                        return new User(
                                dto.getUsername(),
                                dto.getEmail(),
                                hashedPassword,
                                dto.getName(),
                                dto.getRole()
                        );
                    })
                    .collect(Collectors.toList());

            for (User user : users) {
                ServiceResult<Void> validationResult = validateUniqueFields(user, null);
                if (validationResult.getStatus() != ServiceStatus.SUCCESS) {
                    return new ServiceResult<>(validationResult.getStatus(), validationResult.getFieldName());
                }
            }

            List<User> savedUsers = userRepository.saveAll(users);
            List<UserDTO> savedUserDTOs = savedUsers.stream()
                    .map(UserMapper::toDTO)
                    .collect(Collectors.toList());

            return new ServiceResult<>(ServiceStatus.SUCCESS, savedUserDTOs);
        } catch (FieldValidationException e) {
            return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, e.getFieldName());
        } catch (ConstraintViolationException e) {
            String fieldName = e.getConstraintViolations().iterator().next().getPropertyPath().toString();
            return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, fieldName);
        }
    }

    @Override
    public String findPasswordById(Long userId) {
        return userRepository.findById(userId)
                .map(User::getPassword)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}