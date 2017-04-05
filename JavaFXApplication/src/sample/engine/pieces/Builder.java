package sample.engine.pieces;

import sample.annotations.Characteristics;
import sample.annotations.Representation;
import sample.annotations.SoundEffect;
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
@Representation("ic_builder.png")
@Characteristics(cost = 2, creationPoints = 6, attack = 1, defence = 3)
@SoundEffect("button_sound.mp3")
public final class Builder extends Parasite
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
                    if (isFirstColumnExclusion(position, candidatePlacement)
                            || isSecondColumnExclusion(position, candidatePlacement)
                            || isBeforeLastColumnExclusion(position, candidatePlacement)
                            || isLastColumnExclusion(position, candidatePlacement)
                            || isFirstRowExclusion(position, candidatePlacement)
                            || isSecondRowExclusion(position, candidatePlacement)
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
                if (isFirstColumnExclusion(position, candidatePlacement)
                        || isSecondColumnExclusion(position, candidatePlacement)
                        || isBeforeLastColumnExclusion(position, candidatePlacement)
                        || isLastColumnExclusion(position, candidatePlacement)
                        || isFirstRowExclusion(position, candidatePlacement)
                        || isSecondRowExclusion(position, candidatePlacement)
                        || isBeforeLastRowExclusion(position, candidatePlacement)
                        || isLastRowExclusion(position, candidatePlacement)
                        )
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
        return "B";
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Builder)) return false;
        final Builder other = (Builder) obj;
        return other.position == position;
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
