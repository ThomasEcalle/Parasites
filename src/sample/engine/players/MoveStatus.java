package sample.engine.players;

/**
 * Created by Thomas Ecalle on 26/02/2017.
 */
public enum MoveStatus
{
    DONE
            {
                @Override
                boolean isDone()
                {
                    return true;
                }
            },

    ILLEGAL_MOVE
            {
                @Override
                boolean isDone()
                {
                    return false;
                }
            };

    abstract boolean isDone();
}
