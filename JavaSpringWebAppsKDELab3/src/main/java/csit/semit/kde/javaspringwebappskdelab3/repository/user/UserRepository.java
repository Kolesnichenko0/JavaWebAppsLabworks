package csit.semit.kde.javaspringwebappskdelab3.repository.user;

import csit.semit.kde.javaspringwebappskdelab3.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repository interface for managing {@link User} entities.
 * <p>
 * This interface extends {@link JpaRepository} and {@link JpaSpecificationExecutor} to provide CRUD operations
 * and specification-based querying capabilities for {@link User} entities.
 * </p>
 * <p>
 * The main functionalities provided by this repository include:
 * <ul>
 *   <li>Finding a user by its username or email.</li>
 *   <li>Checking if a user exists by its username or email.</li>
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
 *     Optional<User> user = userRepository.findByUsernameOrEmail("usernameOrEmail");
 *     boolean exists = userRepository.existsByUsernameOrEmail("usernameOrEmail");
 * }
 * }
 * </pre>
 * </p>
 * <p>
 * Dependencies:
 * <ul>
 *   <li>{@link User}</li>
 *   <li>{@link JpaRepository}</li>
 *   <li>{@link JpaSpecificationExecutor}</li>
 * </ul>
 * </p>
 * <p>
 * Author: Kolesnychenko Denys Yevhenovych CS-222a
 * </p>
 *
 * @see User
 * @see JpaRepository
 * @see JpaSpecificationExecutor
 * @since 1.0.0
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameOrEmail(String username, String email);

    boolean existsByUsernameOrEmail(String username, String email);
}