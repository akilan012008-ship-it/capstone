package akhilanmart.listener;

import akhilanmart.config.DatabaseConfig;
import akhilanmart.util.DatabaseInitializer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.logging.Logger;

@WebListener
public class AppServletContextListener implements ServletContextListener {
    private static final Logger LOGGER = Logger.getLogger(AppServletContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("Akhilan Mart Web Application starting...");
        try {
            DatabaseInitializer.initialize();
        } catch (Exception e) {
            LOGGER.severe("Failed to initialize database during startup: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("Akhilan Mart Web Application shutting down...");
        DatabaseConfig.closePool();
    }
}
