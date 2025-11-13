package ca.bcit.comp2522.guessgame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.OptionalInt;


/**
 * Handles reading and writing the high score for the game.
 * The score is stored in a text file in the data folder.
 *
 * @author Hali Imanpanah
 * @version 1.0
 */
public final class HighScoreService
{

    private static final int MIN_VALID_SCORE = 1;

    private final Path highScorePath;

    /**
     * Creates a HighScoreService for the given file path.
     *
     * @param highScorePath the file where the high score is stored
     */
    public HighScoreService(final Path highScorePath)
    {
        this.highScorePath = Objects.requireNonNull(highScorePath);
    }

    /**
     * Reads the current best score from data high score.txt.
     * <p>
     * Returns OptionalInt.empty() if file missing or malformed.
     */
    public OptionalInt readBest()
    {
        if (Files.notExists(highScorePath))
        {
            return OptionalInt.empty();
        }

        try (BufferedReader reader =
                 Files.newBufferedReader(highScorePath, StandardCharsets.UTF_8))
        {

            final String line;
            line = reader.readLine();

            if (line == null)
            {
                return OptionalInt.empty();
            }

            final String trimmed;
            trimmed = line.trim();

            if (!trimmed.startsWith("COUNTRY="))
            {
                return OptionalInt.empty();
            }

            final String numberPart;
            numberPart = trimmed.substring("COUNTRY=".length()).trim();

            final int value;
            value = Integer.parseInt(numberPart);

            if (value <= MIN_VALID_SCORE)
            {
                return OptionalInt.empty();
            }

            return OptionalInt.of(value);
        }
        catch (final IOException | NumberFormatException e)
        {
            // malformed or unreadable → treat as no best
            return OptionalInt.empty();
        }
    }

    /**
     * Saves a new best score to the high score file.
     *
     * @param attempts the number of attempts for the new best score
     * @throws IOException if writing the file fails
     */
    public void writeBest(final int attempts) throws IOException
    {
        if (attempts <= MIN_VALID_SCORE)
        {
            throw new IllegalArgumentException("attempts must be positive");
        }

        final Path parent;
        parent = highScorePath.getParent();

        if (parent != null)
        {
            Files.createDirectories(parent);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(
            highScorePath,
            StandardCharsets.UTF_8,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING))
        {

            writer.write("COUNTRY=" + attempts);
            writer.newLine();
        }
    }
}

