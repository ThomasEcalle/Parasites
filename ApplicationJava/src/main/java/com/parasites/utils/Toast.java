package com.parasites.utils;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Created by Thomas Ecalle on 14/05/2017.
 */


public final class Toast
{
    public static void makeText(Stage ownerStage, String toastMsg, int toastDelay, int fadeInDelay, int fadeOutDelay)
    {
        final Stage toastStage = new Stage();
        toastStage.initOwner(ownerStage);
        toastStage.setResizable(false);
        toastStage.initStyle(StageStyle.TRANSPARENT);

        final int padding = 30;

        final Text text = new Text(toastMsg);
        text.setFont(Font.font("Verdana", 20));
        text.setFill(Color.GREEN);

        final StackPane root = new StackPane(text);
        root.setStyle("-fx-background-radius: 20px; -fx-background-color: rgba(0, 0, 0); -fx-padding: 30px;");
        root.setOpacity(0);

        final Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        toastStage.setScene(scene);
        toastStage.show();

        final Timeline fadeInTimeline = new Timeline();
        final KeyFrame fadeInKey1 = new KeyFrame(Duration.millis(fadeInDelay), new KeyValue(toastStage.getScene().getRoot().opacityProperty(), 1));
        fadeInTimeline.getKeyFrames().add(fadeInKey1);


        toastStage.setX((ownerStage.getWidth() - toastStage.getWidth() + padding));
        toastStage.setY(ownerStage.getY() + padding);


        fadeInTimeline.setOnFinished((ae) ->
        {
            new Thread(() ->
            {
                try
                {
                    Thread.sleep(toastDelay);
                } catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Timeline fadeOutTimeline = new Timeline();
                KeyFrame fadeOutKey1 = new KeyFrame(Duration.millis(fadeOutDelay), new KeyValue(toastStage.getScene().getRoot().opacityProperty(), 0));
                fadeOutTimeline.getKeyFrames().add(fadeOutKey1);
                fadeOutTimeline.setOnFinished((aeb) -> toastStage.close());
                fadeOutTimeline.play();
            }).start();
        });
        fadeInTimeline.play();
    }
}