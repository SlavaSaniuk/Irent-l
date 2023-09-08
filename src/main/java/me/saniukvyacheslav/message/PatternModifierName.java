package me.saniukvyacheslav.message;

import lombok.NoArgsConstructor;
import me.saniukvyacheslav.util.string.RegexUtils;
import me.saniukvyacheslav.util.string.StringUtils;

import java.util.regex.Matcher;

/**
 * This class used for adding logger name to logging messages.
 * if logging message pattern has "%NAME%" chars sequence, this instance replace it with called logger name.
 */
@NoArgsConstructor
public class PatternModifierName implements PatternModifier {

    public static final String NAME_ARGUMENT_REGEX = "%NAME%";

    /**
     * Modify logging message pattern.
     * Replace "%NAME%" chars sequence with logger name.
     * @param aPattern - logging message pattern.
     * @param anArguments - modifier arguments (1-st [0] - logger name).
     * @return - modified pattern.
     */
    @Override
    public String modify(String aPattern, Object... anArguments) {
        StringUtils.checkForNull(aPattern, "aPattern");
        // Get logger name:
        String loggerName = (String) anArguments[0];
        StringUtils.checkString(loggerName, "anArguments[0]");

        Matcher matcher = RegexUtils.match(aPattern, NAME_ARGUMENT_REGEX);

        if (matcher != null) return matcher.replaceFirst(loggerName);
        else return aPattern;
    }
}
