package com.parasites;

import com.parasites.engine.board.Board;
import com.parasites.engine.pieces.Builder;
import com.parasites.engine.pieces.Defender;
import com.parasites.engine.pieces.Warrior;
import com.parasites.engine.players.Player;
import com.parasites.network.OnlineGameManager;
import com.parasites.utils.AnimatedZoomOperator;
import com.parasites.view.GraphicBoard;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application
{
    private BorderPane borderPane;
    private GraphicBoard graphicBoard;
    private Scene scene;
    private AnimatedZoomOperator zoomOperator;
    private Board board;
    public final static int DIMENSION = 15;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));
        primaryStage.setTitle("Parasites");

        zoomOperator = new AnimatedZoomOperator();

        final List<Player> fakeList = new ArrayList<>();
        final Player realUser = new Player("robin", Color.YELLOW);
        final Player jean = new Player("thomas", Color.GREEN);
        final Player robin = new Player("jean", Color.RED);

        fakeList.add(realUser);
        fakeList.add(jean);
//        fakeList.add(robin);

        final List<Player> opponents = new ArrayList<>();
        opponents.add(jean);
        opponents.add(robin);

        final OnlineGameManager onlineGameManager = OnlineGameManager.getInstance(realUser, opponents);

        this.board = Board.createInitialBoard(fakeList.get(0), DIMENSION, fakeList);

        initGrid();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initGrid()
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/sample.fxml"));
        try
        {
            borderPane = (BorderPane) loader.load();

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        //Making grid
        graphicBoard = new GraphicBoard(board);


        graphicBoard.setAlignment(Pos.CENTER);

        borderPane.setCenter(graphicBoard);

        graphicBoard.setOnScroll(new EventHandler<ScrollEvent>()
        {
            @Override
            public void handle(ScrollEvent event)
            {
                double zoomFactor = 1.5;
                if (event.getDeltaY() <= 0)
                {
                    // zoom out
                    zoomFactor = 1 / zoomFactor;
                }
                zoomOperator.zoom(graphicBoard, zoomFactor, event.getSceneX(), event.getSceneY());
            }
        });

        scene = new Scene(borderPane, 600, 400);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            public void handle(KeyEvent keyEvent)
            {

                switch (keyEvent.getCode())
                {
                    case B:
                        board.chosenParasite = new Builder(-1, board.getCurrentPlayer());
                        break;
                    case W:
                        board.chosenParasite = new Warrior(-1, board.getCurrentPlayer());
                        break;
                    case D:
                        board.chosenParasite = new Defender(-1, board.getCurrentPlayer());
                        break;
                    case SPACE:
                        graphicBoard.passTurn();
                    default:
                        board.chosenParasite = null;
                        break;
                }
            }
        });

    }


    public static void main(String[] args)
    {
        launch(args);
    }
}
