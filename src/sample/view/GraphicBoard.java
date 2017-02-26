package sample.view;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import sample.engine.board.Board;
import sample.engine.board.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas Ecalle on 26/02/2017.
 */
public class GraphicBoard extends GridPane
{
    final List<Tile> tiles;

    public GraphicBoard(final int dimension, Board board, GraphicParasitesChoices graphicParasitesChoices)
    {
        tiles = new ArrayList<>();
        int counter = 0;
        for (int i = 0; i < dimension; i++)
        {
            for (int j = 0; j < dimension; j++)
            {
                final GraphicTile tile = new GraphicTile(32, 32, Color.TRANSPARENT, counter, null,board, graphicParasitesChoices);
                tile.setIcon(board);
                counter++;
                tile.setStroke(Color.BLACK);


                add(tile, i, j);
            }
        }
    }
}
