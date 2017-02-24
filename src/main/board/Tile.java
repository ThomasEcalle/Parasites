package main.board;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import main.Constants;
import main.utils.ParasitesUtils;

/**
 * Created by My-PC on 24/02/2017.
 */
public class Tile extends Rectangle implements EventHandler
{
    public Tile(double w, double h, Paint paint)
    {
        super(w, h, paint);
        setOnMouseClicked(this);
    }


    @Override
    public void handle(Event event)
    {
        if (MouseEvent.MOUSE_CLICKED == event.getEventType())
        {
            setFill(new ImagePattern(new Image(ParasitesUtils.getImageUrl(Constants.BUILDER_NAME, getClass()))));
        }
    }
}
