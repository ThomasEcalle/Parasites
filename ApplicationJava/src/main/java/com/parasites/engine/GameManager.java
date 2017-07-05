package com.parasites.engine;

import com.parasites.engine.board.*;
import com.parasites.engine.pieces.*;
import com.parasites.engine.players.MoveTransition;
import com.parasites.engine.players.Player;
import com.parasites.network.OnlineServerManager;
import com.parasites.utils.ParasitesUtils;
import com.parasites.view.GraphicBoard;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas Ecalle on 01/07/2017.
 */
public final class GameManager
{
    private static volatile GameManager instance;
    private List<Player> players;
    private Player currentPlayer;
    private Board board;
    private GraphicBoard graphicBoard;
    private List<Integer> selectedParasiteMoves;


    /**
     * Singleton Pattern
     * We accept the "out-of-order writes" case
     *
     * @return
     */
    public static GameManager getInstance()
    {
        if (instance == null)
        {
            synchronized (OnlineServerManager.class)
            {
                if (instance == null)
                {
                    instance = new GameManager();
                }
            }
        }

        return instance;
    }

    public void startGame(final GraphicBoard graphicBoard)
    {
        this.graphicBoard = graphicBoard;
        this.board = graphicBoard.getBoard();
        this.players = board.getPlayers();
        this.currentPlayer = board.getCurrentPlayer();
    }


    public void playTurn(final int destinationTileCoordinate, final boolean isTileLocked, final KindOfParasite chosenParasiteType)
    {
        final Tile destinationTile = board.getTile(destinationTileCoordinate);
        Board.chosenParasite = getParasiteFromName(chosenParasiteType, destinationTileCoordinate, currentPlayer);

        final Queen queen = new Queen(destinationTileCoordinate, currentPlayer);

        if (!currentPlayer.isFirstMove()
                || (!isTileLocked && (!destinationTile.isOccupied() && !queen.isClosedToAnotherQueen(board))))
        {
            ParasitesUtils.logWarnings("Current player is " + currentPlayer.toString());
            graphicBoard.hidePossibilities();

            if (currentPlayer.isFirstMove())
            {
                if (!destinationTile.isOccupied())
                {
                    currentPlayer.setQueen(queen);
                    final FirstMove firstMove = new FirstMove(graphicBoard.getBoard(), queen);
                    final MoveTransition moveTransition = currentPlayer.makeMove(firstMove);
                    if (moveTransition.getMoveStatus().isDone())
                    {
                        currentPlayer.setFirstMove(false);


                        graphicBoard.setBoard(moveTransition.getTransitionBoard());
                        Platform.runLater(() -> graphicBoard.drawBoard());

                        updateInformations(moveTransition.getTransitionBoard());
                        clearSelectedElements();

                    }
                    graphicBoard.showQueensWalls();

                }
            } else
            {

                if (destinationTile.isOccupied())
                {
                    managingPlayingparasites(currentPlayer.getPlayingParasites(), destinationTile.getParasite());
                    if (destinationTile.getParasite().getPlayer().equals(currentPlayer)
                            && (currentPlayer.getPlayingParasites().size() < 2 || currentPlayer.getPlayingParasites().contains(destinationTile.getParasite())))
                    {


                        board.setSelectedParasite(destinationTile.getParasite());
                        if (!currentPlayer.getPlayingParasites().contains(destinationTile.getParasite()) && currentPlayer.getDevelopmentPoints() > 0)
                        {
                            currentPlayer.addPlayingparasite(destinationTile.getParasite());
                        }
                    }

                    selectedParasiteMoves = destinationTile.getParasite().getArea();

                    graphicBoard.showPossibilities(selectedParasiteMoves);
                } else
                {

                    if (Board.chosenParasite != null && board.getSelectedParasite() != null)
                    {
                        final Parasite origin = board.getSelectedParasite();

                        final Parasite created = Board.chosenParasite;

                        created.setPosition(destinationTileCoordinate);
                        final CreationMove creationMove = new CreationMove(board, origin, created);

                        final List<CreationMove> moves = origin.calculateLegalMoves(board);

                        if (moves.contains(creationMove) && origin.getCreationPoints() >= created.getCost())
                        {
                            final MoveTransition moveTransition = currentPlayer.makeMove(creationMove);
                            if (moveTransition.getMoveStatus().isDone())
                            {
                                graphicBoard.setBoard(moveTransition.getTransitionBoard());
                                Platform.runLater(() -> graphicBoard.drawBoard());

                                updateInformations(moveTransition.getTransitionBoard());

                                clearSelectedElements();
                            }
                        }
                    }


                }
            }

        }
    }

    public Player getCurrentPlayer()
    {
        return currentPlayer;
    }

    public void passTurn()
    {
        if (!currentPlayer.isFirstMove())
        {
            final PassTurnMove passTurnMove = new PassTurnMove(board);
            final MoveTransition moveTransition = currentPlayer.makeMove(passTurnMove);
            if (moveTransition.getMoveStatus().isDone())
            {
                graphicBoard.setBoard(moveTransition.getTransitionBoard());
                Platform.runLater(()->graphicBoard.drawBoard());
                updateInformations(moveTransition.getTransitionBoard());
                if (board.isGameEnded())
                {
                    ParasitesUtils.logWarnings("The players all passed, game is over");
                }

            }
        }
    }

    public void chooseWarrior()
    {
        board.chosenParasite = new Warrior(-1, board.getCurrentPlayer());
    }

    public void chooseDefender()
    {
        board.chosenParasite = new Defender(-1, board.getCurrentPlayer());
    }

    public void chooseBuilder()
    {
        board.chosenParasite = new Builder(-1, board.getCurrentPlayer());
    }

    private Parasite getParasiteFromName(final KindOfParasite type, final int position, final Player player)
    {
        if (type == null)
        {
            return null;
        }

        switch (type)
        {
            case BUILDER:
                return new Builder(position, player);
            case WARRIOR:
                return new Warrior(position, player);
            case DEFENDER:
                return new Defender(position, player);
            default:
                return null;

        }
    }


    private void clearSelectedElements()
    {
        board.setSelectedParasite(null);
        Board.chosenParasite = null;
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

    private void updateInformations(Board board)
    {
        this.board = board;
        this.players = board.getPlayers();
        this.currentPlayer = board.getCurrentPlayer();
    }

}
