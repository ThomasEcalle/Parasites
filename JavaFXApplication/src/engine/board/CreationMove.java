package JavaFXApplication.src.engine.board;

import JavaFXApplication.src.engine.pieces.Parasite;
import JavaFXApplication.src.engine.pieces.Queen;
import JavaFXApplication.src.engine.players.Player;
import JavaFXApplication.src.engine.pieces.Colony;
import JavaFXApplication.src.utils.ParasitesUtils;

/**
 * Created by Thomas Ecalle on 24/02/2017.
 */
public final class CreationMove extends Move
{
    public final Parasite originalParasite;
    public final Parasite createdParasite;

    public CreationMove(final Board board, final Parasite originParasite, final Parasite createdparasite)
    {
        this.board = board;
        this.originalParasite = originParasite;
        this.createdParasite = createdparasite;
    }

    @Override
    public Board execute()
    {

        final Player currentPlayer = originalParasite.getPlayer();

        if (!originalParasite.isAlreadyUsedInTurn())
        {
            currentPlayer.setDevelopmentPoints(currentPlayer.getDevelopmentPoints() - originalParasite.getDevelopmentPointsUsed());
            originalParasite.setAlreadyUsedInTurn(true);
        }


        originalParasite.setCreationPoints(originalParasite.getCreationPoints() - createdParasite.getCost());
        createdParasite.setPlayer(originalParasite.getPlayer());

        currentPlayer.getParasites().add(createdParasite);
        board.getTile(createdParasite.getPosition()).setParasite(createdParasite);

        final boolean isNotLastTurn = isMovingPossible(currentPlayer);
        final Board.Builder builder = new Board.Builder(board.DIMENSION, board.getPlayers());

        builder.setParasite(createdParasite);
        for (Player player : board.getPlayers())
        {
            Player queenKiller = null;

            if (isNotLastTurn == false)
            {
                // We, first of all, verify that player's queen is not killed at this turn, which is a special turn
                if (player.getQueen().mustSurrender() != null)
                {
                    ParasitesUtils.logError("QUEEN of " + player.getPseudo() + " is going to be killed by " + player.getQueen().mustSurrender().getPseudo());
                    queenKiller = player.getQueen().mustSurrender();

                    player.setStillPlaying(false);
                    Colony colony = new Colony(player.getQueen().getPosition(), queenKiller);
                    board.getTile(player.getQueen().getPosition()).setParasite(colony);

                    for (Parasite parasite : player.getParasites())
                    {
                        parasite.setPlayer(queenKiller);
                        builder.setParasite(parasite);
                    }

                    builder.setParasite(colony);
                    continue;
                }

            }

            if (!player.equals(currentPlayer))
            {

                // then, for a normal turn, we look for other players's parasites
                for (Parasite parasiteOfPlayer : player.getParasites())
                {
                    if (isNotLastTurn == false)
                    {

                        ParasitesUtils.logWarnings("test de surrender " + parasiteOfPlayer.getPosition());
                        Player invader = parasiteOfPlayer.mustSurrender();
                        if (invader != null)
                        {

                            Colony colony = new Colony(parasiteOfPlayer.getPosition(), invader);
                            board.getTile(parasiteOfPlayer.getPosition()).setParasite(colony);
                            parasiteOfPlayer = colony;
                        }
                        parasiteOfPlayer.setCreationPoints(parasiteOfPlayer.getInitialCreationPoints());
                    }
                    builder.setParasite(parasiteOfPlayer);
                }
            }
        }

        for (Parasite parasiteOfPlayer : currentPlayer.getParasites())
        {
            if (isNotLastTurn == false)
            {
                final Player invader = parasiteOfPlayer.mustSurrender();
                if (invader != null)
                {
                    ParasitesUtils.logError("found a parasite to kill, it is a parasite of " + currentPlayer.getPseudo());

                    if (parasiteOfPlayer instanceof Queen)
                    {
                        currentPlayer.setStillPlaying(false);
                    }

                    final Colony colony = new Colony(parasiteOfPlayer.getPosition(), invader);
                    parasiteOfPlayer = colony;
                }
                parasiteOfPlayer.setCreationPoints(parasiteOfPlayer.getInitialCreationPoints());
            }
            builder.setParasite(parasiteOfPlayer);
        }


        if (isNotLastTurn)
        {
            builder.setMoveMaker(currentPlayer);
        } else
        {
            builder.setMoveMaker(board.getNextPlayer());
        }

        return builder.build();
    }

    private boolean isMovingPossible(final Player player)
    {
        if (player.getDevelopmentPoints() > 0) return true;
        for (Parasite parasite : player.getPlayingParasites())
        {
            if (parasite.getCreationPoints() >= 2 && parasite.calculateLegalMoves(board).size() > 0)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return board.DIMENSION * originalParasite.hashCode() * createdParasite.getPosition();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof CreationMove)) return false;
        final CreationMove other = (CreationMove) obj;

        return (this.originalParasite.equals(other.originalParasite)
                && this.createdParasite.equals(other.createdParasite));
    }


    public Parasite getCreatedParasite()
    {
        return createdParasite;
    }

    public Parasite getOriginalParasite()
    {
        return originalParasite;
    }

}
