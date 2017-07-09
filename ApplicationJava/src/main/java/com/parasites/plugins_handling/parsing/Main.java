package com.parasites.plugins_handling.parsing;


import com.parasites.ParasitesPlugin;

import java.util.List;

/**
 * Created by Thomas Ecalle on 08/07/2017.
 */
public class Main
{
    public static void main(String[] args)
    {
        try
        {
            PluginFinder pluginFinder = new PluginFinder();
            pluginFinder.searchForPlugins();
            List<ParasitesPlugin> pluginCollection = pluginFinder.getPluginCollection();
            for (ParasitesPlugin plugin : pluginCollection)
            {
                System.out.println("Found " + plugin.getName());
                plugin.perform();
            }
        } catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
