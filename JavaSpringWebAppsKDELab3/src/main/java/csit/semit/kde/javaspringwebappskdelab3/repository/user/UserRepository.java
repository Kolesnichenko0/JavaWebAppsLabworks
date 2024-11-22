package csit.semit.kde.javaspringwebappskdelab3.repository.user;

import csit.semit.kde.javaspringwebappskdelab3.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link User} entities.
 * <p>
 * This interface extends {@link JpaRepository} to provide CRUD operations
 * and specification-based querying capabilities for {@link User} entities.
 * </p>
 * <p>
 * The main functionalities provided by this repository include:
 * <ul>
 *   <li>Finding a user by their username.</li>
 *   <li>Finding a user by their username or email.</li>
 *   <li>Checking if a user exists by their username or email.</li>
 * </ul>
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * @Autowired
 * private UserRepository userRepository;
 *
 * public void someMethod() {
 *     Optional<User> user = userRepository.findByUsername("username");
 *     Optional<User> userByEmail = userRepository.findByUsernameOrEmail("username", "email@example.com");
 *     boolean exists = userRepository.existsByUsernameOrEmail("username", "email@example.com");
 * }
 * }
 * </pre>
 * </p>
 * <p>
 * Dependencies:
 * <ul>
 *   <li>{@link User}</li>
 *   <li>{@link JpaRepository}</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see User
 * @see JpaRepository
 * @since 1.0.0
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameOrEmail(String username, String email);

    boolean existsByUsernameOrEmail(String username, String email);
}