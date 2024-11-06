package csit.semit.kde.javaspringwebappskdelab3.util.result.service;

import java.util.List;

/**
 * Interface representing the result of an operation, encapsulating the status, entity, and other relevant details.
 * <p>
 * This interface is used to define the structure of a result object, including the status of the operation, the entity involved,
 * a list of entities, and any relevant field names. It provides methods to access these attributes and default methods to check
 * the presence of an entity or a list of entities.
 * </p>
 * <p>
 * The `Result` interface includes:
 * <ul>
 *   <li>The status of the operation: {@code getStatus()}</li>
 *   <li>The entity involved in the operation: {@code getEntity()}</li>
 *   <li>A list of entities involved in the operation: {@code getEntityList()}</li>
 *   <li>The field name associated with the operation: {@code getFieldName()}</li>
 * </ul>
 * </p>
 * <p>
 * This interface also provides default methods:
 * <ul>
 *   <li>{@code hasEntity()} - Checks if an entity is present.</li>
 *   <li>{@code hasEntityList()} - Checks if a list of entities is present and not empty.</li>
 * </ul>
 * </p>
 *
 * @param <E> the type of the entity involved in the operation
 * @param <S> the type of the status of the operation
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @since 1.0.0
 */
public interface Result<E, S> {
    S getStatus();

    E getEntity();

    List<E> getEntityList();

    String getFieldName();

    default boolean hasEntity() {
        return getEntity() != null;
    }

    default boolean hasEntityList() {
        return getEntityList() != null && !getEntityList().isEmpty();
    }
}
