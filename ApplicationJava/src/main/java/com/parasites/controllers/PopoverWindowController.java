package com.parasites.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by jpougetoux on 12/05/2017.
 */
public class PopoverWindowController implements Initializable{

    @FXML
    private ComboBox players_number;
    @FXML
    private ComboBox map_size;
    @FXML
    private Button validate;

    public PrincipalWindowController getParentController() {
        return parentController;
    }

    public void setParentController(PrincipalWindowController parentController) {
        this.parentController = parentController;
    }

    private PrincipalWindowController parentController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for(int i = 1; i <= 100; i++){
            players_number.getItems().addAll(i);
            map_size.getItems().addAll(i + "x" + i);
        }
    }

    @FXML
    public void clickOnValider() {
        if(players_number.getValue() != null && map_size.getValue() != null) {
            int joueurs = Integer.parseInt(players_number.getValue().toString());
            int taille = Integer.parseInt("" + map_size.getValue().toString().split("x")[0]);
            // VERIFIER SI LES VALEURS SONT BONNES ICI
            parentController.popoverValidation(joueurs, taille, taille);
        }
    }
}
