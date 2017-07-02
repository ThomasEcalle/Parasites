package com.parasites.engine.pieces;

import com.parasites.annotations.Characteristics;
import com.parasites.annotations.Representation;
import com.parasites.annotations.SoundEffect;
import com.parasites.engine.board.Board;
import com.parasites.engine.board.CreationMove;
import com.parasites.engine.players.Player;
import com.parasites.utils.ParasitesUtils;

import java.util.*;

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
    protected Board actualBoard;
    protected boolean alreadyUsedInTurn = false;

    public final static int[] neighbors = {1, -1, Board.DIMENSION, -Board.DIMENSION};

    Parasite(final int position, final Player player)
    {
        this.position = position;
        this.player = player;
        getCharacteristics();
    }

    private void getCharacteristics()
    {
        Characteristics characteristics = getClass().getAnnotation(Characteristics.class);
        if (characteristics == null)
        {
            ParasitesUtils.logError("You created a Parasite without characteristics");
        } else
        {
            this.cost = characteristics.cost();
            this.creationPoints = characteristics.creationPoints();
            this.initialCreationPoints = characteristics.creationPoints();
            this.attack = characteristics.attack();
            this.defence = characteristics.defence();
            this.developmentPointsUsed = characteristics.developmentPointsCost();
        }
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
     * @return
     */
    public abstract List<CreationMove> calculateLegalMoves(final Board board);

    public abstract List<Integer> getArea();

    public abstract KindOfParasite getType();

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
        if (this instanceof Colony)
        {
            int possibleOpponents = 0;
            int realNumberOfOpponents = 0;
            final HashSet<Player> opponents = new HashSet<>();

            //ParasitesUtils.logError("//// Une colonie " + getPosition() + " regarde ses agresseurs :");
            for (int neighbor : neighbors)
            {
                if (ParasitesUtils.isValidTile(getPosition() + neighbor))
                {
                    if (isFirstRowExclusion(getPosition(), neighbor)
                            || isLastRowExclusion(getPosition(), neighbor))
                    {
                        continue;
                    }
                    possibleOpponents++;

                    final Parasite opponent = actualBoard.getTile(getPosition() + neighbor).getParasite();
                    if (opponent != null)
                    {
                        realNumberOfOpponents++;
                        opponents.add(opponent.getPlayer());

                    } else
                    {
                        ParasitesUtils.logError("Cette case ne comporte pas d'agresseur");
                        break;
                    }
                }

            }
            //Condition : if there is only one opponent surrounding this colony
            if (possibleOpponents == realNumberOfOpponents && opponents.size() == 1)
            {
                Player invader = null;
                for (Iterator<Player> it = opponents.iterator(); it.hasNext(); )
                {
                    invader = it.next();
                }
                return invader;
            }
        }
        Player invader = null;
        Map<Player, Integer> neighborsPower = new HashMap<>();
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
                        if (neighborsPower.containsKey(closePlayer))
                        {
                            neighborsPower.put(closePlayer, neighborsPower.get(closePlayer) + closeParasite.getAttack());
                        } else
                        {
                            neighborsPower.put(closePlayer, closeParasite.getAttack());
                        }

                    }

                }
            }

        }
        final Iterator it = neighborsPower.entrySet().iterator();
        while (it.hasNext())
        {

            Map.Entry pair = (Map.Entry) it.next();
            ParasitesUtils.logInfos("key : " + pair.getKey() + "/ value : " + pair.getValue());
            if (((Integer) pair.getValue()) >= this.defence)
            {
                invader = (Player) pair.getKey();
            }
            it.remove(); // avoids a ConcurrentModificationException
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
        final Representation representation = getClass().getAnnotation(Representation.class);
        if (representation != null)
        {
            return representation.value();
        }
        return null;
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

    public String getSoundEffect()
    {
        final SoundEffect sound = getClass().getAnnotation(SoundEffect.class);
        if (sound != null)
        {
            return sound.value();
        }
        return null;
    }
}
