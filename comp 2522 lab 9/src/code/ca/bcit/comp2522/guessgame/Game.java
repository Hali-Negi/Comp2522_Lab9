package ca.bcit.comp2522.guessgame;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.OptionalInt;
import java.util.Random;
import java.util.Scanner;

/**
 * The Game class implements the main logic for the
 * “Lucky Vault — Country Edition” guessing game.
 * Runs the Lucky Vault country guessing game.
 * Loads a list of countries, picks one at random,
 * and lets the user guess until they find the secret word.
 * Also shows and updates the high score.
 *
 * @author Hali Imanpanah
 * @version 1.0
 */
public final class Game
{

    private static final int INITIAL_ATTEMPTS = 0;
    private static final int FIRST_INDEX = 0;

    private final Random random;
    private final WordList wordList;
    private final HighScoreService highScoreService;

    public Game()
    {
        this.random = new Random();

        final Path countriesPath;
        countriesPath = Paths.get("data", "countries.txt");

        final Path highScorePath;
        highScorePath = Paths.get("data", "highscore.txt");

        this.wordList         = new WordList(countriesPath);
        this.highScoreService = new HighScoreService(highScorePath);
    }

    /**
     * Starts the game.
     * Shows the title, word length, and best score.
     * Reads guesses, gives feedback, updates logs,
     * and saves a new high score when needed.
     *
     * @throws IOException if reading or writing files fails
     */
    public void run() throws IOException
    {
        // Load countries
        final List<String> countries;
        countries = wordList.loadCountries();

        // Pick random secret
        final String secret;
        secret = pickSecret(countries);

        final String secretLower;
        secretLower = secret.toLowerCase(Locale.ROOT);

        final int secretLength;
        secretLength = secret.length();

        // Read current best score
        final OptionalInt bestOpt;
        bestOpt = highScoreService.readBest();

        // Print intro
        System.out.println("LUCKY VAULT — COUNTRY MODE. Type QUIT to exit.");
        System.out.println("Secret word length: " +
                               secretLength);

        if (bestOpt.isPresent())
        {
            System.out.println("Current best: " +
                                   bestOpt.getAsInt() +
                                   " attempts");
        }
        else
        {
            System.out.println("Current best: —");
        }

        int attempts = INITIAL_ATTEMPTS;

        // Scanner + Logger with try-with-resources
        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
             LoggerService logger = new LoggerService(secret))
        {

            while (true)
            {
                System.out.print("Your guess: ");

                final String rawGuess;
                rawGuess = scanner.nextLine();

                final String guess;
                guess = rawGuess.trim();

                if (guess.isEmpty())
                {
                    System.out.println("Empty guess. Try again.");
                    logger.logGuess(rawGuess, "empty");
                    continue;
                }

                // QUIT command
                if (guess.equalsIgnoreCase("QUIT"))
                {
                    System.out.println("Bye!");
                    logger.logGuess(rawGuess, "quit");
                    break;
                }

                attempts++;

                // Length check
                if (guess.length() != secretLength)
                {
                    System.out.println("Wrong length (" +
                                           guess.length() +
                                           "). Need " +
                                           secretLength +
                                           ".");

                    logger.logGuess(guess, "wrong_length");
                    continue;
                }

                final String guessLower;
                guessLower = guess.toLowerCase(Locale.ROOT);

                // Exact match?
                if (guessLower.equals(secretLower))
                {
                    System.out.println("Correct in " +
                                           attempts +
                                           " attempts! Word was: " +
                                           secret);

                    logger.logGuess(guess, "CORRECT in " +
                        attempts);

                    final boolean isNewBest;
                    isNewBest = isNewBest(bestOpt, attempts);

                    if (isNewBest)
                    {
                        System.out.println("NEW BEST for COUNTRY mode!");
                        highScoreService.writeBest(attempts);
                    }
                    break;
                }


                // Same length but wrong word → count matching letters in same position
                final int matches;
                matches = countMatchingPositions(secretLower, guessLower);

                System.out.println("Not it. " +
                                       matches +
                                       " letter(s) correct (right position).");

                logger.logGuess(guess, "matches=" + matches);
            }
        }

        System.out.println("The end");
    }

    /**
     * Chooses a random country from the list.
     *
     * @param countries list of country names
     * @return a randomly selected country
     */
    private String pickSecret(final List<String> countries)
    {
        final int index;
        index = random.nextInt(countries.size());

        return countries.get(index);
    }

    /**
     * Checks if the current attempts count is better than the saved best score.
     *
     * @param bestOpt  previous best score (if any)
     * @param attempts attempts in this game
     * @return true if this is a new best score
     */
    private boolean isNewBest(final OptionalInt bestOpt,
                              final int attempts)
    {
        return !bestOpt.isPresent() ||
            attempts < bestOpt.getAsInt();
    }

    /**
     * Counts how many letters match in the same positions
     * between the secret word and the user's guess.
     *
     * @param secretLower secret word in lowercase
     * @param guessLower  guess in lowercase
     * @return number of matching positions
     */
    private int countMatchingPositions(final String secretLower,
                                       final String guessLower)
    {
        final int length;
        length = Math.min(secretLower.length(), guessLower.length());

        int matches = FIRST_INDEX;

        for (int i = FIRST_INDEX; i < length; i++)
        {
            if (secretLower.charAt(i) == guessLower.charAt(i))
            {
                matches++;
            }
        }
        return matches;
    }
}
