package csit.semit.kde.javahibernatewebappskdelab2.collection;

/**
 * Interface for managing a collection of entities.
 * <p>
 * This interface provides methods for common collection operations such as counting, adding, removing, clearing,
 * finding the index of an item, and updating an item by its ID.
 * </p>
 * <p>
 * The `EntityCollection` interface includes:
 * <ul>
 *   <li>Counting the number of items in the collection</li>
 *   <li>Adding an item to the collection</li>
 *   <li>Removing an item from the collection</li>
 *   <li>Clearing all items from the collection</li>
 *   <li>Finding the index of an item in the collection</li>
 *   <li>Updating an item in the collection by its ID</li>
 * </ul>
 * </p>
 * <p>
 * This interface is generic and can be used with any type of entity.
 * </p>
 *
 * @param <T> the type of the entity
 * @author Kolesnychenko Denys Yevhenovych
 * @since 1.0.0
 */
public interface EntityCollection<T> {
    int count();

    void add(T item);

    boolean remove(T item);

    void clear();

    int indexOf(T item);

    boolean updateById(Long id, T item);
}
