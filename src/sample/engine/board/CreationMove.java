package sample.engine.board;

import sample.engine.pieces.Parasite;
import sample.engine.players.Player;

/**
 * Created by Thomas Ecalle on 24/02/2017.
 */
public final class CreationMove
{
    public final Board board;
    public final Parasite originalParasite;
    public final Parasite createdParasite;

    public CreationMove(final Board board, final Parasite originParasite, final Parasite createdparasite)
    {
        this.board = board;
        this.originalParasite = originParasite;
        this.createdParasite = createdparasite;
    }

    public Board execute()
    {
        final Player parasitePlayer = originalParasite.getPlayer();
        parasitePlayer.setDevelopmentPoints(parasitePlayer.getDevelopmentPoints() - createdParasite.getDevelopmentPointsUsed());
        originalParasite.setCreationPoint(originalParasite.getCreationPoint() - createdParasite.getCost());

        final Board.Builder builder = new Board.Builder(board.DIMENSION, board.getPlayers());
        for (Player player : board.getPlayers())
        {
            for (Parasite parasiteOfPlayer : player.getParasites())
            {
                builder.setParasite(parasiteOfPlayer);
            }
        }

        builder.setParasite(originalParasite.createParasite(this));

        builder.setMoveMaker(board.getNextPlayer());
        return null;
    }

    @Override
    public int hashCode()
    {
        return board.DIMENSION * originalParasite.hashCode() * createdParasite.getPosition();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof CreationMove)) return false;
        final CreationMove other = (CreationMove) obj;

        return (this.originalParasite.equals(other.originalParasite)
                && this.createdParasite.equals(other.createdParasite));
    }

    public Parasite getOriginalParasite()
    {
        return originalParasite;
    }

    public Parasite getCreatedParasite()
    {
        return createdParasite;
    }
}
