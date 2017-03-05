package sample.view;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import sample.Constants;
import sample.engine.board.CreationMove;
import sample.engine.board.Tile;
import sample.engine.pieces.Parasite;
import sample.engine.pieces.Queen;
import sample.utils.ParasitesUtils;

import java.util.List;

/**
 * Created by Thomas Ecalle on 26/02/2017.
 */
public class GraphicTile extends Tile implements EventHandler<Event>
{
    private GraphicBoard graphicBoard;
    private List<CreationMove> selectedParasiteMoves;


    public GraphicTile(double w, double h, Paint paint, int tileCoordonate, Parasite parasite, GraphicBoard graphicBoard)
    {
        super(w, h, paint, tileCoordonate, parasite);
        this.graphicBoard = graphicBoard;
        setOnMouseClicked(this);
    }

    @Override
    public void handle(Event event)
    {

        if (MouseEvent.MOUSE_CLICKED == event.getEventType())
        {

            graphicBoard.hidePossibilities();
            if (graphicBoard.getBoard().isFirstMove)
            {
                setParasite(new Queen(this.getTileCoordonate(), graphicBoard.getBoard().getCurrentPlayer()));
                setFill(new ImagePattern(new Image(ParasitesUtils.getImageUrl(Constants.QUEEN_NAME, getClass()))));
                graphicBoard.getBoard().isFirstMove = false;
            } else
            {
                if (isOccupied())
                {
                    graphicBoard.getBoard().setSelectedParasite(getParasite());
                    selectedParasiteMoves = getParasite().calculateLegalMoves(graphicBoard.board);

                    graphicBoard.showPossibilities(getParasite());
                } else
                {
                    if (graphicBoard.getBoard().getChosenParasite() != null && graphicBoard.getBoard().getSelectedParasite() != null)
                    {
                        System.out.println("You want to place a " + graphicBoard.getBoard().getChosenParasite().toString() + " on tile number " + getTileCoordonate());
                        final Parasite origin = graphicBoard.getBoard().getSelectedParasite();
                        final Parasite created = graphicBoard.getBoard().getChosenParasite();
                        created.setPosition(getTileCoordonate());
                        final CreationMove creationMove = new CreationMove(graphicBoard.getBoard(), origin, created);

                        final List<CreationMove> moves = selectedParasiteMoves;
                        System.out.println(moves.size() + " legal moves");
                        if (moves.contains(creationMove))
                        {
                            System.out.println("we can create a " + created.toString() + " on tile " + created.getPosition());

                        }
                    }
                }
            }
            System.out.println(this.getTileCoordonate());
        }
    }

}
