package com.hasanatmomin.mcpvp.storage;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDatabase implements Database {
    private final JavaPlugin plugin;
    private final String filePath;
    private Connection connection;

    public SQLiteDatabase(JavaPlugin plugin, String filePath) {
        this.plugin = plugin;
        this.filePath = filePath;
    }

    @Override
    public void initialize() {
        try {
            File file = new File(filePath);
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS player_profiles (
                        uuid TEXT PRIMARY KEY,
                        name TEXT NOT NULL,
                        rating INTEGER NOT NULL,
                        wins INTEGER NOT NULL,
                        losses INTEGER NOT NULL,
                        draws INTEGER NOT NULL
                    )
                """);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize SQLite database", e);
        }
    }

    @Override
    public Connection getConnection() {
        if (connection == null) {
            throw new IllegalStateException("Database not initialized");
        }
        return connection;
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("Failed to close database: " + e.getMessage());
        }
    }
}
