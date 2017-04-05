package sample.engine.board;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import sample.engine.pieces.Parasite;
import sample.utils.ParasitesUtils;

/**
 * Created by Thomas Ecalle on 24/02/2017.
 */
public class Tile extends Rectangle
{
    private final int tileCoordonate;
    private Parasite parasite;
    private boolean isOccupied;
    private boolean isFirstMove = true;

    public Tile(double w, double h, Paint paint, int tileCoordonate, Parasite parasite)
    {
        super(w, h, paint);
        this.tileCoordonate = tileCoordonate;
        if (parasite != null)
        {
            setParasite(parasite);
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

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Tile)) return false;
        final Tile other = (Tile) obj;
        final boolean coordonateTest = this.tileCoordonate == other.tileCoordonate;
        if (this.getParasite() != null)
        {
            return coordonateTest && this.parasite.equals(other.parasite);
        } else
        {
            return coordonateTest && other.getParasite() == null;
        }
    }

    public void setIcon(Board board)
    {
        if (board.getTile(this.tileCoordonate).isOccupied())
        {
            final Image image = new Image(ParasitesUtils.getResourceUrl(board.getTile(this.tileCoordonate).getParasite().getIcon(), getClass()));

            final ImagePattern imagePattern = new ImagePattern(image);

            setFill(imagePattern);
        }
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

    public int getTileCoordonate()
    {
        return tileCoordonate;
    }
}
