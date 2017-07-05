package com.parasites.network.interfaces;

import com.parasites.engine.pieces.KindOfParasite;

/**
 * Created by Thomas Ecalle on 01/07/2017.
 */
public interface GameObserver extends Observer
{
    /**
     * When a player has played, we got all the needed informations.
     *
     * @param destination
     * @param isTileLocked
     * @param kindOfParasite
     */
    void onMovePlayed(final int destination, final boolean isTileLocked, final KindOfParasite kindOfParasite);

    /**
     * When a player pass his turn
     */
    void onTurnPassed();
}
