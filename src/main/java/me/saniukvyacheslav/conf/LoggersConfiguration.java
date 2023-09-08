package me.saniukvyacheslav.conf;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.saniukvyacheslav.Logger;
import me.saniukvyacheslav.util.file.FileUtils;
import me.saniukvyacheslav.impl.LoggerImpl;
import me.saniukvyacheslav.message.*;
import me.saniukvyacheslav.output.LoggerConsoleOutput;
import me.saniukvyacheslav.output.LoggerFileOutput;
import me.saniukvyacheslav.output.LoggerOutput;
import me.saniukvyacheslav.util.string.StringUtils;
import me.saniukvyacheslav.definition.pattern.Builder;
import me.saniukvyacheslav.util.string.RegexUtils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Configuration for set of loggers.
 * Developer can create many configurations for different environments, packages etc.
 */
public class LoggersConfiguration implements Closeable {

    private boolean enableLoggers = false; // FLAG: Enable/Disable loggers in this configuration;
    @Getter private boolean isEnabledConsoleOutput = true; // FLAG: Enable/Disable logging to console;
    @Getter private boolean isEnabledFileOutput = false; // FLAG: Enable/Disable logging to file;
    @Getter private String logMessagePattern; // Logging message pattern;
    @Getter private boolean isUseCanonicalNames = false; // FLAG: Usage of Canonical/Simple loggers names;
    @Getter private LoggingMessageLevel minimalLevelOfMessages = LoggingMessageLevel.TRACE;
    @Getter private final Map<String, LoggerImpl> loggers = new HashMap<>(); // This configuration loggers;
    @Getter private final List<LoggerOutput> loggersOutputs = new ArrayList<>(); // List of loggers outputs;
    @Getter private final List<PatternModifier> modifiers = new ArrayList<>(); // List of modifiers;

