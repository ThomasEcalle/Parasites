package com.parasites.controllers;

import com.parasites.annotations.PressEnter;
import com.parasites.models.ConnectionAPICall;
import com.parasites.network.OnlineServerManager;
import com.parasites.network.bo.ChatMessage;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public final class ConnexionWindowController extends ParasitesFXController implements Initializable
{

    @FXML
    @PressEnter("clickOnValider")
    private TextField pseudo_textfield;
    @FXML
    @PressEnter("clickOnValider")
    private PasswordField password_textfield;
    @FXML
    private Button validate_button;
    @FXML
    private Button inscription_button;
    @FXML
    private Hyperlink forgotten_password;
    @FXML
    private Hyperlink website;
    @FXML
    private Hyperlink contact;
    @FXML
    private Label error_label;
    private String token;
    private final String siteRoot = "http://127.0.0.1:3080/";

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        OnlineServerManager.getInstance().addObserver(this);
        error_label.setVisible(false);
        super.parseAnnotations(this);
    }

    @FXML
    public void clickOnValider()
    {
        ConnectionAPICall cac = new ConnectionAPICall(pseudo_textfield.getText(), password_textfield.getText());
        if (cac.getCodeResponse() == 200)
        {
            token = cac.getToken();
            //prevent from double connection id double clicked
            validate_button.setDisable(true);


            final User user = cac.getUser();

            user.setToken(token);

            //Connecting online
            OnlineServerManager.getInstance().connect(user);

        } else
        {
            error_label.setText("Le pseudo ou le mot de passe est invalide.");
            error_label.setVisible(true);
        }
    }

    @FXML
    public void clickOnWebSite()
    {
        openWebpage(siteRoot + "accueil");
    }

    @FXML
    public void clickOnInscription()
    {
        openWebpage(siteRoot + "accueil/inscription");
    }

    @FXML
    public void clickOnContact()
    {
        openWebpage(siteRoot + "contact");
    }

    @FXML
    public void clickOnReset()
    {
        openWebpage(siteRoot + "accueil/reinit");
    }

    public void openWebpage(String urlString) {
        try {
            Desktop.getDesktop().browse(new URL(urlString).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ecran_principal.fxml"));
        try
        {
            root = loader.load();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        PrincipalWindowController controller = loader.getController();
        controller.setStage(stage);
        controller.setToken(token);

        stage.setTitle("Parasites");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("game_ico.png")));
        stage.show();
        ((Stage) validate_button.getScene().getWindow()).close();
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
            ParasitesUtils.logInfos("Weell connected as : " + OnlineServerManager.getInstance().getCurrentUser().getPseudo());
            Platform.runLater(() -> openPrincipalWindow());
        } else
        {
            setTextInLabel(error_label, "Connexion au serveur multijoueur impossible.");


            validate_button.setDisable(false);
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

    @Override
    public void onLaunchingGame()
    {
        // NO utility here.
    }

    @Override
    public void onReceivingGameMessage(final ChatMessage chatMessage)
    {
        /// No utility here.
    }


}
