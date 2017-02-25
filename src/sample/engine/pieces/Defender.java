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
 * Created by Thomas Ecalle on 25/02/2017.
 */
public final class Defender extends Parasite
{

    /**
     * Candidate placements are all the logical choices in order to create a parasite
     */
    public final static int[] CANDIDATE_PLACEMENTS = {1, 2, -1, -2, Board.DIMENSION, Board.DIMENSION * 2, -Board.DIMENSION, 2 * (-Board.DIMENSION)};

    public Defender(int position, Player player)
    {
        super(position, player, 2, 4, 2, 8, Constants.DEFENDER_NAME, 1);
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
                        || isSecondRowExclusion(position, candidateDestination)
                        || isBeforeLastRowExclusion(position, candidateDestination)
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


    private boolean isFirstRowExclusion(int position, int candidateDestination)
    {
        return (Board.firstRow[position] && (candidateDestination == -1 || candidateDestination == -2));
    }

    private boolean isSecondRowExclusion(int position, int candidateDestination)
    {
        return (Board.secondRow[position] && (candidateDestination == -2));
    }

    private boolean isBeforeLastRowExclusion(int position, int candidateDestination)
    {
        return (Board.beforeLastRow[position] && (candidateDestination == 2));
    }

    private boolean isLastRowExclusion(int position, int candidateDestination)
    {
        return (Board.lastRow[position] && (candidateDestination == 1 || candidateDestination == 2));
    }
}
