package csit.semit.kde.javahibernatewebappskdelab2.util;

import csit.semit.kde.javahibernatewebappskdelab2.util.hibernate.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the HibernateUtil.
 * <p>
 * This class contains unit tests for the {@code HibernateUtil} class, which provides utility methods
 * for managing Hibernate sessions and session factories. It uses JUnit 5 for testing and includes
 * setup and teardown methods to initialize and clean up the necessary components.
 * </p>
 * <p>
 * The `HibernateUtilTest` class includes:
 * <ul>
 *   <li>Tests for initializing the SessionFactory</li>
 *   <li>Tests for creating and opening a Hibernate session</li>
 *   <li>Tests for shutting down the SessionFactory</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see HibernateUtil
 * @see SessionFactory
 * @see Session
 * @since 1.0.0
 */
public class HibernateUtilTest {
    private static SessionFactory sessionFactory;

    @BeforeAll
    public static void setUp() {
        sessionFactory = HibernateUtil.getSessionFactory();
        assertNotNull(sessionFactory, "SessionFactory should be initialized and not null.");
    }

    @Test
    @DisplayName("Test that Hibernate Session is created and opened successfully.")
    void testSessionCreation() {
        try (Session session = sessionFactory.openSession()) {
            assertTrue(session.isOpen(), "Session should be open.");
        } catch (Exception e) {
            fail("Opening session failed with exception: " + e.getMessage());
        }
    }

    @AfterAll
    public static void tearDown() {
        HibernateUtil.shutdown();
        assertFalse(sessionFactory.isOpen(), "SessionFactory should be closed.");
    }
}
