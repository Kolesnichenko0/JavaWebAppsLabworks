package csit.semit.kde.javaspringwebappskdelab3.util.result.service;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Class representing the result of a service operation, encapsulating the status, entity, and other relevant details.
 * <p>
 * This class is used to define the structure of a result object, including the status of the operation, the entity involved,
 * a list of entities, and any relevant field names. It provides methods to access these attributes and default methods to check
 * the presence of an entity or a list of entities.
 * </p>
 * <p>
 * The `ServiceResult` class includes:
 * <ul>
 *   <li>The status of the operation: {@link #getStatus()}</li>
 *   <li>The entity involved in the operation: {@link #getEntity()}</li>
 *   <li>A list of entities involved in the operation: {@link #getEntityList()}</li>
 *   <li>The field name associated with the operation: {@link #getFieldName()}</li>
 * </ul>
 * </p>
 * <p>
 * This class also provides default methods:
 * <ul>
 *   <li>{@link #hasEntity()} - Checks if an entity is present.</li>
 *   <li>{@link #hasEntityList()} - Checks if a list of entities is present and not empty.</li>
 * </ul>
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * ServiceResult<MyEntity> result = new ServiceResult<>(ServiceStatus.SUCCESS, myEntity);
 * if (result.hasEntity()) {
 *     MyEntity entity = result.getEntity();
 *     // Process the entity
 * }
 * }
 * </pre>
 * </p>
 *
 * @param <E> the type of the entity involved in the operation
 * @author Kolesnychenko Denys Yevhenovych
 * @since 1.0.0
 */
@Getter
@ToString
public class ServiceResult<E> implements Result<E, ServiceStatus> {
    private final ServiceStatus status;
    private E entity;
    private List<E> entityList;
    private String fieldName;

    public ServiceResult(ServiceStatus status) {
        this.status = status;
    }

    public ServiceResult(ServiceStatus status, E entity) {
        this.status = status;
        this.entity = entity;
    }

    public ServiceResult(ServiceStatus status, List<E> entityList) {
        this.status = status;
        this.entityList = entityList;
    }

    public ServiceResult(ServiceStatus status, String fieldName) {
        this.status = status;
        this.fieldName = fieldName;
    }
}