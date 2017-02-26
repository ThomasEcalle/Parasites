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

    public Tile(double w, double h, Paint paint, int tileCoordonate, Parasite parasite)
    {
        super(w, h, paint);
        this.tileCoordonate = tileCoordonate;
        if (parasite != null)
        {
            setParasite(parasite);
        }
        setOnMouseClicked(this);
    }


    @Override
    public void handle(Event event)
    {
        if (MouseEvent.MOUSE_CLICKED == event.getEventType())
        {
            setFill(new ImagePattern(new Image(ParasitesUtils.getImageUrl(Constants.COLONY_NAME, getClass()))));
        }
    }

    @Override
    public String toString()
    {
        if (isOccupied())
        {
            return parasite.toString();
        }
        return "-";
    }

    public boolean isOccupied()
    {
        return isOccupied;
    }

    public void setOccupied(boolean occupied)
    {
        isOccupied = occupied;
    }

    public Parasite getParasite()
    {
        return parasite;
    }

    public void setParasite(Parasite parasite)
    {
        this.parasite = parasite;
        setOccupied(true);
    }

    public void removeParasite()
    {
        this.parasite = null;
        setOccupied(false);
    }
}
