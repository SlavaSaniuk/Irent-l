package me.saniukvyacheslav.extension;

/**
 * Logger extension.
 * This extension define methods for logging formatted messages (formatted via String#format).
 */
public interface FormattedLoggingExt {

    /**
     * Log formatted message (String#format) with "TRACE" logging level.
     * @param aMessage - message to log.
     * @param args - formatting arguments.
     */
    void tracef(String aMessage, Object... args);

    /**
     * Log formatted message (String#format) with "DEBUG" logging level.
     * @param aMessage - message to log.
     * @param args - formatting arguments.
     */
    void debugf(String aMessage, Object... args);

    /**
     * Log formatted message (String#format) with "INFO" logging level.
     * @param aMessage - message to log.
     * @param args - formatting arguments.
     */
    void infof(String aMessage, Object... args);

    /**
     * Log formatted message (String#format) with "WARN" logging level.
     * @param aMessage - message to log.
     * @param args - formatting arguments.
     */
    void warnf(String aMessage, Object... args);

    /**
     * Log formatted message (String#format) with "ERROR" logging level.
     * @param aMessage - message to log.
     * @param args - formatting arguments.
     */
    void errorf(String aMessage, Object... args);

    /**
     * Log formatted message (String#format) with "FATAL" logging level.
     * @param aMessage - message to log.
     * @param args - formatting arguments.
     */
    void fatalf(String aMessage, Object... args);

}
