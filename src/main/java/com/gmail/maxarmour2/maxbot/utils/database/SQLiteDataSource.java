package com.gmail.maxarmour2.maxbot.utils.database;

import com.gmail.maxarmour2.maxbot.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDataSource {

    public static final Logger LOGGER = LoggerFactory.getLogger(SQLiteDataSource.class);
    public static final HikariConfig config = new HikariConfig();
    public static final HikariDataSource source;

    static {
        try {
            final File dbFile = new File("database.db");

            if (!dbFile.exists()) {
                if (dbFile.createNewFile()) {
                    LOGGER.info("Created new database file");
                } else {
                    LOGGER.info("Unable to create a new database file");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        config.setJdbcUrl("jdbc:sqlite:database.db");
        config.setConnectionTestQuery("SELECT 1");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        source = new HikariDataSource(config);
        
        try (final Statement statement = getConnection().createStatement()) {
            final String defaultPrefix = Config.get("PREFIX");

            statement.execute("CREATE TABLE IF NOT EXISTS guild_settings (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "guild_id VARCHAR(20) NOT NULL," +
                    "prefix VARCHAR(255) NOT NULL DEFAULT '" + defaultPrefix + "'" +
                    ");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public SQLiteDataSource() { }

    public static Connection getConnection() throws SQLException {
        return source.getConnection();
    }
}
