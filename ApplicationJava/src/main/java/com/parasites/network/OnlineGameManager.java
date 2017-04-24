package com.parasites.network;

import com.parasites.engine.players.Player;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by Thomas Ecalle on 24/04/2017.
 */
public class OnlineGameManager
{

    public final static String URL = "http://localhost:3880";
    private List<Player> opponents;
    private Player player;
    private Socket socket;
    private static volatile OnlineGameManager instance;

    // We accept the "out-of-order writes" case
    public static OnlineGameManager getInstance(final Player player, final List<Player> opponents)
    {
        if (instance == null)
        {
            synchronized (OnlineGameManager.class)
            {
                if (instance == null)
                {
                    instance = new OnlineGameManager(player, opponents);
                }
            }
        }

        return instance;
    }

    private OnlineGameManager(final Player player, final List<Player> opponents)
    {
        this.player = player;
        this.opponents = opponents;

        try
        {
            socket = IO.socket(OnlineGameManager.URL);
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
        socket.connect();

        socket.on(Socket.EVENT_CONNECT, objects -> socket.emit("declaring_identity", player.toJSON()));

        socket.on(Socket.EVENT_MESSAGE, objects ->
        {
            final String message = (String) objects[0];
            System.out.println("Message received from server : " + message);
        });
    }

}
