package com.parasites.view;

import com.parasites.Constants;
import com.parasites.engine.GameManager;
import com.parasites.engine.board.Board;
import com.parasites.engine.board.PassTurnMove;
import com.parasites.engine.board.Tile;
import com.parasites.engine.players.MoveTransition;
import com.parasites.engine.players.Player;
import com.parasites.network.OnlineServerManager;
import com.parasites.utils.ParasitesUtils;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * Created by Thomas Ecalle on 26/02/2017.
 */
public final class GraphicBoard extends GridPane
{
    private Board board;

    public GraphicBoard(final Board board)
    {
        this.board = board;
        int counter = 0;

        // in case of a board with an odd number of tiles, in order to draw Queen creation's zone
        boolean isQueenCreationTurn = board.getCurrentPlayer().isFirstMove();
        Color color = Color.WHITE;
        boolean isTileLocked = false;

        for (int i = 0; i < board.DIMENSION; i++)
        {
            for (int j = 0; j < board.DIMENSION; j++)
            {
                //This condition is about the Queen creation Zone
                if (isQueenCreationTurn && !mustTileBeLocked(i, j) && Board.DIMENSION < 30)
                {
                    isTileLocked = false;
                    color = Color.RED;
                } else
                {
                    if (isQueenCreationTurn && Board.DIMENSION < 30)
                    {
                        isTileLocked = true;
                    } else
                    {
                        isTileLocked = false;
                    }

                    color = Color.WHITE;
                }
                final GraphicTile tile = new GraphicTile(32, 32, color, counter, null, isTileLocked);
                tile.setIcon(board);
                counter++;
                tile.setStroke(Color.BLACK);


                add(tile, i, j);
            }
        }
    }

    public Board getBoard()
    {
        return board;
    }

    public void setBoard(Board board)
    {
        this.board = board;
        if (OnlineServerManager.getInstance().getCurrentUser().equals(GameManager.getInstance().getCurrentPlayer()))
        {
            String savedBoard = board.savedFormat();
            OnlineServerManager.getInstance().saveTurn(savedBoard);
        }
    }

    public void showPossibilities(final List<Integer> positions)
    {
        System.out.println("number of tiles to fil == " + positions.size());
        for (int position : positions)
        {
            ((Tile) getChildren().get(position)).setFill(Color.BLUE);
        }
    }

    /**
     * Show the emplacement where players can't put their own queen
     */
    public void showQueensWalls()
    {
        int counter = 0;
        System.out.println("showQueensWall");

        for (final Player player : board.getPlayers())
        {
            if (player.isFirstMove())
            {
                counter++;
            }
            if (player.getQueen() != null)
            {
                for (final int position : player.getQueen().getQueensWall())
                {
                    ((Tile) getChildren().get(position)).setFill(Color.YELLOW);
                    ((Tile) getChildren().get(position)).setStroke(Color.YELLOW);
                }
            }
        }

        // it would mean that the last player just placed it's queen
        if (counter == 0)
        {
            hidePossibilities();
        }
    }

    public void hidePossibilities()
    {
        for (Node node : getChildren())
        {
            if (!((Tile) node).isOccupied())
            {
                ((Tile) node).setFill(Color.WHITE);
            }
        }
    }

    public void drawBoard()
    {
        getChildren().clear();
        if (Constants.LOGGING_BOARD)
        {
            System.out.println(board);
        }

        int counter = 0;

        boolean isQueenCreationTurn = board.getCurrentPlayer().isFirstMove();
        Color color = Color.WHITE;
        boolean isTileLocked = false;


        for (int i = 0; i < board.DIMENSION; i++)
        {
            for (int j = 0; j < board.DIMENSION; j++)
            {
                //This condition is about the Queen creation Zone
                if (isQueenCreationTurn && !mustTileBeLocked(i, j) && Board.DIMENSION < 30)
                {
                    isTileLocked = false;
                    color = Color.RED;
                } else
                {
                    if (isQueenCreationTurn && Board.DIMENSION < 30)
                    {
                        isTileLocked = true;
                    } else
                    {
                        isTileLocked = false;
                    }
                    color = Color.WHITE;
                }
                final GraphicTile tile = new GraphicTile(32, 32, color, counter, board.getTile(counter).getParasite(), isTileLocked);

                tile.setIcon(board);

                tile.setStroke(Color.BLACK);
                if (board.getTile(counter) != null
                        && board.getTile(counter).getParasite() != null
                        && board.getTile(counter).getParasite().getPlayer() != null)
                {
                    tile.setStroke(board.getTile(counter).getParasite().getPlayer().getColor());
                }


                counter++;

                add(tile, i, j);
            }
        }

    }

    private boolean mustTileBeLocked(final int i, final int j)
    {
        final int oddDimensionCase = board.DIMENSION % 3;
        return (i >= board.DIMENSION / 3 && i < ((board.DIMENSION / 3) * 2) + oddDimensionCase
                && j >= board.DIMENSION / 3 && j < ((board.DIMENSION / 3) * 2) + oddDimensionCase);
    }

}
