package sample.engine.pieces;

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
public final class Queen extends Parasite
{
    /**
     * Candidate placements are all the logical choices in order to create a parasite
     */
    public final static int[] CANDIDATE_PLACEMENTS = {
            1, 2, 3, -1, -2, -3,                                                                                          //Up and Down
            Board.DIMENSION, Board.DIMENSION * 2, Board.DIMENSION * 3,                                                  //Right
            -Board.DIMENSION, -Board.DIMENSION * 2, -Board.DIMENSION * 3,                                            //Left
            Board.DIMENSION - 1, Board.DIMENSION + 1, -Board.DIMENSION + 1, -Board.DIMENSION - 1,                   // Diagonals
            Board.DIMENSION * 2 + 1, Board.DIMENSION * 2 - 1, -Board.DIMENSION * 2 + 1, -Board.DIMENSION * 2 - 1,  // Special angles
            Board.DIMENSION - 2, Board.DIMENSION + 2, -Board.DIMENSION - 2, -Board.DIMENSION + 2                    // SPecial angles

    };


    public Queen(int position, Player player)
    {
        super(position, player, Constants.INFINITE_VALUE, 10, 3, 8, Constants.QUEEN_NAME, 2);
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
                if (isFirstColumnExclusion(position, candidatePlacement)
                        || isSecondColumnExclusion(position, candidatePlacement)
                        || isBeforeLastColumnExclusion(position, candidatePlacement)
                        || isLastColumnExclusion(position, candidatePlacement)
                        || isFirstRowExclusion(position, candidatePlacement)
                        || isSecondRowExclusion(position, candidatePlacement)
                        || isThirdRowExclusion(position, candidatePlacement)
                        || isBeforeBeforeLastRowExclusion(position, candidatePlacement)
                        || isBeforeLastRowExclusion(position, candidatePlacement)
                        || isLastRowExclusion(position, candidatePlacement)
                        )
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
        return "Q";
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) return false;
        if (!(obj instanceof Queen)) return false;
        final Queen other = (Queen) obj;
        return other.position == position;
    }

    private boolean isBeforeLastRowExclusion(int position, int candidateDestination)
    {
        return (Board.beforeLastRow[position] && (candidateDestination == 2
                || candidateDestination == 3 || candidateDestination == -Board.DIMENSION + 2
                || candidateDestination == Board.DIMENSION + 2
        ));
    }

    private boolean isLastRowExclusion(int position, int candidateDestination)
    {
        return (Board.lastRow[position] && (candidateDestination == 1
                || candidateDestination == 2 || candidateDestination == 3
                || candidateDestination == -Board.DIMENSION + 1 || candidateDestination == -Board.DIMENSION + 2
                || candidateDestination == Board.DIMENSION + 1 || candidateDestination == Board.DIMENSION + 2
                || candidateDestination == -Board.DIMENSION * 2 + 1 || candidateDestination == Board.DIMENSION * 2 + 1
        ));
    }

    private boolean isBeforeBeforeLastRowExclusion(int position, int candidateDestination)
    {
        return (Board.beforeBeforeLastRow[position] && candidateDestination == 3);
    }

    private boolean isThirdRowExclusion(int position, int candidateDestination)
    {
        return (Board.thirdRow[position] && candidateDestination == -3);
    }

    private boolean isFirstRowExclusion(int position, int candidateDestination)
    {
        return (Board.firstRow[position] && (candidateDestination == -1
                || candidateDestination == -2 || candidateDestination == -3
                || candidateDestination == -Board.DIMENSION - 1 || candidateDestination == -Board.DIMENSION - 2
                || candidateDestination == Board.DIMENSION - 1 || candidateDestination == Board.DIMENSION - 2
                || candidateDestination == -Board.DIMENSION * 2 - 1 || candidateDestination == Board.DIMENSION * 2 - 1
        ));
    }

    private boolean isSecondRowExclusion(int position, int candidateDestination)
    {
        return (Board.secondRow[position] && (candidateDestination == -2
                || candidateDestination == -3 || candidateDestination == -Board.DIMENSION - 2
                || candidateDestination == Board.DIMENSION - 2
        ));
    }

    private boolean isFirstColumnExclusion(int position, int candidateDestination)
    {
        return (Board.firstColumn[position] && (candidateDestination == -Board.DIMENSION
                || candidateDestination == -Board.DIMENSION * 2 || candidateDestination == -Board.DIMENSION * 3
                || candidateDestination == -Board.DIMENSION - 1 || candidateDestination == -Board.DIMENSION - 2
                || candidateDestination == -Board.DIMENSION + 1 || candidateDestination == -Board.DIMENSION + 2
                || candidateDestination == -Board.DIMENSION * 2 + 1 || candidateDestination == -Board.DIMENSION * 2 - 1))
                ;
    }

    private boolean isSecondColumnExclusion(int position, int candidateDestination)
    {
        return (Board.secondColumn[position] && (candidateDestination == -Board.DIMENSION * 2
                || candidateDestination == -Board.DIMENSION * 3
                || candidateDestination == -Board.DIMENSION * 2 + 1 || candidateDestination == -Board.DIMENSION * 2 - 1));
    }

    private boolean isLastColumnExclusion(int position, int candidateDestination)
    {
        return (Board.lastColumn[position] && (candidateDestination == Board.DIMENSION
                || candidateDestination == Board.DIMENSION * 2 || candidateDestination == Board.DIMENSION * 3
                || candidateDestination == Board.DIMENSION + 1 || candidateDestination == Board.DIMENSION + 2
                || candidateDestination == Board.DIMENSION - 1 || candidateDestination == Board.DIMENSION - 2
                || candidateDestination == Board.DIMENSION * 2 + 1 || candidateDestination == Board.DIMENSION * 2 - 1));
    }

    private boolean isBeforeLastColumnExclusion(int position, int candidateDestination)
    {
        return (Board.beforeLastColumn[position] && (candidateDestination == Board.DIMENSION * 2
                || candidateDestination == Board.DIMENSION * 3
                || candidateDestination == Board.DIMENSION * 2 + 1 || candidateDestination == Board.DIMENSION * 2 - 1));
    }
}
