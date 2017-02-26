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
public class Builder extends Parasite
{

    /**
     * Candidate placements are all the logical choices in order to create a parasite
     */
    public final static int[] CANDIDATE_PLACEMENTS = {
            1, 2, -1, -2,                                                                                          //Up and Down
            Board.DIMENSION, Board.DIMENSION * 2,                                                                  //Right
            -Board.DIMENSION, -Board.DIMENSION * 2,                                                                  //Left
            Board.DIMENSION - 1, Board.DIMENSION + 1, -Board.DIMENSION + 1, -Board.DIMENSION - 1,                   // Diagonals
            -Board.DIMENSION * 2 - 2, -Board.DIMENSION * 2 + 2, Board.DIMENSION * 2 - 2, Board.DIMENSION * 2 + 2   // Special diagonals

    };

    public Builder(int position, Player player)
    {
        super(position, player, 2, 6, 1, 3, Constants.BUILDER_NAME, 1);
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
                        || isSecondColumnExclusion(position, candidateDestination)
                        || isBeforeLastColumnExclusion(position, candidateDestination)
                        || isLastColumnExclusion(position, candidateDestination)
                        || isFirstRowExclusion(position, candidateDestination)
                        || isSecondRowExclusion(position, candidateDestination)
                        || isBeforeLastRowExclusion(position, candidateDestination)
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

    @Override
    public String toString()
    {
        return "B";
    }

    private boolean isLastRowExclusion(int position, int candidateDestination)
    {
        return (Board.lastRow[position] && (candidateDestination == 1
                || candidateDestination == 2 || candidateDestination == -Board.DIMENSION + 1
                || candidateDestination == Board.DIMENSION + 1 || candidateDestination == -Board.DIMENSION * 2 + 2
                || candidateDestination == Board.DIMENSION * 2 + 2

        ));
    }

    private boolean isBeforeLastRowExclusion(int position, int candidateDestination)
    {
        return (Board.beforeLastRow[position] && (candidateDestination == 2
                || candidateDestination == -Board.DIMENSION * 2 + 2
                || candidateDestination == Board.DIMENSION * 2 + 2

        ));
    }

    private boolean isSecondRowExclusion(int position, int candidateDestination)
    {
        return (Board.secondRow[position] && (candidateDestination == -2
                || candidateDestination == -Board.DIMENSION * 2 - 2
                || candidateDestination == Board.DIMENSION * 2 - 2

        ));
    }

    private boolean isFirstRowExclusion(int position, int candidateDestination)
    {
        return (Board.firstRow[position] && (candidateDestination == -1
                || candidateDestination == -2 || candidateDestination == -Board.DIMENSION - 1
                || candidateDestination == Board.DIMENSION - 1 || candidateDestination == -Board.DIMENSION * 2 - 2
                || candidateDestination == Board.DIMENSION * 2 - 2

        ));
    }

    private boolean isLastColumnExclusion(int position, int candidateDestination)
    {
        return (Board.lastColumn[position] && (candidateDestination == Board.DIMENSION
                || candidateDestination == Board.DIMENSION * 2 || candidateDestination == Board.DIMENSION - 1
                || candidateDestination == Board.DIMENSION + 1 || candidateDestination == Board.DIMENSION * 2 - 2
                || candidateDestination == Board.DIMENSION * 2 + 2

        ));
    }

    private boolean isBeforeLastColumnExclusion(int position, int candidateDestination)
    {
        return (Board.beforeLastColumn[position] && (candidateDestination == Board.DIMENSION * 2
                || candidateDestination == Board.DIMENSION * 2 - 2
                || candidateDestination == Board.DIMENSION * 2 + 2
        ));
    }

    private boolean isSecondColumnExclusion(int position, int candidateDestination)
    {
        return (Board.secondColumn[position] && (candidateDestination == -Board.DIMENSION * 2
                || candidateDestination == -Board.DIMENSION * 2 - 2
                || candidateDestination == -Board.DIMENSION * 2 + 2
        ));
    }

    private boolean isFirstColumnExclusion(int position, int candidateDestination)
    {
        return (Board.firstColumn[position] && (candidateDestination == -Board.DIMENSION
                || candidateDestination == -Board.DIMENSION * 2 || candidateDestination == -Board.DIMENSION - 1
                || candidateDestination == -Board.DIMENSION + 1 || candidateDestination == -Board.DIMENSION * 2 - 2
                || candidateDestination == -Board.DIMENSION * 2 + 2

        ));
    }
}