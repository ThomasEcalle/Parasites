package sample.engine.players;

import javafx.scene.paint.Color;
import sample.engine.board.Board;
import sample.engine.board.CreationMove;
import sample.engine.board.FirstMove;
import sample.engine.board.Move;
import sample.engine.pieces.Parasite;
import sample.utils.ParasitesUtils;

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
    private boolean isFirstMove = true;


    public Player(final String pseudo, final Color color)

    {
        this.pseudo = pseudo;
        this.color = color;
        this.playingParasites = new ArrayList<>();
        developmentPoints = 2;
    }

    @Override
    public boolean equals(Object obj)
    {
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
        if (move instanceof FirstMove)
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

    public int getId()
    {
        return id;
    }

    public List<CreationMove> getLegalCreationMoves()
    {
        return legalCreationMoves;
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
}
