package me.saniukvyacheslav.message;

/**
 * Base interface for all logging messages modifiers.
 * Used for modify logging message before log.
 */
public interface PatternModifier {

    /**
     * Modify logging message pattern.
     * @param aPattern - logging message pattern.
     * @param anArguments - modifier arguments.
     * @return - modified pattern.
     */
    String modify(String aPattern, Object... anArguments);

}
