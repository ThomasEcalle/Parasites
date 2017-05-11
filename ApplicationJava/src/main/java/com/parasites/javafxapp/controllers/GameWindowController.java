package com.parasites.javafxapp.controllers;

import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

public class GameWindowController implements Initializable{


    @FXML
    private Button retour_button;
    @FXML
    private Button valider_button;
    @FXML
    private Button avance_button;
    @FXML
    private TextField chat_textfield;
    @FXML
    private Button chat_button;
    @FXML
    private ListView chat_listview;
    @FXML
    private TableColumn partie_tablecolumn;
    @FXML
    private TableColumn amis_tablecolumn;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
