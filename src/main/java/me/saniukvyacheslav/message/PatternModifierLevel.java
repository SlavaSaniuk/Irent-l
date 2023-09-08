package me.saniukvyacheslav.message;

import lombok.NoArgsConstructor;
import me.saniukvyacheslav.util.string.RegexUtils;
import me.saniukvyacheslav.util.string.StringUtils;

import java.util.Objects;
import java.util.regex.Matcher;

/**
 * This class used for adding message level to logging messages.
 * if logging message pattern has "%LEVEL%" chars sequence, this instance replace it with called method level.
 */
@NoArgsConstructor
public class PatternModifierLevel implements PatternModifier {

    public static final String LEVEl_ARGUMENT_REGEX = "%LEVEL%";

    /**
     * Modify logging message pattern.
     * Replace "%LEVEL%" chars sequence with message level name.
     * @param aPattern - logging message pattern.
     * @param anArguments - modifier arguments (1-st [0] - logger name, 2-nd [1] - LoggingMessageLevel constant).
     * @return - modified pattern.
     */
    @Override
    public String modify(String aPattern, Object... anArguments) {
        StringUtils.checkForNull(aPattern, "aPattern");
        // Get logger name:
        LoggingMessageLevel messageLevel = (LoggingMessageLevel) anArguments[1];
        Objects.requireNonNull(messageLevel, "loggingMessageLevel [anArguments[1]] must be not null.");

        Matcher matcher = RegexUtils.match(aPattern, LEVEl_ARGUMENT_REGEX);
        if (matcher != null) return matcher.replaceFirst(messageLevel.getLevelName());
        else return aPattern;
    }
}
