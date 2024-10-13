package csit.semit.kde.javahibernatewebappskdelab2.service;

import csit.semit.kde.javahibernatewebappskdelab2.dao.DAOManager;
import csit.semit.kde.javahibernatewebappskdelab2.dao.TrainDAO;
import lombok.Getter;

public class TrainService implements ITrainService{
    @Getter
    private TrainDAO trainDAO;
    public TrainService(DAOManager daoManager) {
        this.trainDAO = daoManager.getTrainDAO();
    }
}
