package sample;

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
import sample.engine.board.Board;
import sample.engine.pieces.Builder;
import sample.engine.pieces.Defender;
import sample.engine.pieces.Warrior;
import sample.engine.players.Player;
import sample.utils.AnimatedZoomOperator;
import sample.view.GraphicBoard;

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
    public final static int DIMENSION = 10;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("view/sample.fxml"));
        primaryStage.setTitle("Parasites");

        zoomOperator = new AnimatedZoomOperator();

        final List<Player> fakeList = new ArrayList<>();
        fakeList.add(new Player("thomas", Color.YELLOW));
        fakeList.add(new Player("jean", Color.GREEN));
        fakeList.add(new Player("robin", Color.RED));
        this.board = Board.createInitialBoard(fakeList.get(0), DIMENSION, fakeList);

        initGrid();

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

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        //Making grid
        int counter = 0;
        graphicBoard = new GraphicBoard(DIMENSION, board);


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
                System.out.println(keyEvent.getCode());
                switch (keyEvent.getText())
                {
                    case "b":
                        board.chosenParasite = new Builder(-1, board.getCurrentPlayer());
                        break;
                    case "w":
                        board.chosenParasite = new Warrior(-1, board.getCurrentPlayer());
                        break;
                    case "d":
                        board.chosenParasite = new Defender(-1, board.getCurrentPlayer());
                        break;
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
