package me.saniukvyacheslav.logging;

import me.saniukvyacheslav.Logger;
import me.saniukvyacheslav.conf.LoggersConfiguration;
import me.saniukvyacheslav.impl.LoggerImpl;
import org.junit.jupiter.api.Test;

public class LoggerTestCase {

    @Test
    void trace_simpleMessage_shouldLogMessage() {
        Logger logger = new LoggerImpl(LoggersConfiguration.LoggerConfigurationBuilder.ofName("LoggerTestCase").enableLoggers(false).build(), "LoggerTestCase");
        logger.trace("Hello world!");
    }
}
