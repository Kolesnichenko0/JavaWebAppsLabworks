package csit.semit.kde.javahibernatewebappskdelab2.collection;

import csit.semit.kde.javahibernatewebappskdelab2.entity.Train;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the `EntityCollection` interface for managing a collection of `Train` entities.
 * <p>
 * This class provides methods for common collection operations such as counting, adding, removing, clearing,
 * finding the index of an item, and updating an item by its ID.
 * </p>
 * <p>
 * The `TrainArrayList` class includes:
 * <ul>
 *   <li>Counting the number of trains in the collection</li>
 *   <li>Adding a train to the collection</li>
 *   <li>Removing a train from the collection</li>
 *   <li>Clearing all trains from the collection</li>
 *   <li>Finding the index of a train in the collection</li>
 *   <li>Updating a train in the collection by its ID</li>
 *   <li>Generating a string representation of the collection</li>
 * </ul>
 * </p>
 * <p>
 * This class uses an `ArrayList` to store the `Train` entities and provides methods to manipulate the list.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @see EntityCollection
 * @see Train
 * @since 1.0.0
 */
public class TrainArrayList implements EntityCollection<Train> {
    private final List<Train> list = new ArrayList<>();

    public int count() {
        return list.size();
    }

    public void clear() {
        list.clear();
    }

    public void add(Train train) {
        list.add(train);
    }

    public boolean remove(Train train) {
        return list.remove(train);
    }

    public int indexOf(Train train) {
        return list.indexOf(train);
    }

    public void update(int index, Train updatedTrain) {
        if (index >= 0 && index < list.size()) {
            list.set(index, updatedTrain);
        } else {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
    }

    public boolean updateById(Long id, Train updatedTrain) {
        for (int i = 0; i < list.size(); i++) {
            Train existingTrain = list.get(i);
            if (existingTrain.getId().equals(id)) {
                list.set(i, updatedTrain);
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return "Trains:\n" + list.stream()
                .map(Train::toString)
                .collect(Collectors.joining("\n"));
    }

}
