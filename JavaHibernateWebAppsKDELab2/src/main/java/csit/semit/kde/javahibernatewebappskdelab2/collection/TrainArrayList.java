package csit.semit.kde.javahibernatewebappskdelab2.collection;

import csit.semit.kde.javahibernatewebappskdelab2.entity.Train;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
