package JavaFXApplication.src.engine.players;

/**
 * Created by Thomas Ecalle on 26/02/2017.
 */
public enum MoveStatus
{
    DONE
            {
                @Override
                public boolean isDone()
                {
                    return true;
                }
            },

    ILLEGAL_MOVE
            {
                @Override
                public boolean isDone()
                {
                    return false;
                }
            };

    public abstract boolean isDone();
}
