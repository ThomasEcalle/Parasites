package JavaFXApplication.src.engine.pieces;

import JavaFXApplication.src.annotations.SoundEffect;
import JavaFXApplication.src.engine.board.Board;
import JavaFXApplication.src.engine.board.Tile;
import JavaFXApplication.src.engine.players.Player;
import JavaFXApplication.src.annotations.Characteristics;
import JavaFXApplication.src.annotations.Representation;
import JavaFXApplication.src.engine.board.CreationMove;
import JavaFXApplication.src.utils.ParasitesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas Ecalle on 24/02/2017.
 */
@Representation("ic_warrior.png")
@Characteristics(cost = 3, creationPoints = 2, attack = 4, defence = 5)
@SoundEffect("button_sound_2.mp3")
public final class Warrior extends Parasite
{
    /**
     * Candidate placements are all the logical choices in order to create a parasite
     */
    public final static int[] CANDIDATE_PLACEMENTS = {1, -1, Board.DIMENSION, -Board.DIMENSION};

    public Warrior(int position, Player player)
    {
        super(position, player);
    }

    @Override
    public List<CreationMove> calculateLegalMoves(final Board board)
    {
        this.actualBoard = board;
        final List<CreationMove> legalCreationMoves = new ArrayList<>();
        for (int candidatePlacement : CANDIDATE_PLACEMENTS)
        {
            if (creationPoints > 0)
            {
                final int candidateDestination = this.position + candidatePlacement;
                if (ParasitesUtils.isValidTile(candidateDestination))
                {
                    if (isFirstRowExclusion(position, candidatePlacement)
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
        return "W";
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Warrior)) return false;
        final Warrior other = (Warrior) obj;
        return other.position == position;
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
