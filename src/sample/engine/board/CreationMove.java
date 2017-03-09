package sample.engine.board;

import sample.engine.pieces.Parasite;
import sample.engine.players.Player;
import sample.utils.ParasitesUtils;

/**
 * Created by Thomas Ecalle on 24/02/2017.
 */
public final class CreationMove extends Move
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

    @Override
    public Board execute()
    {
        final Player currentPlayer = originalParasite.getPlayer();
        if (!originalParasite.isAlreadyUsedInTurn())
        {
            currentPlayer.setDevelopmentPoints(currentPlayer.getDevelopmentPoints() - originalParasite.getDevelopmentPointsUsed());
            originalParasite.setAlreadyUsedInTurn(true);
        }


        originalParasite.setCreationPoints(originalParasite.getCreationPoints() - createdParasite.getCost());
        createdParasite.setPlayer(originalParasite.getPlayer());

        final Board.Builder builder = new Board.Builder(board.DIMENSION, board.getPlayers());
        for (Player player : board.getPlayers())
        {
            for (Parasite parasiteOfPlayer : player.getParasites())
            {
                builder.setParasite(parasiteOfPlayer);
            }
        }

        builder.setParasite(createdParasite);
        if (isMovingPossible(currentPlayer))
        {
            builder.setMoveMaker(currentPlayer);
        } else
        {
            builder.setMoveMaker(board.getNextPlayer());
        }

        return builder.build();
    }

    private boolean isMovingPossible(final Player player)
    {
        if (player.getDevelopmentPoints() > 0) return true;
        for (Parasite parasite : player.getPlayingParasites())
        {
            ParasitesUtils.logError("parasite = " + parasite.toString());
            ParasitesUtils.logError("points = " + parasite.getCreationPoints());
            if (parasite.getCreationPoints() >= 2)
            {
                return true;
            }
        }

        return false;
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


    public Parasite getCreatedParasite()
    {
        return createdParasite;
    }

    public Parasite getOriginalParasite()
    {
        return originalParasite;
    }
}
