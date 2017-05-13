package com.parasites.network;

import com.parasites.network.bo.Game;
import com.parasites.network.bo.User;

import java.util.List;

/**
 * Created by Thomas Ecalle on 12/05/2017.
 */
public interface OnlineServerObservable
{
    /**
     * Method called when the current player create a game
     *
     * @param createdGame
     */
    void onPersonalGameCreation(final Game createdGame);

    /**
     * Callback on connection (display loader)
     */
    void onServerConnectionStart();

    /**
     * Callback on server connection
     *
     * @param success
     */
    void onServerConnectionEnd(final boolean success);


    /**
     * In case of informations sent by server
     *
     * @param message
     */
    void onMessageFromServer(final String message);

    /**
     * Called every time the server state changes
     *
     * @param users
     * @param games
     */
    void onServerStateChange(final List<User> users, final List<Game> games);
}
