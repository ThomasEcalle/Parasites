package sample.board;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import sample.Constants;
import sample.utils.ParasitesUtils;

import java.net.URISyntaxException;

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
