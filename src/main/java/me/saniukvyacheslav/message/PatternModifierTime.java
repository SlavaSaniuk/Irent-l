package me.saniukvyacheslav.message;

import lombok.Setter;
import me.saniukvyacheslav.util.string.RegexUtils;
import me.saniukvyacheslav.util.string.StringUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;

/**
 * This class used for adding current time to logging messages.
 * if logging message pattern has "%TIME%" chars sequence, this instance replace it with current time.
 * Developers can set different time formats via
 * {@link me.saniukvyacheslav.logging.conf.LoggersConfiguration.LoggerConfigurationBuilder#setTimeFormat(String)} method
 * for different loggers configurations. By default, time format is "HH:mm:ss".
 */
public class PatternModifierTime implements PatternModifier {

    public static final String TIME_ARGUMENT_REGEX = "%TIME%";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss"; // Default time format;
    @Setter private DateTimeFormatter timeFormatter; // Time to string formatter;

    /**
     * Construct new instance of this class.
     */
    public PatternModifierTime() {
        this.timeFormatter = DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT);
    }

    /**
     * Modify logging message pattern.
     * Replace "%TIME%" char sequence with current time.
     * @param aPattern - logging message pattern.
     * @param anArguments - modifier arguments.
     * @return - modified pattern.
     */
    @Override
    public String modify(String aPattern, Object... anArguments) {
        StringUtils.checkForNull(aPattern, "aPattern");

        Matcher matcher = RegexUtils.match(aPattern, TIME_ARGUMENT_REGEX);

        if (matcher != null) return matcher.replaceFirst(LocalTime.now().format(timeFormatter));
        else return aPattern;
    }
}
