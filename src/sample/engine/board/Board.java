package sample.engine.board;


import javafx.scene.paint.Color;
import sample.engine.pieces.KindOfParasite;
import sample.engine.pieces.Parasite;
import sample.engine.players.Player;
import sample.utils.ParasitesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Thomas Ecalle on 24/02/2017.
 */
public final class Board
{
    public static int DIMENSION;

    public final static KindOfParasite[] EXISTING_PARASITES = {KindOfParasite.BUILDER, KindOfParasite.COLONY,
            KindOfParasite.QUEEN, KindOfParasite.DEFENDER, KindOfParasite.WARRIOR};


    // Arrays for Parasites' movements exceptions
    public static boolean[] firstColumn;
    public static boolean[] secondColumn;
    public static boolean[] beforeLastColumn;
    public static boolean[] lastColumn;
    public static boolean[] firstRow;
    public static boolean[] secondRow;
    public static boolean[] thirdRow;
    public static boolean[] fourthRow;
    public static boolean[] fifthRow;

    public static boolean[] beforeBeforeLastRowPlusTwo;
    public static boolean[] beforeBeforeLastRowPlusOne;

    public static boolean[] beforeBeforeLastRow;
    public static boolean[] beforeLastRow;
    public static boolean[] lastRow;

    private final List<Tile> gameBoard;
    private final List<Player> players;
    private Player currentPlayer;
    private static int playersCounter;

    private Parasite selectedParasite;
    public static Parasite chosenParasite;


    private Board(Builder builder)
    {
        this.currentPlayer = builder.nextMoveMaker;
        this.DIMENSION = builder.dimension;
        this.players = builder.players;
        this.gameBoard = createGameBoard(builder);

        calculateEachPlayerParasites(players);
        calculateLegalMoves(players);
        initExceptionArrays();

    }

    @Override
    public String toString()
    {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < DIMENSION; i++)
        {
            for (int j = 0; j < DIMENSION; j++)
            {
                builder.append(String.format("%3s", gameBoard.get(i + j * DIMENSION).toString()));
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public Player getCurrentPlayer()
    {
        return this.currentPlayer;
    }

    private void calculateEachPlayerParasites(List<Player> players)
    {
        for (Player player : players)
        {
            final List<Parasite> parasitesOnBoard = new ArrayList<>();

            for (Tile tile : gameBoard)
            {
                if (tile.isOccupied() && tile.getParasite().getPlayer().equals(player))
                {
                    parasitesOnBoard.add(tile.getParasite());
                }
            }
            player.setParasites(parasitesOnBoard);
        }
    }

    private void calculateLegalMoves(List<Player> players)
    {
        for (Player player : players)
        {
            final List<CreationMove> legalCreationMoves = new ArrayList<>();
            for (Parasite parasite : player.getParasites())
            {
                legalCreationMoves.addAll(parasite.calculateLegalMoves(this));
            }
            player.setLegalCreationMoves(legalCreationMoves);
        }

    }

    public static Board createInitialBoard(final Player firstToPlay, final int dimension, List<Player> players)
    {
        final Builder builder = new Builder(dimension, players);
        builder.setMoveMaker(firstToPlay);

        playersCounter = 0;
        return builder.build();
    }

    private List<Tile> createGameBoard(final Builder builder)
    {
        final List<Tile> tiles = new ArrayList<>();
        for (int i = 0; i < ((int) Math.pow(DIMENSION, 2)); i++)
        {
            tiles.add(new Tile(32, 32, Color.TRANSPARENT, i, builder.boardConfig.get(i)));
        }
        return tiles;
    }

    private void initExceptionArrays()
    {
        firstColumn = initColumn(0);
        secondColumn = initColumn(Board.DIMENSION - 1);
        beforeLastColumn = initColumn((int) (Math.pow(Board.DIMENSION, 2) - Board.DIMENSION * 2));
        lastColumn = initColumn((int) (Math.pow(Board.DIMENSION, 2) - Board.DIMENSION));

        firstRow = initRow(0);
        secondRow = initRow(1);
        thirdRow = initRow(2);
        fourthRow = initRow(3);
        fifthRow = initRow(4);

        beforeBeforeLastRowPlusTwo = initRow(Board.DIMENSION - 5);
        beforeBeforeLastRowPlusOne = initRow(Board.DIMENSION - 4);
        beforeBeforeLastRow = initRow(Board.DIMENSION - 3);
        beforeLastRow = initRow(Board.DIMENSION - 2);
        lastRow = initRow(Board.DIMENSION - 1);
    }

    public List<Player> getPlayers()
    {
        return players;
    }

    public Tile getTile(final int position)
    {
        return gameBoard.get(position);
    }


    public Player getNextPlayer()
    {
        players.get(playersCounter).setDevelopmentPoints(2);
        players.get(playersCounter).clearPlayingParasites();
        playersCounter++;


        if (playersCounter >= players.size())
        {
            playersCounter = 0;
        }
        if (!players.get(playersCounter).isStillPlaying())
        {
            return getNextPlayer();
        } else
        {
            final Player player = players.get(playersCounter);

            return player;
        }

    }

    /**
     * On a 6x6 dimension board, the first column will be from 0 to 5,
     * the second one from 6 to 11, the last one from 30 to 35, etc...
     *
     * @param index
     * @return
     */
    private boolean[] initColumn(int index)
    {
        final boolean[] column = new boolean[(int) Math.pow(Board.DIMENSION, 2)];
        for (int i = 0; i < Board.DIMENSION; i++)
        {
            column[index] = true;
            index += 1;
        }
        return column;
    }

    private boolean[] initRow(int index)
    {
        final boolean[] row = new boolean[(int) Math.pow(Board.DIMENSION, 2)];
        int count = 0;
        do
        {
            row[index] = true;
            index += Board.DIMENSION;
            count++;
        }
        while (count < Board.DIMENSION);
        return row;
    }

    public void passTurn()
    {

    }

    public static class Builder
    {
        final int dimension;
        final List<Player> players;
        Map<Integer, Parasite> boardConfig;
        Player nextMoveMaker;

        public Builder(final int dimension, final List<Player> players)
        {
            this.dimension = dimension;
            this.players = players;
            this.boardConfig = new HashMap<>();
        }

        public Builder setParasite(final Parasite parasite)
        {
            this.boardConfig.put(parasite.getPosition(), parasite);
            return this;
        }

        public Builder setMoveMaker(final Player player)
        {
            this.nextMoveMaker = player;
            return this;
        }

        public Board build()
        {
            return new Board(this);
        }
    }

    public void setSelectedParasite(Parasite selectedParasite)
    {
        this.selectedParasite = selectedParasite;
    }

    public Parasite getSelectedParasite()
    {
        return selectedParasite;
    }
}
