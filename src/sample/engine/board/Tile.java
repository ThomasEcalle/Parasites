package sample.engine.board;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import sample.Constants;
import sample.engine.pieces.Parasite;
import sample.utils.ParasitesUtils;

/**
 * Created by Thomas Ecalle on 24/02/2017.
 */
public final class Tile extends Rectangle implements EventHandler
{
    private final int tileCoordonate;
    private Parasite parasite;
    private boolean isOccupied;

    public Tile(double w, double h, Paint paint, int tileCoordonate)
    {
        super(w, h, paint);
        this.tileCoordonate = tileCoordonate;
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

    public boolean isOccupied()
    {
        return isOccupied;
    }

    public Parasite getParasite()
    {
        return parasite;
    }

    public void setParasite(Parasite parasite)
    {
        this.parasite = parasite;
    }
}