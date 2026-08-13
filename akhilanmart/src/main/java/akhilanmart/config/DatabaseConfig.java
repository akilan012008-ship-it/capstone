package akhilanmart.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConfig {
    private static final Logger LOGGER = Logger.getLogger(DatabaseConfig.class.getName());
    private static HikariDataSource dataSource;

    static {
        initDataSource();
    }

    private static synchronized void initDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            return;
        }
        try {
            Properties props = new Properties();
            InputStream is = DatabaseConfig.class.getClassLoader().getResourceAsStream("application.properties");
            if (is != null) {
                props.load(is);
            }

            HikariConfig config = new HikariConfig();
            config.setDriverClassName(props.getProperty("db.driver", "org.h2.Driver"));
            config.setJdbcUrl(props.getProperty("db.url", "jdbc:h2:./db/akhilanmartdb;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1;MODE=MySQL"));
            config.setUsername(props.getProperty("db.username", "sa"));
            config.setPassword(props.getProperty("db.password", ""));
            
            config.setMaximumPoolSize(Integer.parseInt(props.getProperty("hikari.maximum-pool-size", "10")));
            config.setMinimumIdle(Integer.parseInt(props.getProperty("hikari.minimum-idle", "2")));
            config.setIdleTimeout(Long.parseLong(props.getProperty("hikari.idle-timeout", "30000")));
            config.setConnectionTimeout(Long.parseLong(props.getProperty("hikari.connection-timeout", "10000")));

            dataSource = new HikariDataSource(config);
            LOGGER.info("HikariCP DataSource initialized successfully for Akhilan Mart.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize HikariCP DataSource", e);
            throw new RuntimeException("Database configuration error", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null || dataSource.isClosed()) {
            initDataSource();
        }
        return dataSource.getConnection();
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }

    public static synchronized void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            LOGGER.info("HikariCP Connection Pool closed.");
        }
    }
}
