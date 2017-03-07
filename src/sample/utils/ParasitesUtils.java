package sample.utils;


import sample.Constants;
import sample.engine.board.Board;

import java.net.URISyntaxException;

import static sample.Constants.ANSI_RESET;

/**
 * Created by Thomas Ecalle on 24/02/2017.
 */
public class ParasitesUtils
{
    /**
     * get the image's path, having its name
     *
     * @param imageName
     * @param myClass
     * @return
     */
    public static String getImageUrl(String imageName, Class myClass)
    {
        try
        {
            return myClass.getResource("../resources/" + imageName).toURI().toString();
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Test if the Tile is on the board
     *
     * @param position
     * @return
     */
    public static boolean isValidTile(final int position)
    {
        return (position >= 0 && position < Board.DIMENSION * Board.DIMENSION);
    }


    public static void logError(final String string)
    {
        System.out.println(Constants.ANSI_RED + string + ANSI_RESET);
    }

    public static void logInfos(final String string)
    {
        System.out.println(Constants.ANSI_GREEN + string + ANSI_RESET);
    }

    public static void logWarnings(final String string)
    {
        System.out.println(Constants.ANSI_YELLOW + string + ANSI_RESET);
    }

}
