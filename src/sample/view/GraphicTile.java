package sample.view;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import sample.Constants;
import sample.engine.board.Board;
import sample.engine.board.Tile;
import sample.engine.pieces.Parasite;
import sample.engine.pieces.Queen;
import sample.utils.ParasitesUtils;

/**
 * Created by Thomas Ecalle on 26/02/2017.
 */
public class GraphicTile extends Tile implements EventHandler<MouseEvent>
{
    private Board board;
    private GraphicParasitesChoices graphicParasitesChoices;


    public GraphicTile(double w, double h, Paint paint, int tileCoordonate, Parasite parasite, Board board, GraphicParasitesChoices graphicParasitesChoices)
    {
        super(w, h, paint, tileCoordonate, parasite);
        this.board = board;
        this.graphicParasitesChoices = graphicParasitesChoices;
        setOnMouseClicked(this);
    }


    @Override
    public void handle(MouseEvent event)
    {
        if (MouseEvent.MOUSE_CLICKED == event.getEventType())
        {
            if (board.isFirstMove)
            {
                setParasite(new Queen(this.getTileCoordonate(), board.getCurrentPlayer()));
                setFill(new ImagePattern(new Image(ParasitesUtils.getImageUrl(Constants.QUEEN_NAME, getClass()))));
                board.isFirstMove = false;
            } else
            {
                if (isOccupied())
                {
                    graphicParasitesChoices.hideParasite(getParasite());
                }
                //setFill(new ImagePattern(new Image(ParasitesUtils.getImageUrl(Constants.COLONY_NAME, getClass()))));
            }

            System.out.println(this.getTileCoordonate());
        }
    }
}
