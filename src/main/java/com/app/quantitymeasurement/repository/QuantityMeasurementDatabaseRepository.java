package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.exception.DatabaseException;
import com.app.quantitymeasurement.util.ApplicationConfig;
import com.app.quantitymeasurement.util.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class QuantityMeasurementDatabaseRepository implements IQuantityMeasurementRepository {
    private static final String SELECT_COLUMNS = """
            SELECT first_value, second_value, first_unit, second_unit, measurement_type,
                   operation, result, success, error_message, created_at
            FROM quantity_measurements
            """;

    private final ConnectionPool connectionPool;

    public QuantityMeasurementDatabaseRepository(ApplicationConfig config) {
        this(new ConnectionPool(config));
    }

    public QuantityMeasurementDatabaseRepository(String jdbcUrl, String username, String password) {
        this(new ConnectionPool(jdbcUrl, username, password, 2, 8, 3000));
    }

    public QuantityMeasurementDatabaseRepository(String jdbcUrl, String username, String password,
                                                 int initialPoolSize, int maxPoolSize, long timeoutMillis) {
        this(new ConnectionPool(jdbcUrl, username, password, initialPoolSize, maxPoolSize, timeoutMillis));
    }

    public QuantityMeasurementDatabaseRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        initializeSchema();
    }

    @Override
    public void save(QuantityMeasurementEntity entity) {
        Connection connection = connectionPool.acquireConnection();
        boolean originalAutoCommit = true;
        try {
            originalAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            long measurementId = insertMeasurement(connection, entity);
            insertHistory(connection, measurementId, entity);
            connection.commit();
        } catch (SQLException e) {
            rollback(connection);
            throw new DatabaseException("Failed to save measurement", e);
        } finally {
            restoreAutoCommit(connection, originalAutoCommit);
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public List<QuantityMeasurementEntity> findAll() {
        return query(SELECT_COLUMNS + " ORDER BY id", statement -> {
        });
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByOperation(String operation) {
        return query(SELECT_COLUMNS + " WHERE LOWER(operation) = ? ORDER BY id",
                statement -> statement.setString(1, normalize(operation)));
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByType(String measurementType) {
        return query(SELECT_COLUMNS + " WHERE LOWER(measurement_type) = ? ORDER BY id",
                statement -> statement.setString(1, normalize(measurementType)));
    }

    @Override
    public int getTotalCount() {
        String sql = "SELECT COUNT(*) FROM quantity_measurements";
        Connection connection = connectionPool.acquireConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getInt(1) : 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to count measurements", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public void deleteAllMeasurements() {
        Connection connection = connectionPool.acquireConnection();
        boolean originalAutoCommit = true;
        try (PreparedStatement deleteHistory = connection.prepareStatement("DELETE FROM quantity_measurement_history");
             PreparedStatement deleteMeasurements = connection.prepareStatement("DELETE FROM quantity_measurements")) {
            originalAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            deleteHistory.executeUpdate();
            deleteMeasurements.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            rollback(connection);
            throw new DatabaseException("Failed to delete measurements", e);
        } finally {
            restoreAutoCommit(connection, originalAutoCommit);
            connectionPool.releaseConnection(connection);
        }
    }

    public void deleteAll() {
        deleteAllMeasurements();
    }

    @Override
    public Map<String, Integer> getPoolStatistics() {
        return connectionPool.getStatistics();
    }

    @Override
    public void releaseResources() {
        connectionPool.closeAll();
    }

    private long insertMeasurement(Connection connection, QuantityMeasurementEntity entity) throws SQLException {
        String sql = """
                INSERT INTO quantity_measurements
                (first_value, second_value, first_unit, second_unit, measurement_type,
                 operation, result, success, error_message, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bindEntity(statement, entity);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
            }
        }
        throw new SQLException("Database did not return a generated measurement id");
    }

    private void insertHistory(Connection connection, long measurementId, QuantityMeasurementEntity entity)
            throws SQLException {
        String sql = """
                INSERT INTO quantity_measurement_history
                (measurement_id, operation, status, detail, created_at)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, measurementId);
            statement.setString(2, entity.getOperation());
            statement.setString(3, entity.isSuccess() ? "SUCCESS" : "FAILED");
            statement.setString(4, entity.getResult());
            statement.setTimestamp(5, Timestamp.valueOf(entity.getCreatedAt()));
            statement.executeUpdate();
        }
    }

    private void bindEntity(PreparedStatement statement, QuantityMeasurementEntity entity) throws SQLException {
        statement.setDouble(1, entity.getFirstValue());
        statement.setDouble(2, entity.getSecondValue());
        statement.setString(3, entity.getFirstUnit());
        statement.setString(4, entity.getSecondUnit());
        statement.setString(5, entity.getMeasurementType());
        statement.setString(6, entity.getOperation());
        statement.setString(7, entity.getResult());
        statement.setBoolean(8, entity.isSuccess());
        statement.setString(9, entity.getErrorMessage());
        statement.setTimestamp(10, Timestamp.valueOf(entity.getCreatedAt()));
    }

    private List<QuantityMeasurementEntity> query(String sql, StatementBinder binder) {
        List<QuantityMeasurementEntity> entities = new ArrayList<>();
        Connection connection = connectionPool.acquireConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            binder.bind(statement);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    entities.add(mapEntity(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to query measurements", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        return entities;
    }

    private QuantityMeasurementEntity mapEntity(ResultSet resultSet) throws SQLException {
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        LocalDateTime createdAtValue = createdAt == null ? LocalDateTime.now() : createdAt.toLocalDateTime();
        return new QuantityMeasurementEntity(
                resultSet.getDouble("first_value"),
                resultSet.getDouble("second_value"),
                resultSet.getString("first_unit"),
                resultSet.getString("second_unit"),
                resultSet.getString("measurement_type"),
                resultSet.getString("operation"),
                resultSet.getString("result"),
                resultSet.getBoolean("success"),
                resultSet.getString("error_message"),
                createdAtValue
        );
    }

    private void initializeSchema() {
        Connection connection = connectionPool.acquireConnection();
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS quantity_measurements (
                        id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                        first_value DOUBLE PRECISION NOT NULL,
                        second_value DOUBLE PRECISION NOT NULL,
                        first_unit VARCHAR(50) NOT NULL,
                        second_unit VARCHAR(50),
                        measurement_type VARCHAR(50) NOT NULL DEFAULT 'unknown',
                        operation VARCHAR(50) NOT NULL,
                        result VARCHAR(100),
                        success BOOLEAN NOT NULL,
                        error_message VARCHAR(255),
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                    )
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS quantity_measurement_history (
                        id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                        measurement_id BIGINT NOT NULL,
                        operation VARCHAR(50) NOT NULL,
                        status VARCHAR(20) NOT NULL,
                        detail VARCHAR(255),
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        CONSTRAINT fk_quantity_measurement_history_measurement
                            FOREIGN KEY (measurement_id)
                            REFERENCES quantity_measurements(id)
                            ON DELETE CASCADE
                    )
                    """);
            statement.executeUpdate("""
                    ALTER TABLE quantity_measurements
                    ADD COLUMN IF NOT EXISTS measurement_type VARCHAR(50) NOT NULL DEFAULT 'unknown'
                    """);
            statement.executeUpdate("""
                    ALTER TABLE quantity_measurements
                    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                    """);
            statement.executeUpdate("""
                    CREATE INDEX IF NOT EXISTS idx_quantity_measurements_operation
                    ON quantity_measurements(operation)
                    """);
            statement.executeUpdate("""
                    CREATE INDEX IF NOT EXISTS idx_quantity_measurements_type
                    ON quantity_measurements(measurement_type)
                    """);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to initialize database schema", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    private void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException ignored) {
            // original database exception carries the useful context
        }
    }

    private void restoreAutoCommit(Connection connection, boolean originalAutoCommit) {
        try {
            connection.setAutoCommit(originalAutoCommit);
        } catch (SQLException ignored) {
            // connection is about to return to the pool; failing restore is non-actionable here
        }
    }

    private String normalize(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT);
    }

    @FunctionalInterface
    private interface StatementBinder {
        void bind(PreparedStatement statement) throws SQLException;
    }
}
