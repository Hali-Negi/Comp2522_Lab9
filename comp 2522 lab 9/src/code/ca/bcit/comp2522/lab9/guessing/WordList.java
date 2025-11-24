package ca.bcit.comp2522.lab9.guessing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Represents a list of country names used by the guessing game.
 *
 * This class loads the words from a text file and provides access
 * to them as a list of strings.
 *
 * @author Minsu Kim
 * @author Hali Imanpanah
 * @author Esin Sahutoglu
 *
 * @version 1.0
 */
public class WordList
{
    private List<String> countries;

    /**
     * Constructs a {@code WordList} by reading all lines
     * from the specified file.
     *
     * @param filePath the path to the file containing the country list
     */
    public WordList(final Path filePath)
    {
        try
        {
            countries = Files.readAllLines(filePath);
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Returns the list of countries.
     *
     * @return the list of countries
     */
    public List<String> getCountries()
    {
        return countries;
    }
}
