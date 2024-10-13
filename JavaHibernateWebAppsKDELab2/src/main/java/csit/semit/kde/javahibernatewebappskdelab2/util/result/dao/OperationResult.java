package csit.semit.kde.javahibernatewebappskdelab2.util.result.dao;

import csit.semit.kde.javahibernatewebappskdelab2.util.result.FieldName;
import csit.semit.kde.javahibernatewebappskdelab2.util.result.Result;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Getter
@ToString
public class OperationResult<E> implements Result<E, OperationStatus> {
    private final OperationStatus status;
    private E entity;
    private List<E> entityList;
    private FieldName fieldName;
    private Set<FieldName> foundFields;

    public OperationResult(OperationStatus status) {
        this.status = status;
    }

//    public OperationResult(OperationStatus status, E entity, FieldName fieldName) {
//        this.status = status;
//        this.entity = entity;
//        this.fieldName = fieldName;
//    }

    public OperationResult(OperationStatus status, E entity,  Set<FieldName> foundFields) {
        this.status = status;
        this.entity = entity;
        this.foundFields = foundFields;
    }

    public OperationResult(OperationStatus status, FieldName fieldName) {
        this.status = status;
        this.fieldName = fieldName;
    }

    public OperationResult(OperationStatus status, E entity) {
        this.status = status;
        this.entity = entity;
    }

    public OperationResult(OperationStatus status, List<E> entityList) {
        this.status = status;
        this.entityList = entityList;
    }
}

