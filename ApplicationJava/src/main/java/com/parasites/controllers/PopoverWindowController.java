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
    private ComboBox nombre_joueurs;
    @FXML
    private ComboBox taille_map;
    @FXML
    private Button valider;

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
            nombre_joueurs.getItems().addAll(i);
            taille_map.getItems().addAll(i + "x" + i);
        }
    }

    @FXML
    public void clickOnValider() {
        if(nombre_joueurs.getValue() != null && taille_map.getValue() != null) {
            int joueurs = Integer.parseInt(nombre_joueurs.getValue().toString());
            int taille = Integer.parseInt("" + taille_map.getValue().toString().toCharArray()[0]);
            // VERIFIER SI LES VALEURS SONT BONNES ICI
            parentController.popoverValidation(joueurs, taille, taille);
        }
    }
}
