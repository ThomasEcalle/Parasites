package sample.view;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import sample.engine.board.Board;
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
    private boolean isTileLocked;


    public GraphicTile(final double w, final double h, final Paint paint, final int tileCoordonate, final Parasite parasite, final GraphicBoard graphicBoard, final boolean isTileLocked)
    {
        super(w, h, paint, tileCoordonate, parasite);
        this.graphicBoard = graphicBoard;
        this.isTileLocked = isTileLocked;
        setOnMouseClicked(this);
    }

    @Override
    public void handle(Event event)
    {
        final Player currentPlayer = graphicBoard.getBoard().getCurrentPlayer();
        final Queen queen = new Queen(this.getTileCoordonate(), currentPlayer);
        if (!graphicBoard.getBoard().getCurrentPlayer().isFirstMove() || (!isTileLocked && (!isOccupied() && !queen.isClosedToAnotherQueen(graphicBoard.getBoard()))))
        {

            if (MouseEvent.MOUSE_CLICKED == event.getEventType() && (!isTileLocked))
            {

                ParasitesUtils.logWarnings("Current player is " + currentPlayer.toString());
                graphicBoard.hidePossibilities();
                if (currentPlayer.isFirstMove())
                {
                    if (!isOccupied())
                    {
                        currentPlayer.setQueen(queen);
                        final FirstMove firstMove = new FirstMove(graphicBoard.getBoard(), queen);
                        final MoveTransition moveTransition = currentPlayer.makeMove(firstMove);
                        if (moveTransition.getMoveStatus().isDone())
                        {
                            currentPlayer.setFirstMove(false);
                            graphicBoard.setBoard(moveTransition.getTransitionBoard());
                            graphicBoard.drawBoard();
                            clearSelectedElements();
                        }
                        graphicBoard.showQueensWalls();

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
                        if (Board.chosenParasite != null && graphicBoard.getBoard().getSelectedParasite() != null)
                        {
                            ParasitesUtils.logError("mamamia");
                            final Parasite origin = graphicBoard.getBoard().getSelectedParasite();

                            final Parasite created = Board.chosenParasite;

                            created.setPosition(getTileCoordonate());
                            final CreationMove creationMove = new CreationMove(graphicBoard.getBoard(), origin, created);

                            final List<CreationMove> moves = origin.calculateLegalMoves(graphicBoard.getBoard());

                            if (moves.contains(creationMove) && origin.getCreationPoints() >= created.getCost())
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
    }


    private void managingPlayingparasites(final ArrayList<Parasite> playingParasites, final Parasite parasite)
    {
        for (final Parasite playingParasite : playingParasites)
        {
            // Test if it was played already or not
            if (playingParasite.getCreationPoints() == playingParasite.getInitialCreationPoints() && !playingParasites.contains(parasite))
            {
                playingParasites.remove(playingParasite);
                playingParasites.add(parasite);
            }
        }

    }

    private void clearSelectedElements()
    {
        graphicBoard.getBoard().setSelectedParasite(null);
        Board.chosenParasite = null;
    }
}
