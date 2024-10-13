package csit.semit.kde.javahibernatewebappskdelab2.listeners;

import csit.semit.kde.javahibernatewebappskdelab2.dao.DAOManager;
import csit.semit.kde.javahibernatewebappskdelab2.service.TrainService;
import csit.semit.kde.javahibernatewebappskdelab2.util.hibernate.HibernateUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppStartupListener implements ServletContextListener {

    private static final Logger logger = LogManager.getLogger(AppStartupListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Initializing application resources...");

        DAOManager daoManager = new DAOManager(HibernateUtil.getSessionFactory(), HibernateUtil.getValidator());
        sce.getServletContext().setAttribute("DAOManager", daoManager);
        TrainService trainService = new TrainService(daoManager);
        sce.getServletContext().setAttribute("TrainService", trainService);

        logger.info("Application resources initialized successfully.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Shutting down application...");

        HibernateUtil.shutdown();

        logger.info("Application resources have been released and shutdown is complete.");
    }
}
