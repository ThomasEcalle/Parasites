package com.parasites.network.bo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas Ecalle on 12/05/2017.
 */
public final class Game
{
    private User creator;
    private int id;
    private int nbPlayerMax;
    private List<User> playersList;
    private int size;

    public Game(final User creator, final int id, final int nbPlayerMax, final int size)
    {
        this.creator = creator;
        this.id = id;
        this.nbPlayerMax = nbPlayerMax;
        this.size = size;

        playersList = new ArrayList<>();
        playersList.add(creator);
    }

    public void addUser(User user)
    {
        getPlayersList().add(user);
    }

    @Override
    public String toString()
    {
        return "Game[created by : " + creator.getPseudo()
                + ", id : " + getId()
                + ", nbPlayersMax : " + getNbPlayerMax()
                + ", Mapsize : " + getSize()
                + ", actual players = " + getPlayersList().toString()
                + ", NB PLAYERS : " + getActualPlayersCount() + "]";
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Game)) return false;
        final Game other = (Game) obj;
        return id == other.id;
    }


    @Override
    public int hashCode()
    {
        return id * nbPlayerMax + creator.getPseudo().length() * size;
    }

    public User getCreator()
    {
        return creator;
    }

    public String getCreatorPseudo()
    {
        return creator.getPseudo();
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getNbPlayerMax()
    {
        return nbPlayerMax;
    }

    public void setNbPlayerMax(int nbPlayerMax)
    {
        this.nbPlayerMax = nbPlayerMax;
    }

    public List<User> getPlayersList()
    {
        return playersList;
    }

    public void setPlayersList(List<User> playersList)
    {
        this.playersList = playersList;
    }

    public int getActualPlayersCount()
    {
        return playersList.size();
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }
}
