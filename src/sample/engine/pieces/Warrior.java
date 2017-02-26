package sample.engine.pieces;

import sample.Constants;
import sample.engine.board.Board;
import sample.engine.board.Move;
import sample.engine.board.Tile;
import sample.engine.players.Player;
import sample.utils.ParasitesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas Ecalle on 24/02/2017.
 */
public final class Warrior extends Parasite
{
    /**
     * Candidate placements are all the logical choices in order to create a parasite
     */
    public final static int[] CANDIDATE_PLACEMENTS = {1, -1, Board.DIMENSION, -Board.DIMENSION};


    public Warrior(int position, Player player)
    {
        super(position, player, 3, 2, 4, 5, Constants.WARRIOR_NAME, 1);
    }

    @Override
    public List<Move> calculateLagalMoves(Board board)
    {

        final List<Move> legalMoves = new ArrayList<>();
        for (int candidatePlacement : CANDIDATE_PLACEMENTS)
        {
            final int candidateDestination = this.position + candidatePlacement;
            if (ParasitesUtils.isValidTile(candidateDestination))
            {
                if (isFirstRowExclusion(position, candidateDestination)
                        || isLastRowExclusion(position, candidateDestination))
                {
                    continue;
                }
                final Tile destinationTile = board.getTile(candidateDestination);

                if (!destinationTile.isOccupied())
                {
                    legalMoves.add(new Move(board, this, candidateDestination));
                }
            }
        }
        return legalMoves;
    }


    @Override
    public String toString()
    {
        return "W";
    }

    private boolean isFirstRowExclusion(final int currentPosition, final int candidatePosition)
    {
        return (Board.firstRow[currentPosition] && (candidatePosition == -1));
    }

    private boolean isLastRowExclusion(final int currentPosition, final int candidatePosition)
    {
        return (Board.lastRow[currentPosition] && (candidatePosition == 1));
    }
}
