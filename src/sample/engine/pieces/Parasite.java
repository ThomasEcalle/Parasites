package sample.engine.pieces;

import sample.engine.board.Board;
import sample.engine.board.CreationMove;
import sample.engine.players.Player;

import java.util.List;

/**
 * Created by Thomas Ecalle on 24/02/2017.
 */
public abstract class Parasite
{
    protected int position;
    protected Player player;

    protected int cost;
    protected int developmentPointsUsed;
    protected int creationPoints;
    protected int initialCreationPoints;
    protected int attack;
    protected int defence;
    protected String icon;
    protected Board actualBoard;
    protected boolean alreadyUsedInTurn = false;

    public final static int[] neighbors = {1, -1, Board.DIMENSION, -Board.DIMENSION};


    Parasite(final int position, final Player player, int cost, int creationPoint, int attack, int defence, String icon, int developmentPointsUsed)
    {
        this.position = position;
        this.player = player;
        this.cost = cost;
        this.creationPoints = creationPoint;
        this.initialCreationPoints = creationPoint;
        this.attack = attack;
        this.defence = defence;
        this.icon = icon;
        this.developmentPointsUsed = developmentPointsUsed;
    }

    @Override
    public int hashCode()
    {
        return cost * developmentPointsUsed * attack - defence;
    }

    public abstract List<CreationMove> calculateLegalMoves(final Board board);

    public abstract List<Integer> getArea();

    protected Parasite getParasiteObject(final KindOfParasite existingParasite, final int candidateDestination, final Player player)
    {
        switch (existingParasite)
        {
            case BUILDER:
                return new Builder(candidateDestination, player);
            case WARRIOR:
                return new Warrior(candidateDestination, player);
            case QUEEN:
                return new Queen(candidateDestination, player);
            case COLONY:
                return new Colony(candidateDestination, player);
            default:
                return new Defender(candidateDestination, player);

        }
    }

    //    public boolean mustSurrender()
    //    {
    //
    //        for (int neighbor : neighbors)
    //        {
    //
    //        }
    //    }

    public int getPosition()
    {
        return position;
    }

    public Player getPlayer()
    {
        return player;
    }

    public void setPlayer(Player player)
    {
        this.player = player;
    }

    public int getCost()
    {
        return cost;
    }

    public int getDevelopmentPointsUsed()
    {
        return developmentPointsUsed;
    }

    public void setCreationPoints(int creationPoints)
    {
        this.creationPoints = creationPoints;
    }

    public int getCreationPoints()
    {
        return creationPoints;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }

    public void setAlreadyUsedInTurn(boolean alreadyUsedInTurn)
    {
        this.alreadyUsedInTurn = alreadyUsedInTurn;
    }

    public boolean isAlreadyUsedInTurn()
    {
        return alreadyUsedInTurn;
    }

    public int getInitialCreationPoints()
    {
        return initialCreationPoints;
    }
}
