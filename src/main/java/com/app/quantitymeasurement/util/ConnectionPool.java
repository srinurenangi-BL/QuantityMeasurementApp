package com.app.quantitymeasurement.util;

import com.app.quantitymeasurement.exception.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class ConnectionPool {
    private final String jdbcUrl;
    private final String username;
    private final String password;
    private final int maxSize;
    private final long timeoutMillis;
    private final Queue<Connection> availableConnections = new ArrayDeque<>();
    private final Set<Connection> leasedConnections = new HashSet<>();
    private int createdConnections;

    public ConnectionPool(ApplicationConfig config) {
        this(config.getDatabaseUrl(), config.getDatabaseUsername(), config.getDatabasePassword(),
                config.getPoolInitialSize(), config.getPoolMaxSize(), config.getPoolTimeoutMillis());
    }

    public ConnectionPool(String jdbcUrl, String username, String password, int initialSize, int maxSize,
                          long timeoutMillis) {
        if (initialSize < 0) {
            throw new IllegalArgumentException("Initial pool size cannot be negative");
        }
        if (maxSize <= 0 || initialSize > maxSize) {
            throw new IllegalArgumentException("Invalid connection pool size configuration");
        }
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        this.maxSize = maxSize;
        this.timeoutMillis = timeoutMillis;
        initialize(initialSize);
    }

    public synchronized Connection acquireConnection() {
        long deadline = System.currentTimeMillis() + timeoutMillis;
        while (availableConnections.isEmpty() && createdConnections >= maxSize) {
            long waitMillis = deadline - System.currentTimeMillis();
            if (waitMillis <= 0) {
                throw new DatabaseException("Connection pool exhausted");
            }
            try {
                wait(waitMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new DatabaseException("Interrupted while waiting for a database connection", e);
            }
        }

        Connection connection = availableConnections.poll();
        if (connection == null) {
            connection = createConnection();
        }
        leasedConnections.add(connection);
        return connection;
    }

    public synchronized void releaseConnection(Connection connection) {
        if (connection == null || !leasedConnections.remove(connection)) {
            return;
        }
        try {
            if (connection.isClosed()) {
                createdConnections--;
            } else {
                availableConnections.offer(connection);
            }
        } catch (SQLException e) {
            createdConnections--;
            throw new DatabaseException("Unable to inspect database connection state", e);
        } finally {
            notifyAll();
        }
    }

    public synchronized Map<String, Integer> getStatistics() {
        Map<String, Integer> statistics = new LinkedHashMap<>();
        statistics.put("active", leasedConnections.size());
        statistics.put("idle", availableConnections.size());
        statistics.put("total", createdConnections);
        statistics.put("max", maxSize);
        return Collections.unmodifiableMap(statistics);
    }

    public synchronized void closeAll() {
        for (Connection connection : availableConnections) {
            closeQuietly(connection);
        }
        for (Connection connection : leasedConnections) {
            closeQuietly(connection);
        }
        availableConnections.clear();
        leasedConnections.clear();
        createdConnections = 0;
        notifyAll();
    }

    private void initialize(int initialSize) {
        for (int index = 0; index < initialSize; index++) {
            availableConnections.offer(createConnection());
        }
    }

    private Connection createConnection() {
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            createdConnections++;
            return connection;
        } catch (SQLException e) {
            throw new DatabaseException("Unable to create database connection", e);
        }
    }

    private void closeQuietly(Connection connection) {
        try {
            connection.close();
        } catch (SQLException ignored) {
            // best-effort cleanup
        }
    }
}
