package sample.engine.pieces;

import com.sun.corba.se.impl.orbutil.DenseIntMapImpl;
import sample.Constants;
import sample.engine.board.Board;
import sample.engine.board.CreationMove;
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
    public List<CreationMove> calculateLegalMoves(Board board)
    {
        final List<CreationMove> legalCreationMoves = new ArrayList<>();

        for (int candidatePlacement : CANDIDATE_PLACEMENTS)
        {
            final int candidateDestination = this.position + candidatePlacement;
            if (ParasitesUtils.isValidTile(candidateDestination))
            {
                if (isFirstRowExclusion(position, candidatePlacement)
                        || isSecondRowExclusion(position, candidatePlacement)
                        || isBeforeLastRowExclusion(position, candidatePlacement)
                        || isLastRowExclusion(position, candidatePlacement))
                {
                    continue;
                }
                final Tile destinationTile = board.getTile(candidateDestination);

                if (!destinationTile.isOccupied())
                {
                    for (KindOfParasite existingParasite : board.EXISTING_PARASITES)
                    {
                        if (existingParasite.cost <= creationPoint
                                && player.getDevelopmentPoints() >= developmentPointsUsed)
                        {
                            legalCreationMoves.add(new CreationMove(board, this, getParasiteObject(existingParasite, candidateDestination, player)));
                        }
                    }
                }
            }
        }
        return legalCreationMoves;
    }

    @Override
    public String toString()
    {
        return "D";
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) return false;
        if (!(obj instanceof Defender)) return false;
        final Defender other = (Defender) obj;
        return other.position == position;
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
