package sample.view;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import sample.engine.board.Board;
import sample.engine.board.CreationMove;
import sample.engine.board.Tile;

import java.util.List;

/**
 * Created by Thomas Ecalle on 26/02/2017.
 */
public class GraphicBoard extends GridPane
{
    private Board board;

    public GraphicBoard(final int dimension, Board board)
    {
        this.board = board;
        int counter = 0;
        for (int i = 0; i < dimension; i++)
        {
            for (int j = 0; j < dimension; j++)
            {
                final GraphicTile tile = new GraphicTile(32, 32, Color.WHITE, counter, null, this);
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
    }

    public void showPossibilities(List<CreationMove> moves)
    {
        System.out.println("number of tiles to fil == " + moves.size());
        for (CreationMove move : moves)
        {
            ((Tile) getChildren().get(move.getCreatedParasite().getPosition())).setFill(Color.BLUE);
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
        System.out.println(board);
        int counter = 0;
        for (int i = 0; i < board.DIMENSION; i++)
        {
            for (int j = 0; j < board.DIMENSION; j++)
            {
                final GraphicTile tile = new GraphicTile(32, 32, Color.WHITE, counter, board.getTile(counter).getParasite(), this);
                tile.setIcon(board);
                counter++;
                tile.setStroke(Color.BLACK);


                add(tile, i, j);
            }
        }
    }
}