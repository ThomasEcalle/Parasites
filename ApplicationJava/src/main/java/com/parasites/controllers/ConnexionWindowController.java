package com.parasites.controllers;

import com.parasites.models.ConnectionAPICall;
import com.parasites.network.OnlineServerManager;
import com.parasites.network.bo.Game;
import com.parasites.network.bo.User;
import com.parasites.utils.ParasitesUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ConnexionWindowController extends ParasitesFXController implements Initializable
{

    @FXML
    private TextField pseudo_textfield;
    @FXML
    private PasswordField password_textfield;
    @FXML
    private Button valider_button;
    @FXML
    private Button inscription_button;
    @FXML
    private Hyperlink forgotten_password;
    @FXML
    private Hyperlink site;
    @FXML
    private Hyperlink contact;
    @FXML
    private Label error_label;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        OnlineServerManager.getInstance().addObserver(this);
        error_label.setVisible(false);
    }

    @FXML
    public void clickOnValider()
    {
        ConnectionAPICall cac = new ConnectionAPICall(pseudo_textfield.getText(), password_textfield.getText());
        if (cac.getCodeResponse() == 200)
        {
            //prevent from double connection id double clicked
            valider_button.setDisable(true);

            final User user = new User(pseudo_textfield.getText());

            //Connecting online
            OnlineServerManager.getInstance().connect(user);

        } else
        {
            error_label.setText("Le pseudo ou le mot de passe est invalide.");
            error_label.setVisible(true);
        }
    }

    @FXML
    public void clickOnInscription()
    {

    }

    @FXML
    private void onTextFieldSelected()
    {
        error_label.setVisible(false);
    }

    private void openPrincipalWindow()
    {
        Stage stage = new Stage();
        Parent root = null;
        try
        {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("ecran_principal.fxml"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        stage.setTitle("Parasites");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("game_ico.png")));
        stage.show();
        ((Stage) valider_button.getScene().getWindow()).close();
    }

    @Override
    public void onPersonalGameCreation(Game createdGame)
    {
        // No utility here
    }

    @Override
    public void onServerConnectionStart()
    {
        ParasitesUtils.logInfos("En attente de connexion, veuillez patienter...");
    }

    @Override
    public void onServerConnectionEnd(boolean success)
    {
        if (success)
        {
            Platform.runLater(() -> openPrincipalWindow());
        } else
        {
            setTextInLabel(error_label, "Connexion au serveur multijoueur impossible.");


            valider_button.setDisable(false);
        }
    }

    @Override
    public void onMessageFromServer(String message)
    {
        setTextInLabel(error_label, message);
    }

    @Override
    public void onServerStateChange(List<User> users, List<Game> games)
    {
        // NO utility here.
    }
}