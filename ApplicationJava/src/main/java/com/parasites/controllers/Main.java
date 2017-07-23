package com.parasites.controllers;

import com.parasites.network.OnlineServerManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application
{

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ecran_accueil.fxml"));
        primaryStage.setTitle("Parasites");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("game_ico.png")));
        primaryStage.show();
    }


    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void stop() throws Exception
    {
        OnlineServerManager.getInstance().disconnect();
        super.stop();
    }
}
