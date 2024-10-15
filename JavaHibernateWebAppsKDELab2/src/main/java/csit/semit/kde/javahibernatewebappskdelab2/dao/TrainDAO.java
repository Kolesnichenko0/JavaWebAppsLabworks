package csit.semit.kde.javahibernatewebappskdelab2.dao;

import csit.semit.kde.javahibernatewebappskdelab2.entity.Train;
import lombok.Getter;
import org.hibernate.SessionFactory;

import jakarta.validation.Validator;

/**
 * Data Access Object (DAO) implementation for managing `Train` entities.
 * <p>
 * This class provides the implementation of the `ITrainDAO` interface, offering methods to interact with the database
 * for CRUD operations, validation, and transaction management specific to `Train` entities.
 * </p>
 * <p>
 * The `TrainDAO` class uses Hibernate for ORM (Object-Relational Mapping) and Jakarta Validation for entity validation.
 * It supports operations such as finding trains by various criteria, filtering, sorting, and inserting sample data.
 * </p>
 * <p>
 * The class includes:
 * <ul>
 *   <li>SessionFactory for managing Hibernate sessions</li>
 *   <li>Validator for validating `Train` entities</li>
 *   <li>Methods for CRUD operations and custom queries</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @see ITrainDAO
 * @see Train
 * @see SessionFactory
 * @see Validator
 * @since 1.0.0
 */
@Getter
public class TrainDAO implements ITrainDAO {
    private final SessionFactory sessionFactory;

    private final Validator validator;

    private static final Class<Train> ENTITY_CLASS = Train.class;

    @Override
    public Class<Train> getEntityClass() {
        return ENTITY_CLASS;
    }

    public TrainDAO(SessionFactory sessionFactory, Validator validator) {
        this.validator = validator;
        this.sessionFactory = sessionFactory;
    }
}
