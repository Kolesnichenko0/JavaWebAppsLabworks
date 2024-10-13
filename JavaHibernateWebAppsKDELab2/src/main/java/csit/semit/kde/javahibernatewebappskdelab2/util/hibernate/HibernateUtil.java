package csit.semit.kde.javahibernatewebappskdelab2.util.hibernate;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import java.io.InputStream;
import java.util.Properties;

public class HibernateUtil {
    private static final Logger logger = LogManager.getLogger(HibernateUtil.class);
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    static {
        initializeSessionFactory();
        initializeValidator();
    }

    private static void initializeSessionFactory() {
        try {
            Properties properties = loadHibernateProperties();

            if (registry != null) {
                StandardServiceRegistryBuilder.destroy(registry);
            }

            registry = new StandardServiceRegistryBuilder()
                    .configure()
                    .applySettings(properties)
                    .build();

            MetadataSources sources = new MetadataSources(registry);
            Metadata metadata = sources.getMetadataBuilder().build();

            sessionFactory = metadata.getSessionFactoryBuilder().build();
        } catch (Exception e) {
            logger.error("Failed to initialize Hibernate SessionFactory.", e);
            if (registry != null) {
                StandardServiceRegistryBuilder.destroy(registry);
            }
        }
    }

    private static void initializeValidator() {
        try {
            validatorFactory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();
            validator = validatorFactory.getValidator();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Validator.", e);
        }
    }

    private static Properties loadHibernateProperties() {
        Properties properties = new Properties();
        try (InputStream input = HibernateUtil.class.getClassLoader().getResourceAsStream("hibernate.properties")) {
            if (input == null) {
                throw new RuntimeException("Cannot find file hibernate.properties.");
            } else {
                properties.load(input);
                logger.info("Hibernate properties loaded successfully.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading hibernate.properties.", e);
        }
        return properties;
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            logger.warn("SessionFactory is null, reinitializing.");
            initializeSessionFactory();
        }
        return sessionFactory;
    }

    public static Validator getValidator() {
        if (validator == null) {
            logger.warn("Validator is null, reinitializing.");
            initializeValidator();
        }
        return validator;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        if (validatorFactory != null) {
            validatorFactory.close();
        }
    }
}