package com.parasites.network;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parasites.Constants;
import com.parasites.network.bo.ChatMessage;
import com.parasites.network.bo.Game;
import com.parasites.network.bo.User;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Thomas Ecalle on 24/04/2017.
 */
public final class OnlineServerManager
{
    // Calls to server
    private static final String DISCONNECT = "disconnect";
    private static final String CREATE_GAME = "createGame";
    private static final String LEAVE_GAME = "leaveGame";
    private static final String JOIN_GAME = "joinGame";
    private static final String LAUNCH_GAME = "launchGame";
    private static final String SEND_GAME_CHAT_MESSAGE = "sendGameChatMessage";


    //Events from server
    private static final String UPDATE_SERVER = "updateServer";
    private static final String PERSONAL_GAME_CREATION = "personalGameCreation";
    private static final String CURRENT_SERVER_STATE = "currentServerState";
    private static final String LAUNCHING_GAME = "launchingGame";
    private static final String RECEIVING_GAME_CHAT_MESSAGE = "receivingGameChatMessage";


    private List<OnlineServerObservable> observers;
    private User currentUser;
    private Game currentGame;
    private List<Game> gameList;
    private List<User> userList;

    private Socket socket;
    private Gson gson;
    private static volatile OnlineServerManager instance;

    /**
     * Singleton Pattern
     * We accept the "out-of-order writes" case
     *
     * @return
     */
    public static OnlineServerManager getInstance()
    {
        if (instance == null)
        {
            synchronized (OnlineServerManager.class)
            {
                if (instance == null)
                {
                    instance = new OnlineServerManager();
                }
            }
        }

        return instance;
    }

    private OnlineServerManager()
    {
        observers = new ArrayList<>();
        gameList = new ArrayList<>();
        userList = new ArrayList<>();
        gson = new Gson();
    }

    public void addObserver(OnlineServerObservable onlineServerObservable)
    {
        observers.add(onlineServerObservable);
    }


    /*******************************************
     *                                         *
     *              Server calls               *
     *                                         *
     *******************************************/

    /**
     * Connect Player to online server
     *
     * @param user
     */
    public void connect(final User user)
    {
        currentUser = user;

        notifyServerConnectionStarting();

        try
        {
            final IO.Options options = new IO.Options();
            options.query = "register=" + gson.toJson(user);

            socket = IO.socket(Constants.ONLINE_SERVER_URL, options);

            serverCallbackHandler(socket);
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }

        socket.connect();
    }

    /**
     * Ask the server to create a game
     *
     * @param game
     */
    public void createGame(final Game game)
    {
        currentGame = game;
        socket.emit(OnlineServerManager.CREATE_GAME, gson.toJson(game));
    }

    public void leaveGame()
    {
        currentGame = null;
        socket.emit(OnlineServerManager.LEAVE_GAME);
    }

    public void joinGame(final Game game)
    {
        currentGame = game;
        socket.emit(OnlineServerManager.JOIN_GAME, gson.toJson(game));
    }

    public void launchGame()
    {
        socket.emit(OnlineServerManager.LAUNCH_GAME);
    }

    public void sendGameChatMessage(final String message)
    {
        final ChatMessage chatMessage = new ChatMessage(currentUser, message);
        socket.emit(OnlineServerManager.SEND_GAME_CHAT_MESSAGE, gson.toJson(chatMessage));
    }


    /*******************************************
     *                                         *
     *        Observers notifications          *
     *                                         *
     *******************************************/

    private void notifyServerConnectionStarting()
    {
        for (OnlineServerObservable observer : observers)
        {
            observer.onServerConnectionStart();
        }
    }

    private void notifyConnectionEnd(final boolean success)
    {
        for (OnlineServerObservable observer : observers)
        {
            observer.onServerConnectionEnd(success);
        }
    }


    /*******************************************
     *                                         *
     *        Getters and Setters              *
     *                                         *
     *******************************************/
    public List<Game> getGameList()
    {
        return gameList;
    }

    public List<User> getUserList()
    {
        return userList;
    }

    public User getCurrentUser()
    {
        return currentUser;
    }

    public Game getCurrentGame()
    {
        return currentGame;
    }

    /*******************************************
     *                                         *
     *               Callbacks                 *
     *                                         *
     *******************************************/

    /**
     * This methos implements all the Server callbacks
     *
     * @param socket
     */
    private void serverCallbackHandler(final Socket socket)
    {
        socket.on(Socket.EVENT_CONNECT, objects -> notifyConnectionEnd(true));
        socket.on(Socket.EVENT_CONNECT_ERROR, objects -> notifyConnectionEnd(false));

        socket.on(OnlineServerManager.UPDATE_SERVER, objects ->
        {
            for (OnlineServerObservable observer : observers)
            {
                observer.onMessageFromServer(String.valueOf(objects[0]));
            }
        });

        socket.on(OnlineServerManager.CURRENT_SERVER_STATE, objects ->
        {
            userList.clear();
            Type listType = new TypeToken<HashMap<String, User>>()
            {
            }.getType();
            final HashMap<String, User> listOfPlayers = new Gson().fromJson(String.valueOf(objects[0]), listType);
            final List<User> tempUser = new ArrayList<>();
            for (String s : listOfPlayers.keySet())
            {
                tempUser.add(listOfPlayers.get(s));
            }
            userList.addAll(tempUser);


            gameList.clear();
            listType = new TypeToken<HashMap<String, Game>>()
            {
            }.getType();
            final HashMap<String, Game> myList = new Gson().fromJson(String.valueOf(objects[1]), listType);

            final List<Game> tempGame = new ArrayList<>();
            for (String s : myList.keySet())
            {
                tempGame.add(myList.get(s));
            }
            gameList.addAll(tempGame);

            for (OnlineServerObservable observer : observers)
            {
                observer.onServerStateChange(userList, gameList);
            }

        });

        socket.on(OnlineServerManager.LAUNCHING_GAME, objects ->
        {
            for (OnlineServerObservable observer : observers)
            {
                observer.onLaunchingGame();
            }
        });

        socket.on(OnlineServerManager.RECEIVING_GAME_CHAT_MESSAGE, objects ->
        {
            final ChatMessage chatMessage = gson.fromJson(String.valueOf(objects[0]), ChatMessage.class);
            for (OnlineServerObservable observer : observers)
            {
                observer.onReceivingGameMessage(chatMessage);
            }
        });
    }
}
