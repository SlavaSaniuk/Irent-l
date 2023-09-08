package me.saniukvyacheslav.message;

import lombok.AllArgsConstructor;

/**
 * All logging message levels.
 */
@AllArgsConstructor
public enum LoggingMessageLevel {

    /**
     * Trace level.
     */
    TRACE("TRACE", 0),
    /**
     * Debug level.
     */
    DEBUG("DEBUG", 1),
    /**
     * Info level.
     */
    INFO("INFO", 2),
    /**
     * Warn level.
     */
    WARN("WARN", 3),
    /**
     * Error level.
     */
    ERROR("ERROR", 4),
    /**
     * Fatal level.
     */
    FATAL("FATAL", 5);

    private final String levelName;
    private final int level;

    /**
     * Get level name.
     * @return - level name.
     */
    public String getLevelName() {return this.levelName;}

    /**
     * Get level.
     * @return - level.
     */
    public int getLevel() {
        return this.level;
    }
}