    /**
     * Construct new configuration with specified name.
     * @param aName - configuration name.
     */
    private LoggersConfiguration(String aName) {
        StringUtils.checkString(aName, "aName");
        // Check if configuration with given name already exist:
        if (LoggingConfiguration.getInstance().loggersConfigurations.containsKey(aName))
            throw new IllegalArgumentException(String.format("Logger configuration with name [%s] already exist.", aName));
        LoggingConfiguration.getInstance().loggersConfigurations.put(aName, this);

        // Initialize loggers outputs:
        try {
            this.enableConsoleOutput(this.isEnabledConsoleOutput);
            this.enableFileOutput(false, null, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Set default logging message pattern:
        this.setLoggingMessagePattern(LoggingConfiguration.DEFAULT_LOG_MESSAGE_PATTERN);
    }

    /**
     * Get logger, configurable by this configuration by it name.
     * If logger doesn't exist, method create it.
     * @param aName - logger name.
     * @return - logger.
     */
    public Logger getLogger(String aName) {
        if (!this.loggers.containsKey(aName)) {
            this.loggers.put(aName, new LoggerImpl(this, aName));
        }
        return this.loggers.get(aName);
    }

    /**
     * Get logger, configurable by this configuration by it name as same as specified class name.
     * If logger doesn't exist, method create it.
     * @param aLoggableClass - logger name as class name.
     * @return - logger.
     */
    public Logger getLogger(Class<?> aLoggableClass) {
        if (this.isUseCanonicalNames) return this.getLogger(aLoggableClass.getCanonicalName());
        else return this.getLogger(aLoggableClass.getSimpleName());
    }

    /**
     * Get "Enable/Disable loggers" flag value.
     * @return - flag value.
     */
    public boolean isLoggersEnabled() {
        return this.enableLoggers;
    }

    /**
     * Enable/Disable logging to console.
     * @param isEnable - value.
     */
    private void enableConsoleOutput(boolean isEnable) {
        if (isEnable) {
            if (!this.loggersOutputs.contains(LoggerConsoleOutput.getInstance()))
                this.loggersOutputs.add(LoggerConsoleOutput.getInstance());
        }else this.loggersOutputs.remove(LoggerConsoleOutput.getInstance());

        this.isEnabledConsoleOutput = isEnable;
    }

    /**
     * Enable / Disable logging to file.
     * @param isEnable - enable / disable flag.
     * @param anOutputFile - log file.
     * @param isAppend - is appends to end of file?
     * @throws IOException - If IO exceptions
     */
    private void enableFileOutput(boolean isEnable, File anOutputFile, boolean isAppend) throws IOException {
        if (isEnable) {
            this.loggersOutputs.add(new LoggerFileOutput(anOutputFile, isAppend));
        }else {
            this.loggersOutputs.forEach((loggerOutput -> {
                if (loggerOutput instanceof LoggerFileOutput) this.loggersOutputs.remove(loggerOutput);
            }));
        }

        this.isEnabledFileOutput = isEnable;
    }

    /**
     * Set "Enable/Disable loggers" flag value.
     * @param isEnable - flag value.
     */
    public void enableLoggers(boolean isEnable) {
        this.enableLoggers = isEnable;
    }

    /**
     * Set common logging message pattern for this configuration.
     * Method construct modifiers instances, which will be used in pattern, and add it to modifiers list.
     * @param aPattern - logging message pattern.
     */
    private void setLoggingMessagePattern(String aPattern) {
        StringUtils.checkString(aPattern, "aPattern");
        // Check for required "%MSG%" arguments:
        if(RegexUtils.match(aPattern, "%MSG%") == null)
            throw new IllegalArgumentException("Logging message pattern string must have required [%MSG%] chars sequence.");

        // Map:
        this.logMessagePattern = aPattern;

        // Initialize modifiers:
        if (RegexUtils.match(aPattern, PatternModifierTime.TIME_ARGUMENT_REGEX) != null) // Time modifier;
            this.modifiers.add(new PatternModifierTime());
        if (RegexUtils.match(aPattern, PatternModifierName.NAME_ARGUMENT_REGEX) != null) // Name modifier;
            this.modifiers.add(new PatternModifierName());
        if (RegexUtils.match(aPattern, PatternModifierLevel.LEVEl_ARGUMENT_REGEX) != null) // Level modifier;
            this.modifiers.add(new PatternModifierLevel());
    }

    /**
     * Set time format for {@link PatternModifierTime} modifier.
     * If time modifier doesn't exist in "modifiers" list, do nothing.
     * @param aFormat - time format.
     */
    private void setTimeFormat(String aFormat) {
        StringUtils.checkString(aFormat, "aFormat");

        // Check if time modifier is enabled:
        Optional<PatternModifier> timeModifierOpt = this.modifiers.stream().filter((patternModifier -> patternModifier instanceof PatternModifierTime)).findFirst();
        if (!timeModifierOpt.isPresent()) return;

        PatternModifierTime timeModifier = (PatternModifierTime) timeModifierOpt.get();
        timeModifier.setTimeFormatter(DateTimeFormatter.ofPattern(aFormat));
    }

    /**
     * Close all {@link LoggerOutput} outputs of this configuration.
     */
    @Override
    public void close() {
        this.loggersOutputs.forEach((loggersOutput) -> {
            try {
                loggersOutput.close();
            } catch (IOException e) {
                //Do nothing;
            }
        });
    }

    /**
     * Builder for {@link LoggingConfiguration} configurations.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class LoggerConfigurationBuilder implements Builder<LoggersConfiguration> {

        private String confName; // Future configuration name;
        private boolean isLoggersEnabled = false;
        private boolean isEnabledConsoleOutput = true;
        private boolean isEnabledFileOutput = false;
        private File outputFile;
        private boolean isAppendToEndOfOutputFile;
        private String loggingMessagePattern = null;
        private String timeFormat = null;
        private boolean isUseCanonicalLoggersName = false;
        private LoggingMessageLevel minimalLevelOfMessages = LoggingMessageLevel.TRACE;

        /**
         * Set future configuration name.
         * @param aName - configuration name.
         * @return - this builder.
         */
        public static LoggerConfigurationBuilder ofName(String aName) {
            StringUtils.checkString(aName, "aName");

            LoggerConfigurationBuilder builder = new LoggerConfigurationBuilder();
            builder.confName = aName;

            return builder;
        }

        /**
         * Enable/Disable loggers in future configuration.
         * @param isEnable - enable or disable loggers value.
         * @return - this builder.
         */
        public LoggerConfigurationBuilder enableLoggers(boolean isEnable) {
            this.isLoggersEnabled = isEnable;
            return this;
        }

        /**
         * Enable/Disable logging to console.
         * @param isEnable - value.
         * @return - this builder.
         */
        public LoggerConfigurationBuilder enableConsoleOutput(boolean isEnable) {
            this.isEnabledConsoleOutput = isEnable;
            return this;
        }

        /**
         * Enable logging to file.
         * @return - LoggerFileOutput factory instance.
         */
        public LoggerFileOutput.LoggerFileOutputFactory enableFileOutput() {
            return new LoggerFileOutput.LoggerFileOutputFactory(this);
        }

        /**
         * Enable logging to specified file.
         * @param anOutputFile - log file.
         * @param isAppend - is appends to end of file?
         * @return - this builder.
         */
        public LoggerConfigurationBuilder enableFileOutput(File anOutputFile, boolean isAppend) {
            FileUtils.checkFile(anOutputFile);
            this.isEnabledFileOutput = true;
            this.outputFile = anOutputFile;
            this.isAppendToEndOfOutputFile = isAppend;
            return this;
        }

        /**
         * Set common logging message pattern for all future configuration.
         * @param aPattern - pattern.
         * @return - this builder.
         */
        public LoggerConfigurationBuilder loggingMessagePattern(String aPattern) {
            StringUtils.checkForNull(aPattern, "aPattern");
            this.loggingMessagePattern = aPattern;
            return this;
        }

        /**
         * Set time format in logging messages.
         * @param aFormat - time format.
         * @return - this builder.
         */
        public LoggerConfigurationBuilder setTimeFormat(String aFormat) {
            this.timeFormat = aFormat;
            return this;
        }

        public LoggerConfigurationBuilder useCanonicalLoggersName(boolean isUse) {
            this.isUseCanonicalLoggersName = isUse;
            return this;
        }

        public LoggerConfigurationBuilder minimalLevelOfMessages(LoggingMessageLevel aLevel) {
            this.minimalLevelOfMessages = aLevel;
            return this;
        }

        /**
         * Try to build new logger configuration.
         * @return - configuration instance.
         */
        @Override
        public LoggersConfiguration build() {
            // Build configuration:
            LoggersConfiguration configuration = new LoggersConfiguration(this.confName); // Can throw NPE/IAE;

            // Set parameters:
            try {
                configuration.enableLoggers = this.isLoggersEnabled;
                configuration.enableConsoleOutput(this.isEnabledConsoleOutput);
                configuration.enableFileOutput(this.isEnabledFileOutput, this.outputFile, this.isAppendToEndOfOutputFile);
                if (this.loggingMessagePattern != null) configuration.setLoggingMessagePattern(this.loggingMessagePattern);
                if (this.timeFormat != null) configuration.setTimeFormat(this.timeFormat);
                configuration.isUseCanonicalNames = this.isUseCanonicalLoggersName;
                configuration.minimalLevelOfMessages = this.minimalLevelOfMessages;
            }catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }

            // Return configuration:
            return configuration;
        }
    }

}
