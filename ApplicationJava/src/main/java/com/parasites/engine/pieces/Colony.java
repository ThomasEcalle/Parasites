package com.parasites.engine.pieces;

import com.parasites.annotations.Characteristics;
import com.parasites.engine.board.Board;
import com.parasites.engine.board.CreationMove;
import com.parasites.engine.board.Tile;
import com.parasites.engine.players.Player;
import com.parasites.annotations.Representation;
import com.parasites.utils.ParasitesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas Ecalle on 25/02/2017.
 */
@Representation("ic_colony.png")
@Characteristics(creationPoints = 6, attack = 2)
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
                            || isLastColumnExclusion(position, candidatePlacement)
                            || isFirstRowExclusion(position, candidatePlacement)
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
                        || isLastColumnExclusion(position, candidatePlacement)
                        || isFirstRowExclusion(position, candidatePlacement)
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
    public KindOfParasite getType()
    {
        return KindOfParasite.COLONY;
    }

    @Override
    public String toString()
    {
        return "C";

    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Colony)) return false;
        final Colony other = (Colony) obj;
        return other.position == position;
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
