package com.parasites.controllers;

import com.parasites.network.OnlineServerManager;
import com.parasites.network.bo.ChatMessage;
import com.parasites.network.bo.Game;
import com.parasites.network.bo.User;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GameWindowController extends ParasitesFXController implements Initializable
{


    @FXML
    private Button back_button;
    @FXML
    private Button validate_button;
    @FXML
    private Button forward_button;
    @FXML
    private TextField chat_textfield;
    @FXML
    private Button chat_button;
    @FXML
    private ListView chat_listview;
    @FXML
    private TableColumn game_tablecolumn;
    @FXML
    private TableColumn friends_tablecolumn;

    private List<ChatMessage> gameMessages;


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        OnlineServerManager.getInstance().addObserver(this);
        gameMessages = new ArrayList<>();

        chat_textfield.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent keyEvent)
            {
                if (keyEvent.getCode() == KeyCode.ENTER)
                {
                    sendChat();
                }
            }
        });
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
    public void onPersonalGameCreation(Game createdGame)
    {

    }

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
    public void onServerStateChange(List<User> users, List<Game> games)
    {

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


}
