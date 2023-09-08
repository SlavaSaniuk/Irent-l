package me.saniukvyacheslav.logging.conf;

import me.saniukvyacheslav.conf.LoggersConfiguration;
import me.saniukvyacheslav.Logger;
import me.saniukvyacheslav.message.LoggingMessageLevel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoggersConfigurationTests {

    @Test
    void BuilderOfName_nameIsNullEmpty_shouldThrowExc() {
        Assertions.assertThrows(NullPointerException.class, () -> LoggersConfiguration.LoggerConfigurationBuilder.ofName(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> LoggersConfiguration.LoggerConfigurationBuilder.ofName(""));
    }

    @Test
    void isLoggersEnabled_defaultSettings_shouldReturnFalse() {
        LoggersConfiguration configuration = LoggersConfiguration.LoggerConfigurationBuilder.ofName("test1").build();
        Assertions.assertFalse(configuration.isLoggersEnabled());
        configuration.close();
    }

    @Test
    void isLoggersEnabled_enableLoggers_shouldReturnTrue() {
        LoggersConfiguration configuration = LoggersConfiguration.LoggerConfigurationBuilder
                .ofName("test2")
                .enableLoggers(true)
                .build();
        Assertions.assertTrue(configuration.isLoggersEnabled());
        configuration.close();
    }

    @Test
    void enableConsoleOutput_true_shouldPrintMessageToConsole() {
        LoggersConfiguration configuration = LoggersConfiguration.LoggerConfigurationBuilder
                .ofName("test3")
                .enableLoggers(true)
                .enableConsoleOutput(true)
                .build();
        Assertions.assertNotNull(configuration);
        Assertions.assertTrue(configuration.isEnabledConsoleOutput());
        Logger logger = configuration.getLogger("test3");
        logger.tracef("Console output is enabled: [%b];", configuration.isEnabledConsoleOutput());
    }

    @Test
    void enableConsoleOutput_false_shouldNotPrintMessageToConsole() {
        LoggersConfiguration configuration = LoggersConfiguration.LoggerConfigurationBuilder
                .ofName("test4")
                .enableLoggers(true)
                .enableConsoleOutput(false)
                .build();
        Assertions.assertNotNull(configuration);
        Assertions.assertFalse(configuration.isEnabledConsoleOutput());
        Logger logger = configuration.getLogger("test4");
        logger.debugf("Console output is enabled: [%b];", configuration.isEnabledConsoleOutput());
    }

    @Test
    void setLoggingMessagePattern_validPattern_shouldSetPattern() {
        String validPattern = "%TIME% %NAME%: %MSG%";
        LoggersConfiguration configuration = LoggersConfiguration.LoggerConfigurationBuilder
                .ofName("test5")
                .enableLoggers(true)
                .enableConsoleOutput(true)
                .loggingMessagePattern(validPattern)
                .setTimeFormat("HH:mm:ss")
                .build();
        Assertions.assertNotNull(configuration);
        Assertions.assertEquals(validPattern, configuration.getLogMessagePattern());
        Logger logger = configuration.getLogger("test5-logger");
        logger.info("Hello world from test5.");
    }

    @Test
    void useCanonicalLoggerNames_true_shouldUseCanonicalLoggersName() {
        LoggersConfiguration configuration = LoggersConfiguration.LoggerConfigurationBuilder
                .ofName("test6")
                .enableLoggers(true)
                .enableConsoleOutput(true)
                .loggingMessagePattern("%NAME%: %MSG%")
                .useCanonicalLoggersName(true)
                .build();
        Assertions.assertTrue(configuration.isUseCanonicalNames());
        Logger logger = configuration.getLogger(LoggersConfigurationTests.class);
        logger.info("Use canonical loggers names;");
        configuration.close();
    }

    @Test
    void minimalMessageLevel_info_shouldSkipDebugAndTraceLogs() {
        LoggersConfiguration configuration = LoggersConfiguration.LoggerConfigurationBuilder
                .ofName("test7")
                .enableLoggers(true)
                .enableConsoleOutput(true)
                .loggingMessagePattern("[%LEVEL%] %TIME% %NAME%: %MSG%")
                .useCanonicalLoggersName(false)
                .minimalLevelOfMessages(LoggingMessageLevel.INFO)
                .build();
        Logger logger = configuration.getLogger(LoggersConfigurationTests.class);
        logger.trace("Trace message;");
        logger.debug("Debug message;");
        logger.info("Info message;");
        logger.warn("Warn message;");
        logger.error("Error message;");
        logger.fatal("Fatal message;");
        configuration.close();
    }

}
