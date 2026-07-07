package com.app.quantitymeasurement.util;

import com.app.quantitymeasurement.exception.DatabaseException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

public class ApplicationConfig {
    private static final String CONFIG_FILE = "application.properties";

    private final Properties properties = new Properties();
    private final String environment;

    public ApplicationConfig() {
        this(System.getProperty("app.env", System.getenv().getOrDefault("APP_ENV", "dev")));
    }

    public ApplicationConfig(String environment) {
        this.environment = normalize(environment);
        loadProperties();
    }

    public String getEnvironment() {
        return environment;
    }

    public String getRepositoryType() {
        return getProperty("app.repository", "database").toLowerCase(Locale.ROOT);
    }

    public String getDatabaseUrl() {
        return getProperty("database.url", "jdbc:h2:./target/quantity-measurements;MODE=LEGACY;AUTO_SERVER=FALSE");
    }

    public String getDatabaseUsername() {
        return getProperty("database.username", "sa");
    }

    public String getDatabasePassword() {
        return getProperty("database.password", "");
    }

    public int getPoolInitialSize() {
        return getIntProperty("database.pool.initialSize", 2);
    }

    public int getPoolMaxSize() {
        return getIntProperty("database.pool.maxSize", 8);
    }

    public long getPoolTimeoutMillis() {
        return getLongProperty("database.pool.timeoutMillis", 3000L);
    }

    public String getProperty(String key, String defaultValue) {
        String environmentKey = environment + "." + key;
        String value = System.getProperty(environmentKey);
        if (value != null) {
            return value;
        }
        value = System.getProperty(key);
        if (value != null) {
            return value;
        }
        value = properties.getProperty(environmentKey);
        if (value != null) {
            return value;
        }
        return properties.getProperty(key, defaultValue);
    }

    private int getIntProperty(String key, int defaultValue) {
        return Integer.parseInt(getProperty(key, String.valueOf(defaultValue)));
    }

    private long getLongProperty(String key, long defaultValue) {
        return Long.parseLong(getProperty(key, String.valueOf(defaultValue)));
    }

    private void loadProperties() {
        try (InputStream input = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            throw new DatabaseException("Unable to load " + CONFIG_FILE, e);
        }
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return "dev";
        }
        return value.toLowerCase(Locale.ROOT);
    }
}
