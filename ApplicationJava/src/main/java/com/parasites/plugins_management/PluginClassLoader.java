package com.parasites.plugins_management;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by Thomas Ecalle on 02/07/2017.
 */
public class PluginClassLoader extends ClassLoader
{
    File directory;

    public PluginClassLoader(File directory)
    {
        this.directory = directory;
    }

    public Class loadClass(String className) throws ClassNotFoundException
    {
        return this.loadClass(className, true);
    }

    public Class loadClass(String className, boolean var2) throws ClassNotFoundException
    {
        try
        {
            Class mClass = this.findLoadedClass(className);
            if (mClass == null)
            {
                try
                {
                    mClass = this.findSystemClass(className);
                } catch (Exception e)
                {
                    System.out.println(e.toString());
                }
            }

            if (mClass == null)
            {
                String newName = className.replace('.', File.separatorChar) + ".class";
                File file = new File(this.directory, newName);
                int fileLength = (int) file.length();
                byte[] tab = new byte[fileLength];
                DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
                dataInputStream.readFully(tab);
                dataInputStream.close();
                mClass = this.defineClass(className, tab, 0, fileLength);
            }

            if (var2)
            {
                this.resolveClass(mClass);
            }

            return mClass;
        } catch (Exception e)
        {
            throw new ClassNotFoundException(e.toString());
        }
    }
}
