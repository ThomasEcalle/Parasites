package sample.engine.board;

import sample.engine.pieces.Parasite;
import sample.engine.players.Player;
import sample.utils.ParasitesUtils;

/**
 * Created by Thomas Ecalle on 24/02/2017.
 */
public final class FirstMove extends Move
{
    public final Parasite createdParasite;

    public FirstMove(final Board board, final Parasite createdparasite)
    {
        this.board = board;
        this.createdParasite = createdparasite;
    }

    @Override
    public Board execute()
    {
        final Player parasitePlayer = createdParasite.getPlayer();
        parasitePlayer.setDevelopmentPoints(parasitePlayer.getDevelopmentPoints() - createdParasite.getDevelopmentPointsUsed());
        ParasitesUtils.logInfos("development points after = " + parasitePlayer.getDevelopmentPoints());
        final Board.Builder builder = new Board.Builder(board.DIMENSION, board.getPlayers());
        for (Player player : board.getPlayers())
        {
            for (Parasite parasiteOfPlayer : player.getParasites())
            {
                builder.setParasite(parasiteOfPlayer);
            }
        }

        builder.setParasite(createdParasite);

        if (parasitePlayer.getDevelopmentPoints() <= 0)
        {
            builder.setMoveMaker(board.getNextPlayer());
        }
        else
        {
            builder.setMoveMaker(parasitePlayer);
        }
        return builder.build();
    }

    @Override
    public int hashCode()
    {
        return board.DIMENSION * createdParasite.hashCode() * createdParasite.getPosition();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof CreationMove)) return false;
        final CreationMove other = (CreationMove) obj;

        return (this.createdParasite.equals(other.createdParasite));


    }

    public Parasite getCreatedParasite()
    {
        return createdParasite;
    }
}
