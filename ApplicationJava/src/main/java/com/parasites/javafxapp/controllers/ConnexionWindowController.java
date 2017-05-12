package com.parasites.javafxapp.controllers;

import com.parasites.javafxapp.models.ConnectionAPICall;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;

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
    @FXML
    private Label error_label;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        error_label.setVisible(false);
    }

    @FXML
    public void clickOnValider(){
        ConnectionAPICall cac = new ConnectionAPICall(pseudo_textfield.getText(), password_textfield.getText());
        if(cac.getCodeResponse() == 200){
            openPrincipalWindow();
        }
        else{
            error_label.setText("Le pseudo ou le mot de passe est invalide.");
            error_label.setVisible(true);
        }
    }

    @FXML
    public void clickOnInscription(){

    }

    @FXML
    private void onTextFieldSelected(){
        error_label.setVisible(false);
    }

    private void openPrincipalWindow(){
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
