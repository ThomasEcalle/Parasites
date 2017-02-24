package sample.engine.board;


/**
 * Created by Thomas Ecalle on 24/02/2017.
 */
public final class Board
{

    public static int DIMENSION;
    public static boolean[] firstColumn;
    public static boolean[] lastColumn;
    public static boolean[] firstRow;
    public static boolean[] lastRow;


    public Board(final int dimension)
    {
        this.DIMENSION = dimension;

        firstColumn = initColumn(0);
        lastColumn = initColumn(Board.DIMENSION - 1);

        firstRow = initRow(0);
        lastRow = initRow(Board.DIMENSION - 1);
    }


    public Tile getTile(final int position)
    {
        return null;
    }


    private boolean[] initColumn(int index)
    {
        final boolean[] column = new boolean[Board.DIMENSION];
        for (int i = 0; i < Board.DIMENSION; i++)
        {
            column[index] = true;
            index += 1;
        }
        return column;
    }

    private boolean[] initRow(int index)
    {
        final boolean[] row = new boolean[Board.DIMENSION];
        do
        {
            row[index] = true;
            index += Board.DIMENSION;
        }
        while (index < Board.DIMENSION);
        return row;
    }
}
