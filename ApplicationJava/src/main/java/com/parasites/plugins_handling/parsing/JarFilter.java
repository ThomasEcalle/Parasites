package com.parasites.plugins_handling.parsing;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by Thomas Ecalle on 08/07/2017.
 */
public class JarFilter implements FilenameFilter
{
    @Override
    public boolean accept(File dir, String name)
    {
        return name.endsWith(".jar");
    }
}
