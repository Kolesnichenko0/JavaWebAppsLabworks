package csit.semit.kde.javahibernatewebappskdelab2.util.result;

import java.util.List;

public interface Result<E, S> {
    S getStatus();
    E getEntity();
    List<E> getEntityList();
    FieldName getFieldName();
    default boolean hasEntity() {
        return getEntity() != null;
    }
    default boolean hasEntityList() {
        return getEntityList() != null && !getEntityList().isEmpty();
    }
}
