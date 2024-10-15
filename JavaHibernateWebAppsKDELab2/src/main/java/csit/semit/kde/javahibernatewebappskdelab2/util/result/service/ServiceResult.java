package csit.semit.kde.javahibernatewebappskdelab2.util.result.service;

import csit.semit.kde.javahibernatewebappskdelab2.util.result.FieldName;
import csit.semit.kde.javahibernatewebappskdelab2.util.result.Result;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

/**
 * Class representing the result of a service operation, encapsulating the status, entity, and other relevant details.
 * <p>
 * This class is used to encapsulate the result of a service operation, including the status of the operation, the entity involved,
 * a list of entities, and any relevant field names. It provides multiple constructors to initialize the result with different
 * combinations of these attributes.
 * </p>
 * <p>
 * The `ServiceResult` class includes:
 * <ul>
 *   <li>The status of the operation: {@code status}</li>
 *   <li>The entity involved in the operation: {@code entity}</li>
 *   <li>A list of entities involved in the operation: {@code entityList}</li>
 *   <li>The field name associated with the operation: {@code fieldName}</li>
 *   <li>A set of field names found during the operation: {@code foundFields}</li>
 * </ul>
 * </p>
 * <p>
 * This class uses Lombok's {@link Getter} and {@link ToString} annotations to automatically generate getter methods and a
 * string representation of the object.
 * </p>
 *
 * @param <E> the type of the entity involved in the operation
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see FieldName
 * @see Result
 * @since 1.0.0
 */
@Getter
@ToString
public class ServiceResult<E> implements Result<E, ServiceStatus> {
    private final ServiceStatus status;
    private E entity;
    private List<E> entityList;
    private FieldName fieldName;
    private Set<FieldName> foundFields;

    public ServiceResult(ServiceStatus status) {
        this.status = status;
    }

    public ServiceResult(ServiceStatus status, E entity, Set<FieldName> foundFields) {
        this.status = status;
        this.entity = entity;
        this.foundFields = foundFields;
    }

    public ServiceResult(ServiceStatus status, FieldName fieldName) {
        this.status = status;
        this.fieldName = fieldName;
    }

    public ServiceResult(ServiceStatus status, E entity) {
        this.status = status;
        this.entity = entity;
    }

    public ServiceResult(ServiceStatus status, List<E> entityList) {
        this.status = status;
        this.entityList = entityList;
    }
}
