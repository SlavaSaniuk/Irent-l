package me.saniukvyacheslav.output;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Implementation of {@link LoggerOutput} interface.
 * Out messages to console (System.out).
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoggerConsoleOutput implements LoggerOutput {

    private static LoggerConsoleOutput INSTANCE; // Singleton instance;

    /**
     * Get current singleton instance of this class.
     * @return - singleton instance.
     */
    public static LoggerConsoleOutput getInstance() {
        if (LoggerConsoleOutput.INSTANCE == null) LoggerConsoleOutput.INSTANCE = new LoggerConsoleOutput();
        return LoggerConsoleOutput.INSTANCE;
    }

    /**
     * Out message to console (System.out).
     * @param aMessage - message to out.
     */
    @Override
    public void out(String aMessage) {
        System.out.println(aMessage);
    }

    /**
     * Close inner BufferedWriter instance.
     */
    @Override
    public void close() {
        // Do nothing.
    }
}
