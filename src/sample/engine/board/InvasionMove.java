package sample.engine.board;

import sample.engine.pieces.Colony;
import sample.engine.pieces.Parasite;
import sample.engine.players.Player;

/**
 * Created by Thomas Ecalle on 09/03/2017.
 */
public class InvasionMove extends Move
{
    private Parasite loser;
    private Player winner;

    public InvasionMove(final Board board, final Parasite loser, final Player winner)
    {
        this.board = board;
        this.loser = loser;
        this.winner = winner;
    }

    @Override
    public Board execute()
    {
        final Player loserPlayer = loser.getPlayer();


        loserPlayer.getParasites().remove(loser);

        final Board.Builder builder = new Board.Builder(board.DIMENSION, board.getPlayers());
        for (Player player : board.getPlayers())
        {
            for (Parasite parasiteOfPlayer : player.getParasites())
            {
                if (parasiteOfPlayer.equals(loser))
                {
                    final Colony colony = new Colony(loser.getPosition(), winner);
                    builder.setParasite(colony);
                } else
                {
                    builder.setParasite(parasiteOfPlayer);
                }

            }
        }

        builder.setMoveMaker(board.getCurrentPlayer());


        return builder.build();
    }
}
