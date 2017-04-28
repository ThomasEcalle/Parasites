package com.parasites.engine.board;

import com.parasites.engine.pieces.Parasite;
import com.parasites.engine.players.Player;

/**
 * Created by Thomas Ecalle on 29/03/2017.
 */

public final class PassTurnMove extends Move
{
    public PassTurnMove(final Board board)
    {
        this.board = board;
    }

    @Override
    public Board execute()
    {
        final Board.Builder builder = new Board.Builder(Board.DIMENSION, board.getPlayers());

        final Player currentPlayer = board.getCurrentPlayer();
        for (Player player : board.getPlayers())
        {
            for (final Parasite parasiteOfPlayer : player.getParasites())
            {
                builder.setParasite(parasiteOfPlayer);
            }
        }

        if (!currentPlayer.isFirstMove())
        {
            builder.setMoveMaker(board.getNextPlayer());
        }

        return builder.build();
    }
}
