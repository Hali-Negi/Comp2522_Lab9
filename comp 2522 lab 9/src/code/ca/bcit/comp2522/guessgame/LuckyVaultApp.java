package ca.bcit.comp2522.guessgame;

import java.io.IOException;

/**
 * The main entry point for the Lucky Vault game.
 * This class only contains the {@code main} method and should not be
 * instantiated. The private constructor prevents creating objects of
 * this class.
 *
 * @author Hali Imanpanah
 * @version 1.0
 */
public final class LuckyVaultApp
{

    /**
     * Private constructor to prevent instantiation of this class.
     * This class is only used to launch the game.
     */

    private LuckyVaultApp()
    {

    }

    public static void main(final String[] args)
    {
        final Game game = new Game();

        try
        {
            game.run();
        }
        catch (final IOException e)
        {
            System.out.println("Unexpected I/O error: " +
                                   e.getMessage());
        }
    }
}
