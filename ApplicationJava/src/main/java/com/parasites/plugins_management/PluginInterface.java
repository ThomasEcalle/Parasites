package com.parasites.plugins_management;

/**
 * Created by Thomas Ecalle on 02/07/2017.
 */
public interface PluginInterface
{
    void setParameter(int var1);

    int getResult();

    String getPluginName();

    boolean hasError();
}
