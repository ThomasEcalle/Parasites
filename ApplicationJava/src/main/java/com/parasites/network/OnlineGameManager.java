package com.parasites.network;

import com.google.gson.Gson;
import com.parasites.Constants;
import com.parasites.engine.Game;
import com.parasites.engine.players.Player;
import com.parasites.utils.ParasitesUtils;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas Ecalle on 24/04/2017.
 */
public class OnlineGameManager
{
    // Calls to server
    public static final String REGISTER = "register";
    public static final String CREATE_GAME = "createGame";

    //Events from server
    public static final String UPDATE_SERVER = "updateServer";
    public static final String UPDATE_GAMES = "updateGames";

    private List<OnlineServerObservable> onlineServerObservables;
    private List<Game> gameList;

    private Socket socket;
    private static volatile OnlineGameManager instance;

    // We accept the "out-of-order writes" case
    public static OnlineGameManager getInstance()
    {
        if (instance == null)
        {
            synchronized (OnlineGameManager.class)
            {
                if (instance == null)
                {
                    instance = new OnlineGameManager();
                }
            }
        }

        return instance;
    }

    private OnlineGameManager()
    {
        onlineServerObservables = new ArrayList<>();
        gameList = new ArrayList<>();
        try
        {
            socket = IO.socket(Constants.ONLINE_SERVER_URL);
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }

        socket.on(Socket.EVENT_CONNECT, objects -> ParasitesUtils.logInfos("Well connected to online server"));

        socket.on(OnlineGameManager.UPDATE_SERVER, objects -> ParasitesUtils.logWarnings((String) objects[0] + " " + objects[1]));
        socket.on(OnlineGameManager.UPDATE_GAMES, new Emitter.Listener()
        {
            @Override
            public void call(Object... objects)
            {

                ParasitesUtils.logWarnings(objects[0] + " \n***********\n" + objects[1]);
                notifyObservers();
            }
        });
    }


    public void connect(final Player player)
    {
        socket.connect();
        socket.emit(OnlineGameManager.REGISTER, player.toJSON());
    }

    public void createGame(final Game game)
    {
        final Gson gson = new Gson();
        socket.emit(OnlineGameManager.CREATE_GAME, gson.toJson(game));
    }

    public void addObserver(OnlineServerObservable onlineServerObservable)
    {
        onlineServerObservables.add(onlineServerObservable);
    }

    public void removeObserver(OnlineServerObservable onlineServerObservable)
    {
        onlineServerObservables.remove(onlineServerObservable);
    }

    private void notifyObservers()
    {
        for (OnlineServerObservable observer : onlineServerObservables)
        {
            observer.actualize();
        }
    }
}
