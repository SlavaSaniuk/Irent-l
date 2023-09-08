package me.saniukvyacheslav;

import me.saniukvyacheslav.extension.FormattedLoggingExt;

/**
 * Main interface which define methods to log messages.
 */
public interface Logger extends FormattedLoggingExt {

    /**
     * Log message with "TRACE" logging level.
     * @param aMessage - message to log.
     */
    void trace(String aMessage);

    /**
     * Log message with "DEBUG" logging level.
     * @param aMessage - message to log.
     */
    void debug(String aMessage);

    /**
     * Log message with "INFO" logging level.
     * @param aMessage - message to log.
     */
    void info(String aMessage);

    /**
     * Log message with "WARN" logging level.
     * @param aMessage - message to log.
     */
    void warn(String aMessage);

    /**
     * Log message with "ERROR" logging level.
     * @param aMessage - message to log.
     */
    void error(String aMessage);

    /**
     * Log message with "FATAL" logging level.
     * @param aMessage - message to log.
     */
    void fatal(String aMessage);

}
