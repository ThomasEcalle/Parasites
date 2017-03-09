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
import sample.engine.players.Player;
import sample.utils.ParasitesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas Ecalle on 26/02/2017.
 */
public final class GraphicTile extends Tile implements EventHandler<Event>
{
    private GraphicBoard graphicBoard;
    private List<Integer> selectedParasiteMoves;


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
            final Player currentPlayer = graphicBoard.getBoard().getCurrentPlayer();

            ParasitesUtils.logWarnings("Current player is " + currentPlayer.toString());
            graphicBoard.hidePossibilities();
            if (currentPlayer.isFirstMove())
            {
                if (!isOccupied())
                {
                    final FirstMove firstMove = new FirstMove(graphicBoard.getBoard(), new Queen(this.getTileCoordonate(), graphicBoard.getBoard().getCurrentPlayer()));
                    final MoveTransition moveTransition = currentPlayer.makeMove(firstMove);
                    if (moveTransition.getMoveStatus().isDone())
                    {
                        currentPlayer.setFirstMove(false);
                        graphicBoard.setBoard(moveTransition.getTransitionBoard());
                        graphicBoard.drawBoard();
                        clearSelectedElements();
                    }

                }
            } else
            {
                if (isOccupied())
                {
                    managingPlayingparasites(currentPlayer.getPlayingParasites(), getParasite());
                    if (getParasite().getPlayer().equals(currentPlayer)
                            && (currentPlayer.getPlayingParasites().size() < 2 || currentPlayer.getPlayingParasites().contains(getParasite())))
                    {

                        graphicBoard.getBoard().setSelectedParasite(getParasite());
                        if (!currentPlayer.getPlayingParasites().contains(getParasite()) && currentPlayer.getDevelopmentPoints() > 0)
                        {
                            currentPlayer.addPlayingparasite(getParasite());
                        }
                    }

                    selectedParasiteMoves = getParasite().getArea();

                    graphicBoard.showPossibilities(selectedParasiteMoves);
                } else
                {
                    if (graphicBoard.getBoard().chosenParasite != null && graphicBoard.getBoard().getSelectedParasite() != null)
                    {
                        final Parasite origin = graphicBoard.getBoard().getSelectedParasite();

                        final Parasite created = graphicBoard.getBoard().chosenParasite;


                        created.setPosition(getTileCoordonate());
                        final CreationMove creationMove = new CreationMove(graphicBoard.getBoard(), origin, created);

                        final List<CreationMove> moves = origin.calculateLegalMoves(graphicBoard.getBoard());

                        if (moves.contains(creationMove) && created.getCost() <= origin.getCreationPoints())
                        {
                            final MoveTransition moveTransition = currentPlayer.makeMove(creationMove);
                            if (moveTransition.getMoveStatus().isDone())
                            {
                                graphicBoard.setBoard(moveTransition.getTransitionBoard());
                                graphicBoard.drawBoard();
                                clearSelectedElements();
                            }
                        }
                    }
                }
            }
            System.out.println(this.getTileCoordonate());
        }
    }

    private void managingPlayingparasites(final ArrayList<Parasite> playingParasites, final Parasite parasite)
    {
        for (final Parasite playingParasite : playingParasites)
        {
            // Test if it was played already or not
            if (playingParasite.getCreationPoints() == playingParasite.getInitialCreationPoints())
            {
                playingParasites.remove(playingParasite);
                playingParasites.add(parasite);
            }
        }

    }

    private void clearSelectedElements()
    {
        graphicBoard.getBoard().setSelectedParasite(null);
        graphicBoard.getBoard().chosenParasite = null;
    }

}
