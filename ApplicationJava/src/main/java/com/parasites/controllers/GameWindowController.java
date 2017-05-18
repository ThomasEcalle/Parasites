package com.parasites.controllers;

import com.parasites.annotations.PressEnter;
import com.parasites.engine.board.Board;
import com.parasites.engine.pieces.Builder;
import com.parasites.engine.pieces.Defender;
import com.parasites.engine.pieces.Warrior;
import com.parasites.engine.players.Player;
import com.parasites.network.OnlineServerManager;
import com.parasites.network.bo.ChatMessage;
import com.parasites.network.bo.Game;
import com.parasites.network.bo.User;
import com.parasites.utils.AnimatedZoomOperator;
import com.parasites.view.GraphicBoard;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public final class GameWindowController extends ParasitesFXController implements Initializable
{


    @FXML
    private Button back_button;
    @FXML
    private Button validate_button;
    @FXML
    private Button forward_button;
    @FXML
    @PressEnter("sendChat")
    private TextField chat_textfield;
    @FXML
    private Button chat_button;
    @FXML
    private ListView chat_listview;
    @FXML
    private TableColumn game_tablecolumn;
    @FXML
    private TableColumn friends_tablecolumn;
    @FXML
    private BorderPane borderPane;
    @FXML
    private ImageView builderImage;
    @FXML
    private ImageView defenderImage;
    @FXML
    private ImageView attackerImage;

    private GraphicBoard graphicBoard;
    private Board board;

    private List<ChatMessage> gameMessages;
    private List<User> userList;


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        OnlineServerManager.getInstance().addObserver(this);
        gameMessages = new ArrayList<>();
        userList = new ArrayList<>();


        showGame();
        setImagesClickListeners();

        super.parseAnnotations(this);
    }

    @FXML
    public void sendChat()
    {
        final String message = chat_textfield.getText();
        chat_textfield.clear();
        OnlineServerManager.getInstance().sendGameChatMessage(message);
    }


    /*******************************************
     *                                         *
     *             Server Life cycle           *
     *                                         *
     *******************************************/


    @Override
    public void onServerConnectionStart()
    {

    }

    @Override
    public void onServerConnectionEnd(boolean success)
    {

    }

    @Override
    public void onMessageFromServer(String message)
    {

    }

    @Override
    public void onServerStateChange(final List<User> users, final List<Game> games)
    {
        updateListOfUsers(users);
    }

    @Override
    public void onLaunchingGame()
    {

    }

    @Override
    public void onReceivingGameMessage(final ChatMessage chatMessage)
    {
        gameMessages.add(chatMessage);
        updateChat(gameMessages);
    }

    private void updateChat(final List<ChatMessage> messages)
    {
        refreshListView(messages, chat_listview);
    }

    private void updateListOfUsers(final List<User> users)
    {
        userList.clear();
        userList.addAll(users);
    }

    private void showGame()
    {
        final List<Player> playerList = new ArrayList<>();
        final Player currentPlayer = new Player(OnlineServerManager.getInstance().getCurrentUser().getPseudo(), Color.BROWN);

        final Game currentGame = OnlineServerManager.getInstance().getCurrentGame();
        final List<User> list = currentGame.getPlayersList();
        final int dimension = currentGame.getSize();

        for (final User user : list)
        {
            playerList.add(new Player(user.getPseudo(), Color.BROWN));
        }


        this.board = Board.createInitialBoard(currentPlayer, dimension, playerList);

//        graphicBoard = new GraphicBoard(board);
//
//        graphicBoard.setAlignment(Pos.CENTER);
//
//        borderPane.setCenter(graphicBoard);

        borderPane.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                System.out.println("okkokok sanchy  ");
            }
        });

        final AnimatedZoomOperator zoomOperator = new AnimatedZoomOperator();

        borderPane.setStyle("-fx-stroke: green;-fx-stroke-width: 5;");
        borderPane.setOnScroll((event) ->
        {
            double zoomFactor = 1.5;
            if (event.getDeltaY() <= 0)
            {
                // zoom out
                zoomFactor = 1 / zoomFactor;
            }
            zoomOperator.zoom(borderPane, zoomFactor, event.getSceneX(), event.getSceneY());

        });
    }

    private void setImagesClickListeners()
    {
        attackerImage.setOnMouseClicked((event) ->
        {
            if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED))
            {
                board.chosenParasite = new Warrior(-1, board.getCurrentPlayer());
            }
        });
        defenderImage.setOnMouseClicked((event) ->
        {
            if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED))
            {
                board.chosenParasite = new Defender(-1, board.getCurrentPlayer());
            }
        });

        builderImage.setOnMouseClicked((event) ->
        {
            if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED))
            {
                board.chosenParasite = new Builder(-1, board.getCurrentPlayer());
            }
        });
    }

}
