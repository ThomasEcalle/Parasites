package sample.view;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import sample.engine.board.CreationMove;
import sample.engine.board.FirstMove;
import sample.engine.board.Tile;
import sample.engine.pieces.Parasite;
import sample.engine.pieces.Queen;
import sample.engine.players.MoveTransition;

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
                //setParasite(new Queen(this.getTileCoordonate(), graphicBoard.getBoard().getCurrentPlayer()));
                //setFill(new ImagePattern(new Image(ParasitesUtils.getImageUrl(Constants.QUEEN_NAME, getClass()))));

                final FirstMove firstMove = new FirstMove(graphicBoard.getBoard(), new Queen(this.getTileCoordonate(), graphicBoard.getBoard().getCurrentPlayer()));
                final MoveTransition moveTransition = graphicBoard.getBoard().getCurrentPlayer().makeMove(firstMove);
                if (moveTransition.getMoveStatus().isDone())
                {
                    graphicBoard.setBoard(moveTransition.getTransitionBoard());
                    graphicBoard.drawBoard();
                }
                graphicBoard.getBoard().isFirstMove = false;
            } else
            {
                if (isOccupied())
                {
                    System.out.println("is occupied case");
                    graphicBoard.getBoard().setSelectedParasite(getParasite());
                    selectedParasiteMoves = getParasite().calculateLegalMoves(graphicBoard.getBoard());

                    graphicBoard.showPossibilities(selectedParasiteMoves);
                } else
                {
                    if (graphicBoard.getBoard().chosenParasite != null && graphicBoard.getBoard().getSelectedParasite() != null)
                    {
                        System.out.println("You want to place a " + graphicBoard.getBoard().chosenParasite.toString() + " on tile number " + getTileCoordonate());
                        final Parasite origin = graphicBoard.getBoard().getSelectedParasite();
                        final Parasite created = graphicBoard.getBoard().chosenParasite;
                        created.setPosition(getTileCoordonate());
                        final CreationMove creationMove = new CreationMove(graphicBoard.getBoard(), origin, created);

                        final List<CreationMove> moves = origin.calculateLegalMoves(graphicBoard.getBoard());
                        if (moves.contains(creationMove))
                        {
                            System.out.println("we can create a " + created.toString() + " on tile " + created.getPosition());
                            final MoveTransition moveTransition = graphicBoard.getBoard().getCurrentPlayer().makeMove(creationMove);
                            if (moveTransition.getMoveStatus().isDone())
                            {
                                graphicBoard.setBoard(moveTransition.getTransitionBoard());
                                graphicBoard.drawBoard();
                            }
                        }
                    }
                }
            }
            System.out.println(this.getTileCoordonate());
        }
    }

}
