package ca.bcit.comp2522.lab9.guessing;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a simple logging service for the Lucky Vault game.
 *
 * Each game session creates a timestamped log file in the {@code logs}
 * directory. Every user action (guess, quit, result) is stored as one
 * line in the format:
 *
 * @author Minsu Kim
 * @author Hali Imanpanah
 * @author Esin Sahutoglu
 *
 * @version 1.0
 */
public class LoggerService
{
    private static final String FILE_NAME_PATTERN = "yyyy-MM-dd_HH-mm-ss";
    private static final String TIME_PATTERN      = "HH:mm:ss";
    private static final String MODE_NAME         = "COUNTRY";

    private final Path logFilePath;
    private final BufferedWriter writer;


    /**
     * Constructs a {@code LoggerService} using the given logs directory.
     *
     * A new log file is created inside the directory. The file name includes
     * the current timestamp to uniquely identify the session.
     *
     * @param logsDir the directory where log files are stored
     * @throws IOException if the directory or log file cannot be created
     */
    public LoggerService(final Path logsDir)
        throws IOException
    {
        if (Files.notExists(logsDir))
        {
            Files.createDirectories(logsDir);
        }

        final LocalDateTime now;
        final DateTimeFormatter fileFormatter;
        final String fileName;
        final Path filePath;
        final BufferedWriter writer;

        now           = LocalDateTime.now();
        fileFormatter = DateTimeFormatter.ofPattern(FILE_NAME_PATTERN);
        fileName      = now.format(fileFormatter) + "_" + MODE_NAME + ".txt";
        filePath      = logsDir.resolve(fileName);

        this.logFilePath = filePath;

        writer      = Files.newBufferedWriter(filePath);
        this.writer = writer;
    }

    /**
     * Writes a log entry for the current game event.
     *
     * Each entry records the current time, the user's guess,
     * and the outcome of that guess.
     *
     * @param guess   the user’s input
     * @param outcome the result or status associated with the guess
     * @throws IOException if an error occurs while writing to the log file
     */
    public void log(final String guess,
                    final String outcome)
        throws IOException
    {
        final LocalTime now;
        final DateTimeFormatter timeFormatter;
        final String timeText;
        final String line;

        now           = LocalTime.now();
        timeFormatter = DateTimeFormatter.ofPattern(TIME_PATTERN);
        timeText      = now.format(timeFormatter);
        line          = timeText + " guess=" + guess + " outcome=" + outcome;

        writer.write(line);
        writer.newLine();
        writer.flush();
    }

    /**
     * Closes the logger and releases its resources.
     *
     * @throws IOException if an error occurs while closing the writer
     */
    public void close() throws IOException
    {
        writer.close();
    }

    /**
     * Gets the log file path.
     *
     * @return the log file path
     */
    public Path getLogFilePath()
    {
        return logFilePath;
    }
}
