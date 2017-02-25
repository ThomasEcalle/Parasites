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
public final class Colony extends Parasite
{

    /**
     * Candidate placements are all the logical choices in order to create a parasite
     */
    public final static int[] CANDIDATE_PLACEMENTS = {1, -1,
            Board.DIMENSION, -Board.DIMENSION, Board.DIMENSION + 1,
            Board.DIMENSION - 1, -Board.DIMENSION + 1, -Board.DIMENSION - 1};

    public Colony(int position, Player player)
    {
        super(position, player, Constants.INFINITE_VALUE, 6, 2, Constants.INFINITE_VALUE, Constants.COLONY_NAME, 1);
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
                if (isFirstColumnExclusion(position, candidateDestination)
                        || isLastColumnExclusion(position, candidateDestination)
                        || isFirstRowExclusion(position, candidateDestination)
                        || isLastRowExclusion(position, candidateDestination)
                        )
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

    private boolean isLastRowExclusion(int position, int candidateDestination)
    {
        return (Board.lastRow[position] && (candidateDestination == 1 || candidateDestination == Board.DIMENSION + 1 || candidateDestination == -Board.DIMENSION + 1));
    }

    private boolean isFirstRowExclusion(int position, int candidateDestination)
    {
        return (Board.firstRow[position] && (candidateDestination == -1 || candidateDestination == Board.DIMENSION - 1 || candidateDestination == -Board.DIMENSION - 1));
    }

    private boolean isLastColumnExclusion(int position, int candidateDestination)
    {
        return (Board.lastColumn[position] && candidateDestination == Board.DIMENSION - 1);
    }

    private boolean isFirstColumnExclusion(int position, int candidateDestination)
    {
        return (Board.firstColumn[position] && candidateDestination == -Board.DIMENSION + 1);
    }
}
