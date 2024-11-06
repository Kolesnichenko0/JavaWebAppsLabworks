package csit.semit.kde.javaspringwebappskdelab3.repository.train;

import csit.semit.kde.javaspringwebappskdelab3.entity.train.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * Repository interface for managing {@link Train} entities.
 * <p>
 * This interface extends {@link JpaRepository} and {@link JpaSpecificationExecutor} to provide CRUD operations
 * and specification-based querying capabilities for {@link Train} entities.
 * </p>
 * <p>
 * The main functionalities provided by this repository include:
 * <ul>
 *   <li>Finding a train by its number.</li>
 *   <li>Checking if a train exists by its number.</li>
 * </ul>
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * @Autowired
 * private TrainRepository trainRepository;
 *
 * public void someMethod() {
 *     Optional<Train> train = trainRepository.findByNumber("12345");
 *     boolean exists = trainRepository.existsByNumber("12345");
 * }
 * }
 * </pre>
 * </p>
 * <p>
 * Dependencies:
 * <ul>
 *   <li>{@link Train}</li>
 *   <li>{@link JpaRepository}</li>
 *   <li>{@link JpaSpecificationExecutor}</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @see Train
 * @see JpaRepository
 * @see JpaSpecificationExecutor
 * @since 1.0.0
 */
public interface TrainRepository extends JpaRepository<Train, Long>, JpaSpecificationExecutor<Train> {
    Optional<Train> findByNumber(String number);

    boolean existsByNumber(String number);
}