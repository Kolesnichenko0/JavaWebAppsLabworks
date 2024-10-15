package csit.semit.kde.javahibernatewebappskdelab2.util.result.dao;

/**
 * Enum representing the status of an operation.
 * <p>
 * This enum is used to encapsulate the various statuses that an operation can have. It includes statuses for successful operations,
 * validation errors, duplicate entries, database errors, entity not found, and other relevant statuses.
 * </p>
 * <p>
 * The `OperationStatus` enum includes:
 * <ul>
 *   <li>{@code SUCCESS} - Indicates a successful operation.</li>
 *   <li>{@code VALIDATION_ERROR} - Indicates a validation error.</li>
 *   <li>{@code DUPLICATE_ENTRY} - Indicates that an object with the same unique field already exists.</li>
 *   <li>{@code DATABASE_ERROR} - Indicates a database-level error.</li>
 *   <li>{@code ENTITY_NOT_FOUND} - Indicates that the entity was not found.</li>
 *   <li>{@code ENTITIES_NOT_FOUND} - Indicates that multiple entities were not found.</li>
 *   <li>{@code UNKNOWN_ERROR} - Indicates an unknown error.</li>
 *   <li>{@code FIELD_NOT_FOUND} - Indicates that the field was not found.</li>
 *   <li>{@code ENTITY_DELETED} - Indicates that the entity was deleted.</li>
 *   <li>{@code ENTITY_ALREADY_DELETED} - Indicates that the entity was already deleted.</li>
 *   <li>{@code ENTITY_ALREADY_ACTIVE} - Indicates that the entity is already active.</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @since 1.0.0
 */
public enum OperationStatus {
    SUCCESS,
    VALIDATION_ERROR,
    DUPLICATE_ENTRY,
    DATABASE_ERROR,
    ENTITY_NOT_FOUND,
    ENTITIES_NOT_FOUND,
    UNKNOWN_ERROR,
    FIELD_NOT_FOUND,
    ENTITY_DELETED,
    ENTITY_ALREADY_DELETED,
    ENTITY_ALREADY_ACTIVE;
}
