package com.parasites.engine.pieces;

import com.parasites.Constants;

/**
 * Created by Thomas Ecalle on 26/02/2017.
 */
public enum KindOfParasite
{
    BUILDER(2, "BUILDER"),
    COLONY(Constants.INFINITE_VALUE, "COLONY"),
    DEFENDER(2, "DEFENDER"),
    QUEEN(Constants.INFINITE_VALUE, "QUEEN"),
    WARRIOR(3, "WARRIOR");

    public int cost;
    public String name;

    KindOfParasite(int cost, String name)
    {
        this.cost = cost;
        this.name = name;
    }

}
