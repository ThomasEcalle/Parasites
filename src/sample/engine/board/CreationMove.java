package sample.engine.board;

import sample.engine.pieces.Parasite;
import sample.engine.players.Player;

/**
 * Created by Thomas Ecalle on 24/02/2017.
 */
public class CreationMove
{
    public final Board board;
    public final Parasite parasite;
    public final Parasite createdParasite;

    public CreationMove(final Board board, final Parasite originParasite, final Parasite createdparasite)
    {
        this.board = board;
        this.parasite = originParasite;
        this.createdParasite = createdparasite;
    }

    public Board execute()
    {
        final Player parasitePlayer = parasite.getPlayer();
        parasitePlayer.setDevelopmentPoints(parasitePlayer.getDevelopmentPoints() - createdParasite.getDevelopmentPointsUsed());
        parasite.setCreationPoint(parasite.getCreationPoint() - createdParasite.getCost());

        final Board.Builder builder = new Board.Builder(board.DIMENSION, board.getPlayers());
        for (Player player : board.getPlayers())
        {
            for (Parasite parasiteOfPlayer : player.getParasites())
            {
                builder.setParasite(parasiteOfPlayer);
            }
        }

        builder.setParasite(parasite.createParasite(this));

        builder.setMoveMaker(board.getNextPlayer());
        return null;
    }

    @Override
    public int hashCode()
    {
        return board.DIMENSION * parasite.hashCode() * createdParasite.getPosition();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if (!(obj instanceof CreationMove)) return false;
        final CreationMove other = (CreationMove) obj;

        return (this.parasite.getPosition() == other.parasite.getPosition()
                && this.createdParasite.getPosition() == other.createdParasite.getPosition());
    }
}
