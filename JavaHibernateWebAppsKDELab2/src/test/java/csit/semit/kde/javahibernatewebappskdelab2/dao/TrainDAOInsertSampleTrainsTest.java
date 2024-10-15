package csit.semit.kde.javahibernatewebappskdelab2.dao;

import csit.semit.kde.javahibernatewebappskdelab2.entity.Train;
import csit.semit.kde.javahibernatewebappskdelab2.util.result.dao.OperationStatus;
import csit.semit.kde.javahibernatewebappskdelab2.util.result.dao.OperationResult;
import csit.semit.kde.javahibernatewebappskdelab2.util.hibernate.HibernateUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for inserting sample trains into the database using {@link TrainDAO}.
 * <p>
 * This class contains unit tests for the {@code TrainDAO} class, specifically for the methods that insert sample trains
 * and truncate the train table. It uses JUnit 5 for testing and includes setup and teardown methods to initialize and
 * clean up resources.
 * </p>
 * <p>
 * The `TrainDAOInsertSampleTrainsTest` class includes:
 * <ul>
 *   <li>Setup method to initialize the {@code TrainDAO} instance</li>
 *   <li>Teardown method to shut down Hibernate</li>
 *   <li>Test method to verify the insertion of sample trains</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see TrainDAO
 * @see OperationStatus
 * @see OperationResult
 * @see HibernateUtil
 * @since 1.0.0
 */
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
