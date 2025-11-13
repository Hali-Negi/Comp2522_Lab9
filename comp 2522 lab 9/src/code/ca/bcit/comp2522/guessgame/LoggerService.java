package ca.bcit.comp2522.guessgame;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Logs each guess made during a game session.
 * The logger creates a new file for every game, using a timestamp
 * and the secret country name in the file name. Each log entry includes
 * the current time, the user's guess, and the result of that guess.
 *
 * @author Hali Imanpanah
 * @version 1.0
 */
public final class LoggerService implements AutoCloseable
{

    private final Path logsDirectory;
    private final Path logFile;
    private final BufferedWriter writer;

    /**
     * Creates a new log file for the current game session.
     * The file name includes the current timestamp and the secret country.
     *
     * @param secretCountry the secret word for the game (used in the file name)
     * @throws IOException if the log file cannot be created
     */
    public LoggerService(final String secretCountry) throws IOException
    {
        Objects.requireNonNull(secretCountry);

        this.logsDirectory = Paths.get("data", "logs");

        Files.createDirectories(logsDirectory);

        final String timestamp;
        timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

        final String safeCountry;
        safeCountry = secretCountry.replace(' ', '_');

        final String fileName;
        fileName = timestamp +
            "_" +
            safeCountry +
            ".txt";

        this.logFile = logsDirectory.resolve(fileName);

        this.writer = Files.newBufferedWriter(
            logFile,
            StandardCharsets.UTF_8);
    }

    /**
     * Writes a single log entry containing the time, the user's guess,
     * and the outcome of that guess.
     *
     * @param guess   the player's guess
     * @param outcome the result (e.g., "wrong_length", "matches=2", "CORRECT in 3")
     * @throws IOException if writing to the log file fails
     */
    public void logGuess(final String guess,
                         final String outcome) throws IOException
    {
        final String now;
        now = LocalDateTime.now().toString();
        writer.write(now +
                         " | " +
                         guess +
                         " | " +
                         outcome);
        writer.newLine();
        writer.flush();
    }

    /**
     * Closes the log file.
     *
     * @throws IOException if closing the writer fails
     */
    @Override
    public void close() throws IOException
    {
        writer.close();
    }
}
