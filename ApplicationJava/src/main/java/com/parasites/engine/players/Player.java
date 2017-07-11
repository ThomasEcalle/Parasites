package com.parasites.engine.players;

import com.parasites.engine.board.*;
import com.parasites.engine.pieces.Parasite;
import com.parasites.engine.pieces.Queen;
import com.parasites.network.bo.User;
import javafx.scene.paint.Color;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas Ecalle on 24/02/2017.
 */
public final class Player
{
    private int id;
    private Color color;
    private int developmentPoints;
    private String pseudo;

    private List<Parasite> parasites;
    private List<CreationMove> legalCreationMoves;
    private ArrayList<Parasite> playingParasites;
    private Queen queen;

    //Moves data
    private boolean isFirstMove = true;
    private boolean playedLastTurn = true;

    private boolean stillPlaying;

    // Server data
    private JSONObject jsonObject;


    public Player(final int id, final String pseudo, final Color color)

    {
        this.id = id;
        this.pseudo = pseudo;
        this.color = color;
        this.playingParasites = new ArrayList<>();
        this.stillPlaying = true;
        developmentPoints = 2;

        jsonObject = new JSONObject();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof User)
        {
            return ((User) obj).getPseudo().equals(pseudo);
        }
        if (!(obj instanceof Player)) return false;
        final Player other = (Player) obj;
        return this.pseudo.equals(other.pseudo);
    }

    @Override
    public int hashCode()
    {
        return id * parasites.size();
    }

    @Override
    public String toString()
    {
        return "[player : " + pseudo + ", development points : " + developmentPoints + ", first move : " + isFirstMove +
                " playing parasites are : " + playingParasites.toString() + "]";
    }

    public boolean isLegalMove(CreationMove creationMove)
    {
        return legalCreationMoves.contains(creationMove);
    }

    public MoveTransition makeMove(final Move move)
    {
        if (move instanceof FirstMove || move instanceof PassTurnMove)
        {
            final Board transitionBoard = move.execute();

            return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
        } else
        {
            final CreationMove creationMove = (CreationMove) move;
            if (!isLegalMove(creationMove))
            {
                return new MoveTransition(null, creationMove, MoveStatus.ILLEGAL_MOVE);
            }
            final Board transitionBoard = creationMove.execute();

            return new MoveTransition(transitionBoard, creationMove, MoveStatus.DONE);
        }

    }

    public List<Parasite> getParasites()
    {
        return parasites;
    }

    public void setParasites(List<Parasite> parasites)
    {
        this.parasites = parasites;
    }

    public void setLegalCreationMoves(List<CreationMove> legalCreationMoves)
    {
        this.legalCreationMoves = legalCreationMoves;
    }

    public Color getColor()
    {
        return color;
    }

    public int getDevelopmentPoints()
    {
        return developmentPoints;
    }

    public void setDevelopmentPoints(int developmentPoints)
    {
        this.developmentPoints = developmentPoints;
    }

    public void setFirstMove(boolean firstMove)
    {
        isFirstMove = firstMove;
    }

    public boolean isFirstMove()
    {
        return isFirstMove;
    }

    public ArrayList<Parasite> getPlayingParasites()
    {
        return playingParasites;
    }

    public void addPlayingparasite(final Parasite parasite)
    {
        if (playingParasites.size() < 2)
        {
            playingParasites.add(parasite);
        }
    }

    public void clearPlayingParasites()
    {
        for (Parasite playingParasite : playingParasites)
        {
            playingParasite.setAlreadyUsedInTurn(false);
        }
        playingParasites.clear();
    }

    public String getPseudo()
    {
        return pseudo;
    }

    public Queen getQueen()
    {
        return queen;
    }

    public void setQueen(Queen queen)
    {
        this.queen = queen;
    }

    public boolean isStillPlaying()
    {
        return stillPlaying;
    }

    public void setStillPlaying(boolean stillPlaying)
    {
        this.stillPlaying = stillPlaying;
    }

    public boolean hasPlayedLastTurn()
    {
        return playedLastTurn;
    }

    public void setPlayedLastTurn(boolean playedLastTurn)
    {
        this.playedLastTurn = playedLastTurn;
    }

    public JSONObject toJSON()
    {
        try
        {
            jsonObject.put("pseudo", pseudo);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public int getId()
    {
        return id;
    }
}
