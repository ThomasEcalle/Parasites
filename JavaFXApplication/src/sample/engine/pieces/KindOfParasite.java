package sample.engine.pieces;

import sample.Constants;

/**
 * Created by Thomas Ecalle on 26/02/2017.
 */
public enum KindOfParasite
{
    BUILDER(2),
    COLONY(Constants.INFINITE_VALUE),
    DEFENDER(2),
    QUEEN(Constants.INFINITE_VALUE),
    WARRIOR(3);

    public int cost;

    KindOfParasite(int cost)
    {
        this.cost = cost;
    }

}
