package com.parasites.engine.board;

import com.parasites.engine.players.Player;
import javafx.scene.paint.Color;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas Ecalle on 18/04/2017.
 */
public class BoardTest
{
    private List<Player> players;
    private Player sanchyu;
    private int dimension;

    @Before
    public void initialize()
    {
        dimension = 15;
        players = new ArrayList<>();
        sanchyu = new Player(42,"Sanchyu", Color.YELLOW);
        players.add(sanchyu);
        players.add(new Player(43,"Drakorn", Color.BROWN));
        players.add(new Player(44,"Leekwars", Color.RED));
    }


    @Test(expected = IndexOutOfBoundsException.class)
    public void initialBoard()
    {
        final Board board = Board.createInitialBoard(sanchyu, dimension, players);
        Assert.assertEquals(sanchyu, board.getCurrentPlayer());
        Assert.assertEquals(players, board.getPlayers());
        Assert.assertEquals(null, board.getTile((int) Math.pow(dimension, 2) + 1));
        Assert.assertEquals(null, board.getSelectedParasite());
    }
}