package com.parasites.network;

import com.parasites.network.bo.ChatMessage;
import com.parasites.network.bo.Game;
import com.parasites.network.bo.User;

import java.util.List;

/**
 * Created by Thomas Ecalle on 12/05/2017.
 */
public interface OnlineServerObservable
{
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

    /**
     * Method called when the game is launched
     */
    void onLaunchingGame();

    /**
     * When someone from the game sent a message
     *
     * @param chatMessage
     */
    void onReceivingGameMessage(final ChatMessage chatMessage);
}
