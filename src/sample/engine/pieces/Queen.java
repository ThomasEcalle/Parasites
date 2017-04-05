package sample.engine.pieces;

import sample.annotations.Characteristics;
import sample.annotations.Representation;
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
@Representation("ic_queen.png")
@Characteristics(creationPoints = 10, attack = 3, defence = 8, developmentPointsCost = 2)
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

    public final static int[] OTHER_QUEEN_LOCKED_PLACEMENT = {
            1, 2, 3, 4, 5, -1, -2, -3, -4, -5,                                                                 //UP AND DOWN
            Board.DIMENSION, Board.DIMENSION * 2, Board.DIMENSION * 3, Board.DIMENSION * 4, Board.DIMENSION * 5,        //RIGHT
            -Board.DIMENSION, -Board.DIMENSION * 2, -Board.DIMENSION * 3, -Board.DIMENSION * 4, -Board.DIMENSION * 5,    //LEFT

            Board.DIMENSION + 1, Board.DIMENSION + 2, Board.DIMENSION + 3, Board.DIMENSION + 4, Board.DIMENSION - 1, Board.DIMENSION - 2, Board.DIMENSION - 3, Board.DIMENSION - 4,
            Board.DIMENSION * 2 + 1, Board.DIMENSION * 2 + 2, Board.DIMENSION * 2 + 3, Board.DIMENSION * 2 - 1, Board.DIMENSION * 2 - 2, Board.DIMENSION * 2 - 3,
            Board.DIMENSION * 3 + 1, Board.DIMENSION * 3 + 2, Board.DIMENSION * 3 - 1, Board.DIMENSION * 3 - 2,
            Board.DIMENSION * 4 + 1, Board.DIMENSION * 4 - 1,

            -Board.DIMENSION + 1, -Board.DIMENSION + 2, -Board.DIMENSION + 3, -Board.DIMENSION + 4, -Board.DIMENSION - 1,
            -Board.DIMENSION - 2, -Board.DIMENSION - 3, -Board.DIMENSION - 4,
            -Board.DIMENSION * 2 + 1, -Board.DIMENSION * 2 + 2, -Board.DIMENSION * 2 + 3, -Board.DIMENSION * 2 - 1, -Board.DIMENSION * 2 - 2, -Board.DIMENSION * 2 - 3,
            -Board.DIMENSION * 3 + 1, -Board.DIMENSION * 3 + 2, -Board.DIMENSION * 3 - 1, -Board.DIMENSION * 3 - 2,
            -Board.DIMENSION * 4 + 1, -Board.DIMENSION * 4 - 1
    };


    public Queen(int position, Player player)
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
                if (isLastColumnExclusion(position, candidatePlacement)
                        || isBeforeLastColumnExclusion(position, candidatePlacement)
                        || isSecondColumnExclusion(position, candidatePlacement)
                        || isFirstColumnExclusion(position, candidatePlacement)
                        || isLastRowExclusion(position, candidatePlacement)
                        || isBeforeLastRowExclusion(position, candidatePlacement)
                        || isBeforeBeforeLastRowExclusion(position, candidatePlacement)
                        || isFirstRowExclusion(position, candidatePlacement)
                        || isSecondRowExclusion(position, candidatePlacement)
                        || isThirdRowExclusion(position, candidatePlacement)
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
        return "Q";
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Queen)) return false;
        final Queen other = (Queen) obj;
        return other.position == position;
    }

    public boolean isClosedToAnotherQueen(final Board board)
    {
        for (int candidatePlacement : OTHER_QUEEN_LOCKED_PLACEMENT)
        {
            if (isLastColumnExclusion(position, candidatePlacement)
                    || isBeforeLastColumnExclusion(position, candidatePlacement)
                    || isSecondColumnExclusion(position, candidatePlacement)
                    || isFirstColumnExclusion(position, candidatePlacement)
                    || isLastRowExclusion(position, candidatePlacement)
                    || isBeforeLastRowExclusion(position, candidatePlacement)
                    || isBeforeBeforeLastRowExclusion(position, candidatePlacement)
                    || isBeforeBeforeLastRowPlusTwoExclusion(position, candidatePlacement)
                    || isBeforeBeforeLastRowPlusOneExclusion(position, candidatePlacement)
                    || isFourthRowExclusion(position, candidatePlacement)
                    || isFifthRowExclusion(position, candidatePlacement)
                    || isFirstRowExclusion(position, candidatePlacement)
                    || isSecondRowExclusion(position, candidatePlacement)
                    || isThirdRowExclusion(position, candidatePlacement)
                    )
            {
                continue;
            }
            final int destination = candidatePlacement + position;
            if (ParasitesUtils.isValidTile(destination))
            {
                if (board.getTile(destination).isOccupied() && board.getTile(destination).getParasite() instanceof Queen)
                {
                    ParasitesUtils.logError("yoooooooooooooooooooooooooooooo");
                    return true;
                }
            }

        }
        return false;
    }

    public List<Integer> getQueensWall()
    {
        final List<Integer> array = new ArrayList<>();
        for (int candidatePlacement : OTHER_QUEEN_LOCKED_PLACEMENT)
        {

            final int candidateDestination = this.position + candidatePlacement;
            if (ParasitesUtils.isValidTile(candidateDestination) && candidateDestination != this.position)
            {
                if (isLastColumnExclusion(position, candidatePlacement)
                        || isBeforeLastColumnExclusion(position, candidatePlacement)
                        || isSecondColumnExclusion(position, candidatePlacement)
                        || isFirstColumnExclusion(position, candidatePlacement)
                        || isLastRowExclusion(position, candidatePlacement)
                        || isBeforeLastRowExclusion(position, candidatePlacement)
                        || isBeforeBeforeLastRowExclusion(position, candidatePlacement)
                        || isBeforeBeforeLastRowPlusTwoExclusion(position, candidatePlacement)
                        || isBeforeBeforeLastRowPlusOneExclusion(position, candidatePlacement)
                        || isFourthRowExclusion(position, candidatePlacement)
                        || isFifthRowExclusion(position, candidatePlacement)
                        || isFirstRowExclusion(position, candidatePlacement)
                        || isSecondRowExclusion(position, candidatePlacement)
                        || isThirdRowExclusion(position, candidatePlacement)
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

    // last rows
    private boolean isBeforeBeforeLastRowPlusOneExclusion(int position, int candidateDestination)
    {
        return (Board.beforeBeforeLastRowPlusOne[position] && (candidateDestination == 5
                || candidateDestination == 4 || candidateDestination == Board.DIMENSION + 4
                || candidateDestination == -Board.DIMENSION + 4));
    }

    private boolean isBeforeBeforeLastRowPlusTwoExclusion(int position, int candidateDestination)
    {
        return (Board.beforeBeforeLastRowPlusTwo[position] && candidateDestination == 5);
    }

    private boolean isBeforeLastRowExclusion(int position, int candidateDestination)
    {
        return (Board.beforeLastRow[position] && (candidateDestination == 2
                || candidateDestination == 3
                || candidateDestination == 4 || candidateDestination == 5

                || candidateDestination == Board.DIMENSION + 4 || candidateDestination == -Board.DIMENSION + 4
                || candidateDestination == Board.DIMENSION + 3 || candidateDestination == Board.DIMENSION * 2 + 3
                || candidateDestination == -Board.DIMENSION + 3 || candidateDestination == -Board.DIMENSION * 2 + 3

                || candidateDestination == -Board.DIMENSION + 2 || candidateDestination == -Board.DIMENSION * 2 + 2 || candidateDestination == -Board.DIMENSION * 3 + 2
                || candidateDestination == Board.DIMENSION + 2 || candidateDestination == Board.DIMENSION * 2 + 2 || candidateDestination == Board.DIMENSION * 3 + 2
        ));
    }

    private boolean isLastRowExclusion(int position, int candidateDestination)
    {
        return (Board.lastRow[position] && (candidateDestination == 1
                || candidateDestination == 2 || candidateDestination == 3 || candidateDestination == 4 || candidateDestination == 5


                || candidateDestination == -Board.DIMENSION + 2 || candidateDestination == -Board.DIMENSION * 2 + 2 || candidateDestination == -Board.DIMENSION * 3 + 2
                || candidateDestination == Board.DIMENSION + 2 || candidateDestination == Board.DIMENSION * 2 + 2 || candidateDestination == Board.DIMENSION * 3 + 2

                || candidateDestination == -Board.DIMENSION + 1 || candidateDestination == -Board.DIMENSION * 2 + 1 || candidateDestination == -Board.DIMENSION * 3 + 1 || candidateDestination == -Board.DIMENSION * 4 + 1
                || candidateDestination == Board.DIMENSION + 1 || candidateDestination == Board.DIMENSION * 2 + 1 || candidateDestination == Board.DIMENSION * 3 + 1 || candidateDestination == Board.DIMENSION * 4 + 1

                || candidateDestination == Board.DIMENSION + 3 || candidateDestination == Board.DIMENSION * 2 + 3
                || candidateDestination == -Board.DIMENSION + 3 || candidateDestination == -Board.DIMENSION * 2 + 3

                || candidateDestination == Board.DIMENSION + 4 || candidateDestination == -Board.DIMENSION + 4
        ));
    }

    private boolean isBeforeBeforeLastRowExclusion(int position, int candidateDestination)
    {
        return (Board.beforeBeforeLastRow[position] && (candidateDestination == 3
                || candidateDestination == 4
                || candidateDestination == 5
                || candidateDestination == Board.DIMENSION + 4 || candidateDestination == -Board.DIMENSION + 4
                || candidateDestination == Board.DIMENSION + 3 || candidateDestination == -Board.DIMENSION + 3
                || candidateDestination == Board.DIMENSION * 2 + 3 || candidateDestination == -Board.DIMENSION * 2 + 3));
    }


    // firsts rows
    private boolean isFifthRowExclusion(int position, int candidateDestination)
    {
        return (Board.fifthRow[position] && candidateDestination == -5);
    }

    private boolean isFourthRowExclusion(int position, int candidateDestination)
    {
        return (Board.fourthRow[position] && (candidateDestination == -4
                || candidateDestination == -5
                || candidateDestination == -Board.DIMENSION - 4
                || candidateDestination == Board.DIMENSION - 4
        ));
    }

    private boolean isThirdRowExclusion(int position, int candidateDestination)
    {
        return (Board.thirdRow[position] && (candidateDestination == -3
                || candidateDestination == -4 || candidateDestination == -5

                || candidateDestination == -Board.DIMENSION - 3 || candidateDestination == -Board.DIMENSION - 4
                || candidateDestination == Board.DIMENSION - 3 || candidateDestination == Board.DIMENSION - 4

                || candidateDestination == -Board.DIMENSION * 2 - 3
                || candidateDestination == Board.DIMENSION * 2 - 3
        ));
    }

    private boolean isFirstRowExclusion(int position, int candidateDestination)
    {
        return (Board.firstRow[position] && (candidateDestination == -1
                || candidateDestination == -2 || candidateDestination == -3
                || candidateDestination == -4 || candidateDestination == -5

                || candidateDestination == -Board.DIMENSION - 1 || candidateDestination == -Board.DIMENSION - 2 || candidateDestination == -Board.DIMENSION - 3 || candidateDestination == -Board.DIMENSION - 4
                || candidateDestination == Board.DIMENSION - 1 || candidateDestination == Board.DIMENSION - 2 || candidateDestination == Board.DIMENSION - 3 || candidateDestination == Board.DIMENSION - 4

                || candidateDestination == -Board.DIMENSION * 2 - 1 || candidateDestination == -Board.DIMENSION * 2 - 2 || candidateDestination == -Board.DIMENSION * 2 - 3
                || candidateDestination == Board.DIMENSION * 2 - 1 || candidateDestination == Board.DIMENSION * 2 - 2 || candidateDestination == Board.DIMENSION * 2 - 3

                || candidateDestination == -Board.DIMENSION * 3 - 1 || candidateDestination == -Board.DIMENSION * 3 - 2
                || candidateDestination == Board.DIMENSION * 3 - 1 || candidateDestination == Board.DIMENSION * 3 - 2

                || candidateDestination == Board.DIMENSION * 4 - 1 || candidateDestination == -Board.DIMENSION * 4 - 1
        ));
    }

    private boolean isSecondRowExclusion(int position, int candidateDestination)
    {
        return (Board.secondRow[position] && (candidateDestination == -2
                || candidateDestination == -3
                || candidateDestination == -4 || candidateDestination == -5

                || candidateDestination == -Board.DIMENSION - 2 || candidateDestination == -Board.DIMENSION - 3 || candidateDestination == -Board.DIMENSION - 4
                || candidateDestination == Board.DIMENSION - 2 || candidateDestination == Board.DIMENSION - 3 || candidateDestination == Board.DIMENSION - 4

                || candidateDestination == -Board.DIMENSION * 2 - 2 || candidateDestination == -Board.DIMENSION * 2 - 3
                || candidateDestination == Board.DIMENSION * 2 - 2 || candidateDestination == Board.DIMENSION * 2 - 3

                || candidateDestination == -Board.DIMENSION * 3 - 2
                || candidateDestination == Board.DIMENSION * 3 - 2
        ));
    }


    //columns
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
