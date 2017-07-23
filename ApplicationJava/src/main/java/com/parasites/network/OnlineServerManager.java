package com.parasites.network;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parasites.Constants;
import com.parasites.engine.pieces.KindOfParasite;
import com.parasites.engine.players.Player;
import com.parasites.network.bo.ChatMessage;
import com.parasites.network.bo.Game;
import com.parasites.network.bo.User;
import com.parasites.network.interfaces.GameObserver;
import com.parasites.network.interfaces.Observer;
import com.parasites.network.interfaces.OnlineServerObserver;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Thomas Ecalle on 24/04/2017.
 */
public final class OnlineServerManager
{
    // Calls to server
    private static final String SAVE_TURN = "saveTurn";
    private static final String CREATE_GAME = "createGame";
    private static final String LEAVE_GAME = "leaveGame";
    private static final String JOIN_GAME = "joinGame";
    private static final String LAUNCH_GAME = "launchGame";
    private static final String SEND_GAME_CHAT_MESSAGE = "sendGameChatMessage";
    private static final String PLAY_MOVE = "playMove";
    private static final String PASS_TURN = "passTurn";
    private static final String WINNER = "winner";


    //Events from server
    private static final String UPDATE_SERVER = "updateServer";
    private static final String RECEIVING_GAME_MOVE = "playMove";
    private static final String CURRENT_SERVER_STATE = "currentServerState";
    private static final String LAUNCHING_GAME = "launchingGame";
    private static final String RECEIVING_GAME_CHAT_MESSAGE = "receivingGameChatMessage";
    private static final String RECEIVING_PASS_TURN = "passTurn";
    private static final String RECEIVING_WINNER = "winner";


    private List<Observer> observers;
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

    public void addObserver(Observer observer)
    {
        observers.add(observer);
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


    public void playMove(final int tileCoordonate, final boolean isTileLocked, final KindOfParasite kindOfParasite)
    {
        System.out.println("sent move : " + tileCoordonate + ", " + isTileLocked + ", " + kindOfParasite);


        socket.emit(OnlineServerManager.PLAY_MOVE, tileCoordonate, isTileLocked, kindOfParasite);
    }

    public void saveTurn(final String turn)
    {
        System.out.println("saving turn : " + turn);

        socket.emit(OnlineServerManager.SAVE_TURN, turn);
    }

    public void passTurn()
    {
        socket.emit(OnlineServerManager.PASS_TURN);
    }

    public void setWinner(final Player winner)
    {
        socket.emit(OnlineServerManager.WINNER, winner.getId());
    }

    public void disconnect()
    {
        if (socket !=  null && socket.connected())
            socket.disconnect();
    }

    /*******************************************
     *                                         *
     *        Observers notifications          *
     *                                         *
     *******************************************/

    private void notifyServerConnectionStarting()
    {
        for (Observer observer : observers)
        {
            if (observer instanceof OnlineServerObserver)
            {
                ((OnlineServerObserver) observer).onServerConnectionStart();
            }
        }
    }

    private void notifyConnectionEnd(final boolean success)
    {
        for (Observer observer : observers)
        {
            if (observer instanceof OnlineServerObserver)
            {

                ((OnlineServerObserver) observer).onServerConnectionEnd(success);
            }
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
            for (Observer observer : observers)
            {
                if (observer instanceof OnlineServerObserver)
                {

                    ((OnlineServerObserver) observer).onMessageFromServer(String.valueOf(objects[0]));
                }
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
                if (currentGame != null && myList.get(s).getId() == currentGame.getId())
                {
                    currentGame = myList.get(s);
                }
            }
            gameList.addAll(tempGame);

            for (Observer observer : observers)
            {
                if (observer instanceof OnlineServerObserver)
                {

                    ((OnlineServerObserver) observer).onServerStateChange(userList, gameList);
                }
            }

        });

        socket.on(OnlineServerManager.LAUNCHING_GAME, objects ->
        {
            for (Observer observer : observers)
            {
                if (observer instanceof OnlineServerObserver)
                {

                    ((OnlineServerObserver) observer).onLaunchingGame();
                }
            }
        });

        socket.on(OnlineServerManager.RECEIVING_GAME_CHAT_MESSAGE, objects ->
        {
            final ChatMessage chatMessage = gson.fromJson(String.valueOf(objects[0]), ChatMessage.class);


            for (Observer observer : observers)
            {
                if (observer instanceof OnlineServerObserver)
                {

                    ((OnlineServerObserver) observer).onReceivingGameMessage(chatMessage);
                }
            }
        });

        socket.on(OnlineServerManager.RECEIVING_GAME_MOVE, objects ->
        {
            final int origin = (int) objects[0];
            final boolean isTileLocked = (boolean) objects[1];
            KindOfParasite kindOfParasite = null;
            if (objects[2] != null)
            {
                kindOfParasite = KindOfParasite.valueOf((String) objects[2]);
            }

            System.out.println("OnlineServerManager callback GAME MOVE : " + origin + ", " + isTileLocked + ", " + kindOfParasite);


            final List<Observer> copiedList = Collections.synchronizedList(observers);
            for (Observer observer : copiedList)
            {
                if (observer instanceof GameObserver)
                {
                    ((GameObserver) observer).onMovePlayed(origin, isTileLocked, kindOfParasite);
                }
            }
        });
        socket.on(OnlineServerManager.RECEIVING_PASS_TURN, objects ->
        {

            System.out.println("OnlineServerManager callback PASS TURN");

            final List<Observer> copiedList = Collections.synchronizedList(observers);
            for (Observer observer : copiedList)
            {
                if (observer instanceof GameObserver)
                {
                    ((GameObserver) observer).onTurnPassed();
                }
            }
        });

        socket.on(OnlineServerManager.RECEIVING_WINNER, objects ->
        {
            final String winnerPseudo = (String) objects[0];
            for (Observer observer : observers)
            {
                if (observer instanceof GameObserver)
                {

                    ((GameObserver) observer).onEndGame(winnerPseudo);
                }
            }
        });

    }


}
