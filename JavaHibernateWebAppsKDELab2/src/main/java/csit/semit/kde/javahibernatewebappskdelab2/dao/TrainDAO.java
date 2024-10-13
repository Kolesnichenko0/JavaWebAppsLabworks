package csit.semit.kde.javahibernatewebappskdelab2.dao;

import csit.semit.kde.javahibernatewebappskdelab2.entity.Train;
import lombok.Getter;
import org.hibernate.SessionFactory;

import jakarta.validation.Validator;

@Getter
public class TrainDAO implements ITrainDAO{
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
