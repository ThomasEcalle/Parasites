package JavaFXApplication.src.engine.pieces;

import JavaFXApplication.src.engine.board.Board;
import JavaFXApplication.src.engine.board.CreationMove;
import JavaFXApplication.src.engine.board.Tile;
import JavaFXApplication.src.engine.players.Player;
import JavaFXApplication.src.annotations.Characteristics;
import JavaFXApplication.src.annotations.Representation;
import JavaFXApplication.src.utils.ParasitesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas Ecalle on 25/02/2017.
 */


@Representation("ic_defender.png")
@Characteristics(cost = 2, creationPoints = 4, attack = 2, defence = 8)
public final class Defender extends Parasite
{

    /**
     * Candidate placements are all the logical choices in order to create a parasite
     */
    public final static int[] CANDIDATE_PLACEMENTS = {1, 2, -1, -2, Board.DIMENSION, Board.DIMENSION * 2, -Board.DIMENSION, 2 * (-Board.DIMENSION)};

    public Defender(int position, Player player)
    {
        super(position, player);
    }

    @Override
    public List<CreationMove> calculateLegalMoves(final Board board)
    {
        final List<CreationMove> legalCreationMoves = new ArrayList<>();
        this.actualBoard = board;
        for (int candidatePlacement : CANDIDATE_PLACEMENTS)
        {
            if (creationPoints > 0)
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
                            if ((player.getDevelopmentPoints() >= developmentPointsUsed) || player.getPlayingParasites().contains(this))
                            {
                                legalCreationMoves.add(new CreationMove(board, this, getParasiteObject(existingParasite, candidateDestination, player)));
                            }
                        }
                    }
                }
            }

        }
        return legalCreationMoves;
    }

    @Override
    public List<Integer> getArea()
    {
        final List<Integer> array = new ArrayList<>();
        for (int candidatePlacement : CANDIDATE_PLACEMENTS)
        {
            final int candidateDestination = this.position + candidatePlacement;
            if (ParasitesUtils.isValidTile(candidateDestination) && candidateDestination != this.position)
            {
                if (isFirstRowExclusion(position, candidatePlacement)
                        || isSecondRowExclusion(position, candidatePlacement)
                        || isBeforeLastRowExclusion(position, candidatePlacement)
                        || isLastRowExclusion(position, candidatePlacement))
                {
                    continue;
                }
                if (!actualBoard.getTile(candidateDestination).isOccupied())
                {
                    array.add(candidateDestination);
                }
            }
        }
        return array;
    }

    @Override
    public String toString()
    {
        return "D";
    }

    @Override
    public boolean equals(Object obj)
    {
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
