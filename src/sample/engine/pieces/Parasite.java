package sample.engine.pieces;

import sample.engine.board.Board;
import sample.engine.board.CreationMove;
import sample.engine.players.Player;
import sample.utils.ParasitesUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    /**
     * This function is called in order to know which are the creation moves that a parasite can do
     *
     * @param board
     * @param points
     * @return
     */
    public abstract List<CreationMove> calculateLegalMoves(final Board board, final int points);

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

    public Player mustSurrender()
    {
        Player invader = null;
        final Map<Player, Integer> neighborsPower = new HashMap<>();

        for (int neighbor : neighbors)
        {
            if (ParasitesUtils.isValidTile(getPosition() + neighbor))
            {
                if (isFirstRowExclusion(getPosition(), neighbor)
                        || isLastRowExclusion(getPosition(), neighbor))
                {
                    continue;
                }

                if (actualBoard != null && actualBoard.getTile(getPosition() + neighbor).getParasite() != null)
                {
                    final Parasite closeParasite = actualBoard.getTile(getPosition() + neighbor).getParasite();
                    final Player closePlayer = closeParasite.getPlayer();
                    if (!closePlayer.equals(this.player))
                    {
                        neighborsPower.put(closePlayer, closeParasite.getAttack());
                    }
                    final Iterator it = neighborsPower.entrySet().iterator();
                    while (it.hasNext())
                    {
                        Map.Entry pair = (Map.Entry) it.next();
                        if (((Integer) pair.getValue()) >= this.defence)
                        {
                            invader = (Player) pair.getKey();
                        }
                        it.remove(); // avoids a ConcurrentModificationException
                    }
                }

            }

        }


        return invader;
    }

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

    private boolean isFirstRowExclusion(final int currentPosition, final int candidatePosition)
    {
        return (Board.firstRow[currentPosition] && (candidatePosition == -1));
    }

    private boolean isLastRowExclusion(final int currentPosition, final int candidatePosition)
    {
        return (Board.lastRow[currentPosition] && (candidatePosition == 1));
    }

    public int getAttack()
    {
        return attack;
    }
}
