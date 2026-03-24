package org.pure.java.project;

import org.pure.java.project.config.AppConfiguration;
import org.pure.java.project.config.ApplicationContext;
import org.pure.java.project.ui.ConsoleUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: Artyom Aroyan
 * Date: 15.01.26
 * Time: 23:43:53
 */
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    static void main() {
        try {
            AppConfiguration configuration = AppConfiguration.load();
            ApplicationContext context = ApplicationContext.initialize(configuration);
            ConsoleUI consoleUI = context.consoleUI();
            consoleUI.start();
        } catch (Exception ex) {
            LOGGER.error("Failed to start application", ex);
            System.err.println("Application failed to start: " + ex.getMessage());
        }
    }
}
