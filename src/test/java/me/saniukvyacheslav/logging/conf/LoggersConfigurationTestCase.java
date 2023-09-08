package me.saniukvyacheslav.logging.conf;

import me.saniukvyacheslav.Logger;
import me.saniukvyacheslav.conf.LoggersConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class LoggersConfigurationTestCase {

    private static LoggersConfiguration testConfiguration;
    private static Logger LOGGER;

    @BeforeAll
    static void beforeAll() {
       testConfiguration = LoggersConfiguration.LoggerConfigurationBuilder
                .ofName("logger-configuration-test-case")
                .enableLoggers(true)
                .build();
       LOGGER = testConfiguration.getLogger(LoggersConfigurationTestCase.class);
    }

    @Test
    void isLoggersEnabled_enabled_shouldReturnTrue() {
        Assertions.assertTrue(testConfiguration.isLoggersEnabled());
        LOGGER.info(String.format("Is loggers enabled: [%b];", testConfiguration.isLoggersEnabled()));
    }

    @Test
    void getLogger_newName_shouldReturnLogger() {
        Logger logger = testConfiguration.getLogger("NewName");
        Assertions.assertNotNull(logger);
        logger.warn("New name logger");
    }

}
