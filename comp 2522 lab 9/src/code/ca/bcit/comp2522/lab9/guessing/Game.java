package ca.bcit.comp2522.lab9.guessing;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Represents the Lucky Vault guessing game for country names.
 *
 * The game loads a list of countries, chooses one at random as the
 * secret word, and lets the user guess until they either quit or
 * find the correct country. It also reports and updates the best score.
 *
 * @author Minsu Kim
 * @author Hali Imanpanah
 * @author Esin Sahutoglu
 * @version 1.0
 */
public class Game
{
    private static final int NO_ATTEMPTS = 0;
    private static final int NO_BEST_RECORD = -1;
    private static final int FIRST_INDEX = 0;
    private static final int NO_COUNT = 0;

    /**
     * Runs the Lucky Vault game in COUNTRY mode.
     *
     * This method sets up the input scanner, loads the list of
     * countries and the current high score, selects a secret country,
     * and processes user guesses until the game is finished.
     *
     * @throws IOException if an I/O error occurs while reading
     *                     or writing game data
     */
    public void run()
        throws IOException
    {
        final Scanner scanner;
        final ca.bcit.comp2522.lab9.guessing.WordList wordList;
        final ca.bcit.comp2522.lab9.guessing.HighScoreService highScore;
        final ca.bcit.comp2522.lab9.guessing.LoggerService logger;
        final Path countriesPath;
        final Path highScorePath;
        final Path logsDir;
        final List<String> countries;
        final Random random;
        final int best;

        String secret;
        String input;
        int attempts;

        countriesPath = Paths.get("data", "countries.txt");
        highScorePath = Paths.get("data", "highscore.txt");
        logsDir       = Paths.get("data", "logs");

        scanner   = new Scanner(System.in);
        wordList  = new ca.bcit.comp2522.lab9.guessing.WordList(countriesPath);
        highScore = new ca.bcit.comp2522.lab9.guessing.HighScoreService(highScorePath);
        logger    = new ca.bcit.comp2522.lab9.guessing.LoggerService(logsDir);
        random    = new Random();

        countries = wordList.getCountries();
        best      = highScore.getHighScore();

        if (countries.isEmpty())
        {
            System.out.println("No countries found");
            return;
        }

        secret   = countries.get(random.nextInt(countries.size()));
        attempts = NO_ATTEMPTS;

        System.out.println(
            "LUCKY VAULT — COUNTRY MODE. Type QUIT to exit.");
        System.out.println(
            "Secret word length: " + secret.length());
        if (best == NO_BEST_RECORD)
        {
            System.out.println("Current best: —");
        }
        else
        {
            System.out.println("Current best: " +
                                   best +
                                   " attempts");
        }
        System.out.println("(For testing) Secret word is: " + secret); // TODO: remove later

        while (true)
        {
            System.out.print("Your guess: ");
            input = scanner.nextLine().trim();

            if (input.isEmpty())
            {
                System.out.println("Empty guess. Try again.");
                continue;
            }
            if (input.equalsIgnoreCase("QUIT"))
            {
                logger.log("QUIT", "user_exit");
                System.out.println("Bye.");
                break;
            }
            else
            {
                attempts++;
                if (input.length() != secret.length())
                {
                    logger.log(input, "wrong_length");
                    System.out.println("Wrong length (" +
                                           input.length() +
                                           "). Need " +
                                           secret.length() +
                                           ".");
                }
                else if (input.equalsIgnoreCase(secret))
                {
                    logger.log(input, "correct in " + attempts);
                    System.out.println("Correct in " +
                                           attempts +
                                           " attempts! Word was: " +
                                           secret);

                    if (best == NO_BEST_RECORD ||
                        attempts < best)
                    {
                        System.out.println("NEW BEST for COUNTRY mode!");
                        highScore.updateHighScore(attempts);
                    }
                    break;
                }
                else
                {
                    int count = NO_COUNT;
                    for (int i = FIRST_INDEX; i < input.length(); i++)
                    {
                        if (input.toLowerCase().charAt(i) ==
                            secret.toLowerCase().charAt(i))
                        {
                            count++;
                        }
                    }
                    logger.log(input, "matches=" + count);
                    System.out.println("Not it. " +
                                           count +
                                           " letter(s) correct (right position).");
                }
            }
        }
        logger.close();
        scanner.close();
    }

    /**
     * Entry point of the program.
     *
     * Creates a {@code Game} instance and runs the Lucky Vault game.
     *
     * @param args the command line arguments (not used)
     * @throws IOException if an I/O error occurs while running the game
     */
    public static void main(final String[] args)
        throws IOException
    {
        final Game game;
        game = new Game();

        game.run();
    }
}
