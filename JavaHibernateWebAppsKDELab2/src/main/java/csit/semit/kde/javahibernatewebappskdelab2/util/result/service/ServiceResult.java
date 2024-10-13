package csit.semit.kde.javahibernatewebappskdelab2.util.result.service;

import csit.semit.kde.javahibernatewebappskdelab2.util.result.FieldName;
import csit.semit.kde.javahibernatewebappskdelab2.util.result.Result;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

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

    public ServiceResult(ServiceStatus status, E entity,  Set<FieldName> foundFields) {
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
