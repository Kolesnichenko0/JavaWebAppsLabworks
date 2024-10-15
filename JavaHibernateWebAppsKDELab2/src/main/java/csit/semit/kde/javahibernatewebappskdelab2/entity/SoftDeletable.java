package csit.semit.kde.javahibernatewebappskdelab2.entity;

/**
 * Interface for entities that support soft deletion.
 * <p>
 * This interface provides methods to mark an entity as deleted or active. Soft deletion is a technique where an entity
 * is not physically removed from the database but is instead marked as deleted, allowing it to be excluded from
 * queries without losing the data.
 * </p>
 * <p>
 * The `SoftDeletable` interface includes:
 * <ul>
 *   <li>Method to set the deletion status of the entity</li>
 *   <li>Method to get the current deletion status of the entity</li>
 * </ul>
 * </p>
 * <p>
 * Implementing this interface allows entities to be managed with a soft deletion status, providing more control over
 * data lifecycle and retention.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @since 1.0.0
 */
public interface SoftDeletable {
    void setIsDeleted(Boolean isDeleted);

    Boolean getIsDeleted();
}
