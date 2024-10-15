package csit.semit.kde.javahibernatewebappskdelab2.dao;

import lombok.Getter;
import org.hibernate.SessionFactory;

import jakarta.validation.Validator;

/**
 * Manager class for Data Access Objects (DAOs).
 * <p>
 * This class serves as a central point for managing various DAOs in the application.
 * It initializes and provides access to specific DAOs, such as `TrainDAO`.
 * </p>
 * <p>
 * The `DAOManager` class includes:
 * <ul>
 *   <li>Initialization of DAOs with necessary dependencies like `SessionFactory` and `Validator`</li>
 *   <li>Getter methods to access the DAOs</li>
 * </ul>
 * </p>
 * <p>
 * This class ensures that all DAOs are properly initialized and ready for use in the application.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see TrainDAO
 * @see SessionFactory
 * @see Validator
 * @since 1.0.0
 */
@Getter
public class DAOManager {
    private TrainDAO trainDAO;

    public DAOManager(SessionFactory sessionFactory, Validator validator) {
        this.trainDAO = new TrainDAO(sessionFactory, validator);
    }

}
