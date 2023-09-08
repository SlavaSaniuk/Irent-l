package me.saniukvyacheslav.conf;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.saniukvyacheslav.annotation.pattern.Singleton;

import java.util.HashMap;
import java.util.Map;

/**
 * Main configuration for all loggers and inner loggers configurations.
 */
@Singleton
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LoggingConfiguration {

    // Constants:
    public static final String DEFAULT_LOG_MESSAGE_PATTERN = "%MSG%";

    private static LoggingConfiguration INSTANCE; // Singleton instance;
    final Map<String, LoggersConfiguration> loggersConfigurations = new HashMap<>(); // All loggers configurations;
    // Configuration parameters:
    @Getter private boolean isLoggingEnabled = true; // "enable/disable logging" flag;

    /**
     * Get current singleton instance if this configuration.
     * @return - singleton instance.
     */
    public static LoggingConfiguration getInstance() {
        if (LoggingConfiguration.INSTANCE == null) LoggingConfiguration.INSTANCE = new LoggingConfiguration();
        return LoggingConfiguration.INSTANCE;
    }

    /**
     * Enable/disable logging.
     * @param isEnable - flag.
     */
    public void enableLogging(boolean isEnable) {
        this.isLoggingEnabled = isEnable;
    }
}
