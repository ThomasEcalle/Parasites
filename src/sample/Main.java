package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.engine.board.Board;
import sample.engine.players.Player;
import sample.utils.AnimatedZoomOperator;
import sample.view.GraphicBoard;
import sample.view.GraphicParasitesChoices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application
{
    private BorderPane borderPane;
    private GraphicBoard graphicBoard;
    @FXML
    private GraphicParasitesChoices parasitesChoices;
    private Scene scene;
    private AnimatedZoomOperator zoomOperator;

    private Board board;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("view/sample.fxml"));
        primaryStage.setTitle("Parasites");

        zoomOperator = new AnimatedZoomOperator();
        final List<Player> fakeList = new ArrayList<>();
        this.board = Board.createInitialBoard(new Player(), 10, fakeList);

        initGrid();
        final List<Player> players = new ArrayList<>();
        final Board board = Board.createInitialBoard(new Player(), 10, players);
        System.out.println(board);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initGrid()
    {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/sample.fxml"));
        try
        {
            borderPane = (BorderPane) loader.load();
            parasitesChoices = new GraphicParasitesChoices();

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        //Making grid
        int counter = 0;
        graphicBoard = new GraphicBoard(10, board, parasitesChoices
        );


        graphicBoard.setAlignment(Pos.CENTER);
        parasitesChoices.setAlignment(Pos.CENTER_LEFT);

        borderPane.setCenter(graphicBoard);
        borderPane.setRight(parasitesChoices);

        //        graphicBoard.setOnScroll(new EventHandler<ScrollEvent>()
        //        {
        //            @Override
        //            public void handle(ScrollEvent event)
        //            {
        //                double zoomFactor = 1.5;
        //                if (event.getDeltaY() <= 0)
        //                {
        //                    // zoom out
        //                    zoomFactor = 1 / zoomFactor;
        //                }
        //                zoomOperator.zoom(graphicBoard, zoomFactor, event.getSceneX(), event.getSceneY());
        //            }
        //        });

        scene = new Scene(borderPane, 600, 400);


    }


    public static void main(String[] args)
    {
        launch(args);
    }
}
