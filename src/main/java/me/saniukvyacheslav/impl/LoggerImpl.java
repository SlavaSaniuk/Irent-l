package me.saniukvyacheslav.impl;

import me.saniukvyacheslav.Logger;
import me.saniukvyacheslav.conf.LoggersConfiguration;
import me.saniukvyacheslav.conf.LoggingConfiguration;
import me.saniukvyacheslav.message.LoggingMessageLevel;
import me.saniukvyacheslav.message.PatternModifier;
import me.saniukvyacheslav.util.string.RegexUtils;
import me.saniukvyacheslav.util.string.StringUtils;

import java.util.Objects;

/**
 * Implementation of {@link Logger} logger.
 */
public class LoggerImpl implements Logger {

    private final LoggersConfiguration loggersConfiguration; // Link on loggers configuration instance;
    private final String loggerName; // This logger name;
    /**
     * Construct new logger instance with name for specified configuration.
     * @param aConfiguration - loggers configuration.
     * @param aName - logger name.
     */
    public LoggerImpl(LoggersConfiguration aConfiguration, String aName) {
        Objects.requireNonNull(aConfiguration, "LoggerConfiguration [aConfiguration] must be not null");
        StringUtils.checkString(aName, "aName");
        this.loggersConfiguration = aConfiguration;
        this.loggerName = aName;
    }

    /**
     * Check whether is log disabled.
     * Check if whole logging is enabled ({@link LoggingConfiguration#isLoggingEnabled()}), then check if loggers are enabled in LoggersConfiguration.
     * @param aLevel - message level.
     * @return - true, if log is disabled.
     */
    private boolean isLogDisabled(LoggingMessageLevel aLevel) {
        if (!LoggingConfiguration.getInstance().isLoggingEnabled()) return true;
        if (!this.loggersConfiguration.isLoggersEnabled()) return true;
        return aLevel.getLevel() < this.loggersConfiguration.getMinimalLevelOfMessages().getLevel();
    }

    /**
     * Out message to all available LoggersOutput outputs.
     * @param aMessage - message to out.
     */
    private void out(String aMessage) {
        this.loggersConfiguration.getLoggersOutputs().forEach((loggerOutput) -> loggerOutput.out(aMessage));
    }

    private String modifyByModifiers(Object... aModifierArguments) {
        String usedPattern = this.loggersConfiguration.getLogMessagePattern();
        // Modify be modifiers:
        for (PatternModifier modifier : this.loggersConfiguration.getModifiers()) {
            usedPattern = modifier.modify(usedPattern, aModifierArguments);
        }

        return usedPattern;
    }

    private String formLogMessage(String anOrigMessage, Object... aModifierArguments) {
        String messageServicePart = this.modifyByModifiers(aModifierArguments);
        return Objects.requireNonNull(RegexUtils.match(messageServicePart, "%MSG%")).replaceFirst(anOrigMessage);
    }

    /**
     * Log message with "TRACE" logging level.
     * @param aMessage - message to log.
     */
    @Override
    public void trace(String aMessage) {
        if (this.isLogDisabled(LoggingMessageLevel.TRACE)) return;
        this.out(this.formLogMessage(aMessage, this.loggerName, LoggingMessageLevel.TRACE));
    }

    /**
     * Log message with "DEBUG" logging level.
     * @param aMessage - message to log.
     */
    @Override
    public void debug(String aMessage) {
        if (this.isLogDisabled(LoggingMessageLevel.DEBUG)) return;
        this.out(this.formLogMessage(aMessage, this.loggerName, LoggingMessageLevel.DEBUG));
    }

    /**
     * Log message with "INFO" logging level.
     * @param aMessage - message to log.
     */
    @Override
    public void info(String aMessage) {
        if (this.isLogDisabled(LoggingMessageLevel.INFO)) return;
        this.out(this.formLogMessage(aMessage, this.loggerName, LoggingMessageLevel.INFO));
    }

    /**
     * Log message with "WARN" logging level.
     * @param aMessage - message to log.
     */
    @Override
    public void warn(String aMessage) {
        if (this.isLogDisabled(LoggingMessageLevel.WARN)) return;
        this.out(this.formLogMessage(aMessage, this.loggerName, LoggingMessageLevel.WARN));
    }

    /**
     * Log message with "ERROR" logging level.
     * @param aMessage - message to log.
     */
    @Override
    public void error(String aMessage) {
        if (this.isLogDisabled(LoggingMessageLevel.ERROR)) return;
        this.out(this.formLogMessage(aMessage, this.loggerName, LoggingMessageLevel.ERROR));
    }

    /**
     * Log message with "FATAL" logging level.
     * @param aMessage - message to log.
     */
    @Override
    public void fatal(String aMessage) {
        if (this.isLogDisabled(LoggingMessageLevel.FATAL)) return;
        this.out(this.formLogMessage(aMessage, this.loggerName, LoggingMessageLevel.FATAL));
    }

    /**
     * Log formatted message (String#format) with "TRACE" logging level.
     * @param aMessage - message to log.
     * @param args - formatting arguments.
     */
    @Override
    public void tracef(String aMessage, Object... args) {
        if (this.isLogDisabled(LoggingMessageLevel.TRACE)) return;
        this.out(this.formLogMessage(String.format(aMessage, args), this.loggerName, LoggingMessageLevel.TRACE));
    }

    /**
     * Log formatted message (String#format) with "DEBUG" logging level.
     * @param aMessage - message to log.
     * @param args - formatting arguments.
     */
    @Override
    public void debugf(String aMessage, Object... args) {
        if (this.isLogDisabled(LoggingMessageLevel.DEBUG)) return;
        this.out(this.formLogMessage(String.format(aMessage, args), this.loggerName, LoggingMessageLevel.DEBUG));
    }

    /**
     * Log formatted message (String#format) with "INFO" logging level.
     * @param aMessage - message to log.
     * @param args - formatting arguments.
     */
    @Override
    public void infof(String aMessage, Object... args) {
        if (this.isLogDisabled(LoggingMessageLevel.INFO)) return;
        this.out(this.formLogMessage(String.format(aMessage, args), this.loggerName, LoggingMessageLevel.INFO));
    }

    /**
     * Log formatted message (String#format) with "WARN" logging level.
     * @param aMessage - message to log.
     * @param args - formatting arguments.
     */
    @Override
    public void warnf(String aMessage, Object... args) {
        if (this.isLogDisabled(LoggingMessageLevel.WARN)) return;
        this.out(this.formLogMessage(String.format(aMessage, args), this.loggerName, LoggingMessageLevel.WARN));
    }

    /**
     * Log formatted message (String#format) with "ERROR" logging level.
     * @param aMessage - message to log.
     * @param args - formatting arguments.
     */
    @Override
    public void errorf(String aMessage, Object... args) {
        if (this.isLogDisabled(LoggingMessageLevel.ERROR)) return;
        this.out(this.formLogMessage(String.format(aMessage, args), this.loggerName, LoggingMessageLevel.ERROR));
    }

    /**
     * Log formatted message (String#format) with "FATAL" logging level.
     * @param aMessage - message to log.
     * @param args - formatting arguments.
     */
    @Override
    public void fatalf(String aMessage, Object... args) {
        if (this.isLogDisabled(LoggingMessageLevel.FATAL)) return;
        this.out(this.formLogMessage(String.format(aMessage, args), this.loggerName, LoggingMessageLevel.FATAL));
    }


}
