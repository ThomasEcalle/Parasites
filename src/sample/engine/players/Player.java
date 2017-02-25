package sample.engine.players;

import sample.engine.board.Move;
import sample.engine.pieces.Parasite;

import java.util.List;

/**
 * Created by Thomas Ecalle on 24/02/2017.
 */
public class Player
{
    private int id;
    private List<Parasite> parasites;
    private List<Move> legalMoves;


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

    public List<Move> getLegalMoves()
    {
        return legalMoves;
    }

    public void setLegalMoves(List<Move> legalMoves)
    {
        this.legalMoves = legalMoves;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) return false;
        if (!(obj instanceof Player)) return false;
        final Player other = (Player) obj;
        return this.id == other.id;
    }


    @Override
    public int hashCode()
    {
        return id * parasites.size();
    }
}
