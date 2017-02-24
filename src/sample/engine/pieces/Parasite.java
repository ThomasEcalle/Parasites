package sample.engine.pieces;

import sample.engine.board.Board;
import sample.engine.board.Move;
import sample.engine.players.Player;

import java.util.List;

/**
 * Created by Thomas Ecalle on 24/02/2017.
 */
public abstract class Parasite
{
    protected final int position;
    protected final Player player;

    protected int cost;
    protected int creationPoint;
    protected int attack;
    protected int defence;
    protected String icon;

    Parasite(final int position, final Player player, int cost, int creationPoint, int attack, int defence, String icon)
    {
        this.position = position;
        this.player = player;
        this.cost = cost;
        this.creationPoint = creationPoint;
        this.attack = attack;
        this.defence = defence;
        this.icon = icon;
    }

    public abstract List<Move> calculateLagalMoves(final Board board);
}
