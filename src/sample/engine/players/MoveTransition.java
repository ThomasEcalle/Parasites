package sample.engine.players;

import sample.engine.board.Board;
import sample.engine.board.CreationMove;

/**
 * Created by Thomas Ecalle on 26/02/2017.
 */
public final class MoveTransition
{
    private final Board transitionBoard;
    private final CreationMove creationMove;
    private final MoveStatus moveStatus;

    public MoveTransition(Board transitionBoard, CreationMove creationMove, MoveStatus moveStatus)
    {
        this.transitionBoard = transitionBoard;
        this.creationMove = creationMove;
        this.moveStatus = moveStatus;
    }
}
