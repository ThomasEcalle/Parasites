package com.parasites.plugins_parsing;


import com.parasites.ParasitesPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Thomas Ecalle on 08/07/2017.
 */
public class PluginFinder
{
    // Parameters
    private static final Class[] parameters = new Class[]{URL.class};

    private List<ParasitesPlugin> pluginCollection;

    public PluginFinder()
    {
        pluginCollection = new ArrayList<ParasitesPlugin>(5);
    }

    public void searchForPlugins() throws Exception
    {
        File dir = new File(System.getProperty("user.dir") + "/plugins");
        if (dir.isFile())
        {
            return;
        }
        File[] files = dir.listFiles(new JarFilter());
        for (File f : files)
        {
            List<String> classNames = getClassNames(f.getAbsolutePath());
            for (String className : classNames)
            {
                // Remove the “.class” at the back
                Class clazz = getClass(f, className);
                Class[] interfaces = clazz.getInterfaces();
                for (Class c : interfaces)
                {
                    // Implement the IPlugin interface
                    if (c.getName().equals("com.parasites.ParasitesPlugin"))
                    {
                        pluginCollection.add((ParasitesPlugin) clazz.newInstance());
                    }
                }
            }
        }
    }

    protected List<String> getClassNames(String jarName) throws IOException
    {
        List<String> classNames = new ArrayList<String>();
        ZipInputStream zip = new ZipInputStream(new FileInputStream(jarName));
        for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry())
        {
            if (!entry.isDirectory() && entry.getName().endsWith(".class"))
            {
                // This ZipEntry represents a class. Now, what class does it represent?
                String className = entry.getName().replace('/', '.'); // including ".class"
                classNames.add(className.substring(0, className.length() - ".class".length()));
            }
        }

        return classNames;
    }

    public Class getClass(File file, String name) throws Exception
    {
        addURL(file.toURL());

        URLClassLoader clazzLoader;
        Class clazz;
        String filePath = file.getAbsolutePath();
        filePath = "jar:file://" + filePath + "!/";
        URL url = new File(filePath).toURL();
        clazzLoader = new URLClassLoader(new URL[]{url});
        clazz = clazzLoader.loadClass(name);
        return clazz;

    }

    public void addURL(URL u) throws IOException
    {
        URLClassLoader sysLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        URL urls[] = sysLoader.getURLs();
        for (int i = 0; i < urls.length; i++)
        {
            if (urls[i].toString().equalsIgnoreCase(u.toString()))
            {
                return;
            }
        }
        Class sysclass = URLClassLoader.class;
        try
        {
            Method method = sysclass.getDeclaredMethod("addURL", parameters);
            method.setAccessible(true);
            method.invoke(sysLoader, new Object[]{u});
        } catch (Throwable t)
        {
            t.printStackTrace();
            throw new IOException("Error, could not add URL to system classloader");
        }
    }

    public List<ParasitesPlugin> getPluginCollection()
    {
        return pluginCollection;
    }

    public void setPluginCollection(List<ParasitesPlugin> pluginCollection)
    {
        this.pluginCollection = pluginCollection;
    }
}
