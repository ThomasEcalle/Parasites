package sample.engine.board;

import sample.engine.pieces.Parasite;

/**
 * Created by Thomas Ecalle on 06/03/2017.
 */
public abstract class Move
{
    protected Parasite createdParasite;

    public abstract Board execute();

    public Parasite getCreatedParasite()
    {
        return createdParasite;
    }
}
