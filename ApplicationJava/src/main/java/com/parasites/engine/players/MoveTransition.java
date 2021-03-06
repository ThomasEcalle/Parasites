package com.parasites.engine.players;

import com.parasites.engine.board.Board;
import com.parasites.engine.board.Move;

/**
 * Created by Thomas Ecalle on 26/02/2017.
 */
public final class MoveTransition
{
    private final Board transitionBoard;
    private final Move move;
    private final MoveStatus moveStatus;

    public MoveTransition(Board transitionBoard, Move move, MoveStatus moveStatus)
    {
        this.transitionBoard = transitionBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public Board getTransitionBoard()
    {
        return transitionBoard;
    }

    public MoveStatus getMoveStatus()
    {
        return moveStatus;
    }
}
