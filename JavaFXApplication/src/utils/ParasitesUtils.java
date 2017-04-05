package JavaFXApplication.src.utils;


import JavaFXApplication.src.Constants;
import JavaFXApplication.src.engine.board.Board;

import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by Thomas Ecalle on 24/02/2017.
 */
public class ParasitesUtils
{
    /**
     * get the resource's path, having its name
     *
     * @param resourceName
     * @param myClass
     * @return
     */
    public static String getResourceUrl(final String resourceName, final Class myClass)
    {
        try
        {
            if (myClass == null) ParasitesUtils.logWarnings("class == null");
            return myClass.getResource("../resources/" + resourceName).toURI().toString();
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
        System.out.println(Constants.ANSI_RED + string + Constants.ANSI_RESET);
    }

    public static void logInfos(final String string)
    {
        System.out.println(Constants.ANSI_GREEN + string + Constants.ANSI_RESET);
    }

    public static void logWarnings(final String string)
    {
        System.out.println(Constants.ANSI_YELLOW + string + Constants.ANSI_RESET);
    }

    public static void logList(List<?> list)
    {
        if (list != null)
        {
            logWarnings("List beginning");
            for (int i = 0; i < list.size(); i++)
            {
                logInfos(i + ") " + list.get(i));
            }
            logWarnings("List beginning");
        }
    }

    public static void logArray(boolean[] array)
    {
        int counter = 0;
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < Board.DIMENSION; i++)
        {
            for (int j = 0; j < Board.DIMENSION; j++)
            {
                builder.append(String.format("%7s", array[counter]));
                counter++;
            }
            builder.append("\n");
        }
        ParasitesUtils.logError(builder.toString());
    }

}
