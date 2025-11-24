package ca.bcit.comp2522.lab9.guessing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * /**
 * * Represents the high-score service.
 *
 * This class is responsible for reading, storing, and updating
 * the best number of attempts (high score) for COUNTRY mode.
 * The score is persisted in a simple text file.
 *
 * @author Minsu Kim
 * @author Hali Imanpanah
 * @author Esin Sahutoglu
 *
 * @version 1.0
 */
public class HighScoreService
{
    private static final int NO_RECORD = -1;
    private static final String PREFIX = "COUNTRY=";

    private final Path filePath;
    private int highScore;

    /**
     * Constructs a {@code HighScoreService} using the given file path.
     *
     * If the file does not exist, it is created. The existing content
     * is then read to initialize the {@code highScore} value.
     *
     * @param filePath the file path to the high-score file
     * @throws IOException if an I/O error occurs while creating
     *                     or reading the file
     */
    public HighScoreService(final Path filePath)
        throws IOException
    {
        this.filePath = filePath;

        if (Files.notExists(filePath))
        {
            Files.createFile(filePath);
        }

        this.highScore = loadHighScore();
    }

    /**
     * Returns the current high score.
     *
     * If there is no valid record, this method returns {@code NO_RECORD}.
     *
     * @return the high-score, or {@code NO_RECORD} if none exists
     */
    public int getHighScore()
    {
        return highScore;
    }

    /**
     * Loads the high score from the file.
     * <p>
     * The method expects the file content to be a single line
     * starting with {@code PREFIX}, followed by an integer.
     * If the file is empty, does not start with the prefix,
     * or contains an invalid number, {@code NO_RECORD} is returned.
     *
     * @return the loaded high-score, or {@code NO_RECORD} if
     * the file has no valid record
     * @throws IOException if an I/O error occurs while reading the file
     */
    private int loadHighScore()
        throws IOException
    {
        final String line;
        final String valuePart;

        line = Files.readString(filePath).trim();

        if (line.isEmpty())
        {
            return NO_RECORD;
        }

        if (!line.startsWith(PREFIX))
        {
            return NO_RECORD;
        }

        valuePart = line.substring(PREFIX.length()).trim();

        try
        {
            return Integer.parseInt(valuePart);
        }
        catch (NumberFormatException e)
        {
            return NO_RECORD;
        }
    }

    /**
     * Updates the high score if the given number of attempts is better.
     *
     * If there is no existing record, or if {@code attempts} is less
     * than the current high score, this method updates both the in-memory
     * value and the file content. Otherwise, no change is made.
     *
     * @param attempts the number of attempts made in the current game
     * @throws IOException if an I/O error occurs while writing the file
     */
    public void updateHighScore(final int attempts)
        throws IOException
    {
        if (highScore == NO_RECORD ||
            attempts < highScore)
        {
            highScore = attempts;

            final String line;
            line = PREFIX + attempts;
            Files.writeString(filePath, line);
        }
    }
}