package com.parasites.engine.board;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.parasites.Constants;
import com.parasites.engine.pieces.KindOfParasite;
import com.parasites.engine.pieces.Parasite;
import com.parasites.engine.players.Player;
import com.parasites.utils.ParasitesUtils;
import javafx.scene.paint.Color;

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

    private List<Tile> gameBoard = null;
    private final List<Player> players;
    private Player currentPlayer;
    private static int playersCounter;

    private Parasite selectedParasite;
    public static Parasite chosenParasite;

    public Player getWinner()
    {
//        System.out.println("tata");
        int mapSize = gameBoard.size();
        int PlayerListSize = players.size();
        int max = 0;
        Player winner = null;
        HashMap<Player,Integer> playerPointMap = new HashMap<Player,Integer>();
        for (Player player : players) {
            playerPointMap.put(player,0);
        }
//        System.out.println("titi");
        for (Tile tile : gameBoard) {
            Player tmpPlayer = null;
            if (tile.isOccupied())
            {
                tmpPlayer =  tile.getParasite().getPlayer();
                playerPointMap.put(tmpPlayer,playerPointMap.get(tmpPlayer) + 1);

            }
            else
            {
                Double dist = 0.0;
                for (Player player : players)
                {
                    for (Parasite parasite : player.getParasites())
                    {
                        int tilePlayerX = (int)(parasite.getPosition() / Math.sqrt(mapSize));
                        int tileX = (int)(tile.getTileCoordonate() / Math.sqrt(mapSize));
                        int tilePlayerY = (int)(parasite.getPosition() % Math.sqrt(mapSize));
                        int tileY = (int)(tile.getTileCoordonate() % Math.sqrt(mapSize));
                        double tmpdist = Math.abs(tilePlayerX-tileX) + Math.abs(tilePlayerY-tileY);
                        if (tmpdist < dist || dist == 0.0)
                        {
                            dist = tmpdist;
                            tmpPlayer = player;
                            playerPointMap.put(tmpPlayer,playerPointMap.get(tmpPlayer) + 1);
                        }

                    }
                }

            }
            if (tmpPlayer == null)
                continue;
            if ( playerPointMap.get(tmpPlayer) > max)
            {
                max = playerPointMap.get(tmpPlayer);
                winner = tmpPlayer;
            }

        }
        return winner;
    }
    private Board(Builder builder)
    {
        this.currentPlayer = builder.nextMoveMaker;
        this.DIMENSION = builder.dimension;
        this.players = builder.players;
        writeBoardInJSON();
        this.gameBoard = createGameBoard(builder);

        calculateEachPlayerParasites(players);
        calculateLegalMoves(players);
        initExceptionArrays();

        System.out.println("banane :\n " + savedFormat());


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

    public String savedFormat()
    {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < DIMENSION; i++)
        {
            for (int j = 0; j < DIMENSION; j++)
            {
                final Tile tile = gameBoard.get(i + j * DIMENSION);
                if (tile.isOccupied())
                {
                    builder.append(tile.toString() + "," + tile.getParasite().getPlayer().getId());
                } else
                {
                    builder.append("*");
                }
                builder.append("|");
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
        final Player actual = players.get(playersCounter);
        if (actual.getDevelopmentPoints() != Constants.INITIAL_DEVELOPMENT_POINTS)
        {
            actual.setPlayedLastTurn(true);
        } else
        {
            actual.setPlayedLastTurn(false);
        }
        actual.setDevelopmentPoints(2);
        actual.clearPlayingParasites();
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
            return players.get(playersCounter);
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

    public boolean isGameEnded()
    {
        for (Player player : players)
        {
            ParasitesUtils.logWarnings("isGameEnded, has played : " + player.getPseudo() + " ?");
            if (player.isStillPlaying() && player.hasPlayedLastTurn())
            {
                ParasitesUtils.logWarnings("isGameEnded, " + player.getPseudo() + ", yes");
                return false;
            }
        }
        return true;
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

    private void writeBoardInJSON()
    {
        final Gson gson = new GsonBuilder().create();
    }
}
