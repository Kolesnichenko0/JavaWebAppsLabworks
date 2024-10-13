package csit.semit.kde.javahibernatewebappskdelab2.dao;

import csit.semit.kde.javahibernatewebappskdelab2.entity.Train;
import csit.semit.kde.javahibernatewebappskdelab2.util.result.dao.OperationStatus;
import csit.semit.kde.javahibernatewebappskdelab2.util.result.dao.OperationResult;
import csit.semit.kde.javahibernatewebappskdelab2.util.hibernate.HibernateUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TrainDAOInsertSampleTrainsTest {

    private static TrainDAO trainDAO;

    @BeforeAll
    public static void setUp() {
        DAOManager daoManager = new DAOManager(HibernateUtil.getSessionFactory(), HibernateUtil.getValidator());
        assertNotNull(daoManager);
        trainDAO = daoManager.getTrainDAO();
    }

    @AfterAll
    public static void tearDown() {
        HibernateUtil.shutdown();
    }

    @Test
    public void testInsertSampleTrains() {
        OperationResult<Train> truncateResult = trainDAO.truncateTable();
        assertEquals(OperationStatus.SUCCESS, truncateResult.getStatus());

        OperationResult<Train> insertResult = trainDAO.insertSampleTrains();
        assertEquals(OperationStatus.SUCCESS, insertResult.getStatus());
        assertNotNull(insertResult.getEntityList());
        assertFalse(insertResult.getEntityList().isEmpty());
    }
}
