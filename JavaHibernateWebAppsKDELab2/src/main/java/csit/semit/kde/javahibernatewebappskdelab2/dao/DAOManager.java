package csit.semit.kde.javahibernatewebappskdelab2.dao;

import lombok.Getter;
import org.hibernate.SessionFactory;

import jakarta.validation.Validator;

@Getter
public class DAOManager {
    private TrainDAO trainDAO;

    public DAOManager(SessionFactory sessionFactory, Validator validator) {
        this.trainDAO = new TrainDAO(sessionFactory, validator);
    }

}
