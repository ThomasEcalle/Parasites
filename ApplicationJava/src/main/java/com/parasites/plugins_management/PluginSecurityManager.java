package com.parasites.plugins_management;

import java.io.File;
import java.io.FileDescriptor;
import java.net.InetAddress;

/**
 * Created by Thomas Ecalle on 02/07/2017.
 */
public class PluginSecurityManager extends SecurityManager
{
    private String pluginDir = null;

    PluginSecurityManager(String var1)
    {
        this.pluginDir = var1;
    }

    protected void trusted()
    {
        if (this.inClassLoader())
        {
            throw new SecurityException();
        }
    }

    public void checkCreateClassLoader()
    {
        this.trusted();
    }

    public void checkAccess(Thread var1)
    {
        this.trusted();
    }

    public void checkAccess(ThreadGroup var1)
    {
        this.trusted();
    }

    public void checkExit(int var1)
    {
        this.trusted();
    }

    public void checkExec(String var1)
    {
        this.trusted();
    }

    public void checkLink(String var1)
    {
        this.trusted();
    }

    public void checkRead(FileDescriptor var1)
    {
        this.trusted();
    }

    public void checkRead(String var1)
    {
        String var2 = (new File(var1)).getParentFile().getAbsolutePath();
        if (!var2.endsWith(this.pluginDir))
        {
            this.trusted();
        }

    }

    public void checkRead(String var1, Object var2)
    {
        this.trusted();
    }

    public void checkWrite(FileDescriptor var1)
    {
        this.trusted();
    }

    public void checkWrite(String var1)
    {
        this.trusted();
    }

    public void checkDelete(String var1)
    {
        this.trusted();
    }

    public void checkConnect(String var1, int var2)
    {
        this.trusted();
    }

    public void checkConnect(String var1, int var2, Object var3)
    {
        this.trusted();
    }

    public void checkListen(int var1)
    {
        this.trusted();
    }

    public void checkAccept(String var1, int var2)
    {
        this.trusted();
    }

    public void checkMulticast(InetAddress var1)
    {
        this.trusted();
    }

    public void checkMulticast(InetAddress var1, byte var2)
    {
        this.trusted();
    }

    public void checkPropertiesAccess()
    {
        this.trusted();
    }

    public void checkPropertyAccess(String var1)
    {
        if (!var1.equals("user.dir"))
        {
            this.trusted();
        }

    }

    public void checkPrintJobAccess()
    {
        this.trusted();
    }

    public void checkSystemClipboardAccess()
    {
        this.trusted();
    }

    public void checkAwtEventQueueAccess()
    {
        this.trusted();
    }

    public void checkSetFactory()
    {
        this.trusted();
    }

    public void checkMemberAccess(Class var1, int var2)
    {
        this.trusted();
    }

    public void checkSecurityAccess(String var1)
    {
        this.trusted();
    }

    public void checkPackageAccess(String var1)
    {
        if (this.inClassLoader() && !var1.startsWith("java.") && !var1.startsWith("javax."))
        {
            throw new SecurityException();
        }
    }

    public void checkPackageDefinition(String var1)
    {
        if (this.inClassLoader() && (var1.startsWith("java.") || var1.startsWith("javax.") || var1.startsWith("sun.")))
        {
            throw new SecurityException();
        }
    }

    public boolean checkTopLevelWindow(Object var1)
    {
        this.trusted();
        return true;
    }
}
