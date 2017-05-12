package com.parasites.javafxapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;

import javax.xml.stream.Location;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by spyro on 11/05/2017.
 */
public class PrincipalWindowController implements Initializable{

    @FXML
    private Tab partie_tab;
    @FXML
    private Tab salon_tab;
    @FXML
    private Button creer_partie;
    @FXML
    private TableView parties_disponibles;
    @FXML
    private TableView membres_partie;
    @FXML
    private Label proprietaire;
    @FXML
    private Label joueurs_max;
    @FXML
    private Label joueurs_actuel;
    @FXML
    private Label nombre_cases;
    @FXML
    private Label creation;
    @FXML
    private Label expiration;
    @FXML
    private Button lancer;
    @FXML
    private Button quitter;
    @FXML
    private Label pseudo_actuel;
    @FXML
    private TextField pseudo;
    @FXML
    private TextField confirmation_pseudo;
    @FXML
    private Label password_actuel;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmation_password;
    @FXML
    private TextField mail;
    @FXML
    private Label mail_actuel;
    @FXML
    private TextField nom;
    @FXML
    private Label nom_actuel;
    @FXML
    private TextField prenom;
    @FXML
    private Label prenom_actuel;
    @FXML
    private TextField telephone;
    @FXML
    private Label telephone_actuel;
    private PopOver pop;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        salon_tab.setDisable(true);
    }

    @FXML
    public void clickOnCreation() {
        pop = new PopOver();
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("ecran_popover.fxml"));
            root = fxmlLoader.load();
            PopoverWindowController controller = fxmlLoader.<PopoverWindowController>getController();
            controller.setParentController(this);
            pop.setContentNode(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        pop.setArrowLocation(PopOver.ArrowLocation.RIGHT_TOP);
        pop.show(creer_partie,
                creer_partie.localToScreen(creer_partie.getBoundsInLocal()).getMinX(),
                creer_partie.localToScreen(creer_partie.getBoundsInLocal()).getMinY() + 25);
        pop.setDetachable(false);
    }

    @FXML
    public void clickOnQuitterSalon(){
        partie_tab.setDisable(false);
        partie_tab.getTabPane().getSelectionModel().select(partie_tab);
        salon_tab.setDisable(true);
    }

    public void popoverValidation(int numb_players, int width, int height){
        pop.hide();
        // ICI ON VA POUVOIR CREER LA PARTIE AVEC DES VALEURS DONNEES
        salon_tab.setDisable(false);
        salon_tab.getTabPane().getSelectionModel().select(salon_tab);
        partie_tab.setDisable(true);
    }
}
