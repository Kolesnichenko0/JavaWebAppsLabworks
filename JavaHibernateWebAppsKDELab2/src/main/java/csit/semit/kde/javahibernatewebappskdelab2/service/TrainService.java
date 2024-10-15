package csit.semit.kde.javahibernatewebappskdelab2.service;

import csit.semit.kde.javahibernatewebappskdelab2.dao.DAOManager;
import csit.semit.kde.javahibernatewebappskdelab2.dao.TrainDAO;
import lombok.Getter;

/**
 * Service class for managing train-related operations.
 * <p>
 * This class implements the {@link ITrainService} interface and provides concrete implementations for its methods.
 * It uses a {@link TrainDAO} for data access and a {@link DAOManager} to initialize the DAO.
 * </p>
 * <p>
 * The `TrainService` class includes:
 * <ul>
 *   <li>Initialization of the `TrainDAO` using the `DAOManager`</li>
 *   <li>Implementation of train-related service methods defined in the `ITrainService` interface</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @see ITrainService
 * @see TrainDAO
 * @see DAOManager
 * @since 1.0.0
 */
public class TrainService implements ITrainService {
    @Getter
    private TrainDAO trainDAO;

    public TrainService(DAOManager daoManager) {
        this.trainDAO = daoManager.getTrainDAO();
    }
}
