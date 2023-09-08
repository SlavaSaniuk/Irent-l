package me.saniukvyacheslav.output;

import me.saniukvyacheslav.util.file.FileUtils;
import me.saniukvyacheslav.conf.LoggersConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class LoggerFileOutput implements LoggerOutput {

    private final BufferedWriter writer; // Writer;

    /**
     * Construct new instance of this class for specified file.
     * @param anOutputFile - output file.
     * @param isAppend - is appends messages to end of file?.
     * @throws IOException - If IO exception occurs.
     */
    public LoggerFileOutput(File anOutputFile, boolean isAppend) throws IOException {
        FileUtils.checkFile(anOutputFile);
        this.writer = new BufferedWriter(new FileWriter(anOutputFile, isAppend)); // Initialize buffered writer;

    }

    /**
     * Out message to file.
     * @param aMessage - message to out.
     * @throws RuntimeException - if IO exception occur.
     */
    @Override
    public void out(String aMessage) {
        if (aMessage == null) return;
        try {
            this.writer.write(aMessage +System.lineSeparator());
            this.writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Close inner BufferedWriter writer.
     * @throws IOException - If IO Exception occurs.
     */
    @Override
    public void close() throws IOException {
        // Close this buffered writer:
        this.writer.close();
    }

    public static final class LoggerFileOutputFactory {
        private final LoggersConfiguration.LoggerConfigurationBuilder configurationBuilder;
        public LoggerFileOutputFactory(LoggersConfiguration.LoggerConfigurationBuilder aConfigurationBuilder) {
            Objects.requireNonNull(aConfigurationBuilder, "LoggersConfigurationBuilder [aConfigurationBuilder] must be not null.");
            this.configurationBuilder = aConfigurationBuilder;
        }

        public LoggersConfiguration.LoggerConfigurationBuilder ofFile(File anOutputFile, boolean isAppend) {
            // Check file:
            FileUtils.checkFile(anOutputFile);
            return this.configurationBuilder.enableFileOutput(anOutputFile, isAppend);
        }

    }

}
