package com.test.global.support;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestConfig {
    private static final Logger LOGGER = Logger.getLogger(TestConfig.class.getName());

    private Properties fileConfig;
    private static TestConfig instance;

    private TestConfig() {
        fileConfig = new Properties();
        try {
            fileConfig.load(getClass().getResourceAsStream("/test.config"));
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Failed to load test.config", e);
        }
    }

    private static synchronized TestConfig getInstance() {
        if (instance == null) {
            instance = new TestConfig();
        }
        return instance;
    }

    private String _get(String key) {
        String envKey = key.toUpperCase().replaceAll("\\.", "_");
        String value = System.getenv(envKey);
        if (value == null) {
            value = System.getProperty(key, fileConfig.getProperty(key));
        }
        LOGGER.log(Level.CONFIG, "Requesting property {0}: env={1}, prop={2}, file={3}, value={4}",
                new Object[]{key, System.getenv(envKey), System.getProperty(key), fileConfig.getProperty(key), value});
        return value;
    }

    public static String get(String key) {
        return getInstance()._get(key);
    }
}
