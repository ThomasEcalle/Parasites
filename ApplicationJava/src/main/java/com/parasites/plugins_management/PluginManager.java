package com.parasites.plugins_management;


import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Thomas Ecalle on 02/07/2017.
 */
public class PluginManager
{
    String pluginsDir;
    List<PluginInterface> plugins;

    public static void main(String[] args)
    {
        PluginManager pluginManager = new PluginManager(args);
        pluginManager.getPlugins();
        pluginManager.runPlugins();
    }

    public PluginManager(String[] args)
    {
        if (args.length > 0)
        {
            this.pluginsDir = args[0];
        } else
        {
            this.pluginsDir = "plugins";
        }

        this.plugins = new ArrayList();
        System.setSecurityManager(new PluginSecurityManager(this.pluginsDir));
    }

    protected void getPlugins()
    {
        File file = new File(System.getProperty("user.dir") + File.separator + this.pluginsDir);
        PluginClassLoader var2 = new PluginClassLoader(file);
        if (file.exists() && file.isDirectory())
        {
            String[] files = file.list();

            for (int i = 0; i < files.length; ++i)
            {
                try
                {
                    if (files[i].endsWith(".class"))
                    {
                        Class mClass = var2.loadClass(files[i].substring(0, files[i].indexOf(".")));
                        Class[] interfaces = mClass.getInterfaces();

                        for (int j = 0; j < interfaces.length; ++j)
                        {
                            if (interfaces[j].getName().equals("PluginFunction"))
                            {
                                PluginInterface pluginInterface = (PluginInterface) mClass.newInstance();
                                this.plugins.add(pluginInterface);
                            }
                        }
                    }
                } catch (Exception var9)
                {
                    System.err.println("File " + files[i] + " does not contain a valid PluginFunction class.");
                }
            }
        }

    }

    protected void runPlugins()
    {
        int count = 1;
        Iterator iterator = this.plugins.iterator();

        while (iterator.hasNext())
        {
            PluginInterface pluginInterface = (PluginInterface) iterator.next();

            try
            {
                pluginInterface.setParameter(count);
                System.out.print(pluginInterface.getPluginName());
                System.out.print(" ( " + count + " ) = ");
                if (pluginInterface.hasError())
                {
                    System.out.println("there was an error during plugin initialization");
                } else
                {
                    int result = pluginInterface.getResult();
                    if (pluginInterface.hasError())
                    {
                        System.out.println("there was an error during plugin execution");
                    } else
                    {
                        System.out.println(result);
                    }

                    ++count;
                }
            } catch (SecurityException var5)
            {
                System.err.println("plugin \'" + pluginInterface.getClass().getName() + "\' tried to do something illegal");
            }
        }

    }
}
