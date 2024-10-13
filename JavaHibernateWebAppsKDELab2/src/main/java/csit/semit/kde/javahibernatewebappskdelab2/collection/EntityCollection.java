package csit.semit.kde.javahibernatewebappskdelab2.collection;

public interface EntityCollection<T> {
    int count();
    void add(T item);
    boolean remove(T item);
    void clear();
    int indexOf(T item);
    boolean updateById(Long id, T item);
}
