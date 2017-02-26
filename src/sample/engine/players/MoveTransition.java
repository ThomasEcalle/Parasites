package sample.engine.players;

import sample.engine.board.Board;
import sample.engine.board.Move;

/**
 * Created by Thomas Ecalle on 26/02/2017.
 */
public class MoveTransition
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
}
