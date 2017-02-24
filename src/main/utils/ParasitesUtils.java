package main.utils;


import java.net.URISyntaxException;

/**
 * Created by My-PC on 24/02/2017.
 */
public class ParasitesUtils
{
    public static String getImageUrl (String imageName, Class myClass)
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
}
