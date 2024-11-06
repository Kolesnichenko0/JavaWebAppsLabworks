package csit.semit.kde.javaspringwebappskdelab3.dto;

/**
 * Interface for entities that can be identified by an ID.
 * <p>
 * This interface defines a contract for entities that have a unique identifier.
 * It provides a method to retrieve the ID of the entity.
 * </p>
 * <p>
 * Implementing this interface allows entities to be easily managed and identified
 * within the application, especially when dealing with data transfer objects (DTOs)
 * and persistence operations.
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * public class SomeEntity implements Identifiable {
 *     private Long id;
 *
 *     @Override
 *     public Long getId() {
 *         return id;
 *     }
 * }
 * }
 * </pre>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @since 1.0.0
 */
public interface Identifiable {
    Long getId();
}