package JavaFXApplication.src.engine.board;

/**
 * Created by Thomas Ecalle on 06/03/2017.
 */
public abstract class Move
{
    protected Board board;

    public abstract Board execute();
}
