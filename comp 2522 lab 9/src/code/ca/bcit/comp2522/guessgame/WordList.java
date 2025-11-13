package ca.bcit.comp2522.guessgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class WordList
{

    private final Path countriesPath;

    public WordList(final Path countriesPath)
    {
        this.countriesPath = Objects.requireNonNull(countriesPath);
    }

    /**
     * Loads all non-empty lines from countries.txt as country names.
     *
     * @return an unmodifiable list of country names
     * @throws IOException           if the file cannot be read
     * @throws IllegalStateException if the file exists but is empty
     */
    public List<String> loadCountries() throws IOException
    {
        if (Files.notExists(countriesPath))
        {
            throw new IOException("countries file not found: " +
                                      countriesPath);
        }

        final List<String> countries;
        countries = new ArrayList<>();

        try (BufferedReader reader =
                 Files.newBufferedReader(countriesPath, StandardCharsets.UTF_8))
        {

            String line;
            while ((line = reader.readLine()) != null)
            {
                final String trimmed;
                trimmed = line.trim();

                if (!trimmed.isEmpty())
                {
                    countries.add(trimmed);
                }
            }
        }

        if (countries.isEmpty())
        {
            throw new IllegalStateException("countries file is empty: " +
                                                countriesPath);
        }

        return Collections.unmodifiableList(countries);
    }
}
