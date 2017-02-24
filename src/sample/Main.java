package sample;

import com.sun.prism.paint.Paint;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import sample.board.Tile;
import sample.utils.AnimatedZoomOperator;

import java.io.IOException;

public class Main extends Application
{
    private BorderPane borderPane;
    private GridPane gridPane;
    private Scene scene;
    private AnimatedZoomOperator zoomOperator;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("view/sample.fxml"));
        primaryStage.setTitle("Parasites");

        zoomOperator = new AnimatedZoomOperator();

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
        gridPane = new GridPane();

        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                final Tile tile = new Tile(32,32,Color.TRANSPARENT);
                tile.setStroke(Color.BLACK);


                gridPane.add(tile, i, j);
            }
        }


        gridPane.setAlignment(Pos.CENTER);
        borderPane.setCenter(gridPane);

        gridPane.setOnScroll(new EventHandler<ScrollEvent>()
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
                zoomOperator.zoom(gridPane, zoomFactor, event.getSceneX(), event.getSceneY());
            }
        });

        scene = new Scene(borderPane, 600, 400);
    }


    public static void main(String[] args)
    {
        launch(args);
    }
}
