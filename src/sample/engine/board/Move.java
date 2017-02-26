package sample.engine.board;

import sample.engine.pieces.Parasite;

/**
 * Created by Thomas Ecalle on 24/02/2017.
 */
public class Move
{
    public final Board board;
    public final Parasite parasite;
    public final int destination;

    public Move(final Board board, final Parasite parasite, final int destination)
    {
        this.board = board;
        this.parasite = parasite;
        this.destination = destination;
    }

    public Board execute()
    {

        return null;
    }
}
