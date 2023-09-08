package me.saniukvyacheslav.output;

import java.io.Closeable;

/**
 * Define common method to print message to implemented output.
 * Pay attention! Implemented instance must be closed.
 */
public interface LoggerOutput extends Closeable {

    /**
     * Out message to output.
     * @param aMessage - message to out.
     */
    void out(String aMessage);
}
