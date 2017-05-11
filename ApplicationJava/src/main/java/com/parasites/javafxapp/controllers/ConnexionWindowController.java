package com.parasites.javafxapp.controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.parasites.javafxapp.models.ConnectionAPICall;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ConnexionWindowController implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void clickOnValider(){
        ConnectionAPICall cac = new ConnectionAPICall(pseudo_textfield.getText(), password_textfield.getText());
        if(cac.getCodeResponse() == 200){
            Stage stage = new Stage();
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getClassLoader().getResource("ecran_principal.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setTitle("Parasites");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("game_ico.png")));
            stage.show();
            ((Stage)valider_button.getScene().getWindow()).close();
        }
    }
}
