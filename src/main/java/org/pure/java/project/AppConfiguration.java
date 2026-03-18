package org.pure.java.project;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Author: Artyom Aroyan
 * Date: 14.03.26
 * Time: 23:19:03
 */
public class AppConfiguration {
    private final Properties properties;

    public AppConfiguration(Properties properties) {
        this.properties = properties;
    }

    public static AppConfiguration load() {
        Properties prop = new Properties();

        try (InputStream input = AppConfiguration.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (input != null) {
                prop.load(input);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load configuration", ex);
        }

        String envPath = System.getenv("QUESTION_FILE_PATH");
        if (envPath != null && !envPath.isEmpty()) {
            prop.setProperty("questions.file.path", envPath);
        }
        return new AppConfiguration(prop);
    }

    public String getQuestionsFilePath() {
        return properties.getProperty("questions.file.path",
                System.getProperty("user.dir") + "/resources/Question.json");
    }

    public boolean isCacheEnabled() {
        return Boolean.parseBoolean(properties.getProperty("question.cache.enable", "true"));
    }
}