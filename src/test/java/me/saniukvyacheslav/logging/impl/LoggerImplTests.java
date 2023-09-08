package me.saniukvyacheslav.logging.impl;

import me.saniukvyacheslav.conf.LoggersConfiguration;
import me.saniukvyacheslav.impl.LoggerImpl;
import org.junit.jupiter.api.Test;

public class LoggerImplTests {

    @Test
    void level_shouldPrintMessagesWithLevel() {
        LoggersConfiguration configuration = LoggersConfiguration.LoggerConfigurationBuilder.ofName("level")
                .enableLoggers(true)
                .enableConsoleOutput(true)
                .loggingMessagePattern("%TIME% [%LEVEL%] %NAME%: %MSG%")
                .build();
        LoggerImpl logger = (LoggerImpl) configuration.getLogger(LoggerImplTests.class);
        logger.trace("Trace message!");
        logger.debug("Debug message!");
        logger.info("Info message!");
        logger.warn("Warn message!");
        logger.error("Error message!");
        logger.fatal("Fatal message!");

        configuration.close();
    }
}
