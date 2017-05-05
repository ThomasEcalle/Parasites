package controllers;

import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
}
