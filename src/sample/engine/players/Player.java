package sample.engine.players;

import sample.engine.board.Board;
import sample.engine.board.CreationMove;
import sample.engine.board.FirstMove;
import sample.engine.board.Move;
import sample.engine.pieces.Parasite;

import java.awt.*;
import java.util.List;

/**
 * Created by Thomas Ecalle on 24/02/2017.
 */
public final class Player
{
    private int id;
    private Color color;
    private int developmentPoints;

    private List<Parasite> parasites;
    private List<CreationMove> legalCreationMoves;
    private Board board;


    public Player()
    {
        developmentPoints = 2;
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

    public boolean isLegalMove(CreationMove creationMove)
    {
        return legalCreationMoves.contains(creationMove);
    }

    public MoveTransition makeMove(final Move move)
    {
        if (move instanceof FirstMove)
        {
            final Board transitionBoard = move.execute();

            return new MoveTransition(transitionBoard, move,MoveStatus.DONE);
        }
        else
        {
            final CreationMove creationMove = (CreationMove) move;
            if (!isLegalMove(creationMove))
            {
                return new MoveTransition(this.board, creationMove, MoveStatus.ILLEGAL_MOVE);
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
}
