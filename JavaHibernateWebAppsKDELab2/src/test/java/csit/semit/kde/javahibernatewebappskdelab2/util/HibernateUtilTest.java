package csit.semit.kde.javahibernatewebappskdelab2.util;

import csit.semit.kde.javahibernatewebappskdelab2.util.hibernate.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HibernateUtilTest {
    private static SessionFactory sessionFactory;

    /**
     * Initialize the SessionFactory before all tests.
     */
    @BeforeAll
    public static void setUp() {
        sessionFactory = HibernateUtil.getSessionFactory();
        assertNotNull(sessionFactory, "SessionFactory should be initialized and not null.");
    }

    /**
     * Test to verify that the session factory is working by opening and closing a session.
     */
    @Test
    @DisplayName("Test that Hibernate Session is created and opened successfully.")
    void testSessionCreation() {
        try (Session session = sessionFactory.openSession()) {
            assertTrue(session.isOpen(), "Session should be open.");
        } catch (Exception e) {
            fail("Opening session failed with exception: " + e.getMessage());
        }
    }

    /**
     * Shutdown the SessionFactory after all tests.
     */
    @AfterAll
    public static void tearDown() {
        HibernateUtil.shutdown();
        assertFalse(sessionFactory.isOpen(), "SessionFactory should be closed.");
    }
}
