package com.parasites.view;

import com.parasites.engine.GameManager;
import com.parasites.engine.board.Board;
import com.parasites.engine.board.Tile;
import com.parasites.engine.pieces.KindOfParasite;
import com.parasites.engine.pieces.Parasite;
import com.parasites.network.OnlineServerManager;
import com.parasites.utils.ParasitesUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;

/**
 * Created by Thomas Ecalle on 26/02/2017.
 */
public final class GraphicTile extends Tile implements EventHandler<Event>
{
    private boolean isTileLocked;


    public GraphicTile(final double w, final double h, final Paint paint, final int tileCoordonate, final Parasite parasite, final boolean isTileLocked)
    {
        super(w, h, paint, tileCoordonate, parasite);
        this.isTileLocked = isTileLocked;
        setOnMouseClicked(this);
    }

    @Override
    public void handle(Event event)
    {
        if (MouseEvent.MOUSE_CLICKED == event.getEventType())
        {

            if (OnlineServerManager.getInstance().getCurrentUser().equals(GameManager.getInstance().getCurrentPlayer()))
            {
                final KindOfParasite kindOfParasite = Board.chosenParasite == null ? null : Board.chosenParasite.getType();
                OnlineServerManager.getInstance().playMove(getTileCoordonate(), isTileLocked, kindOfParasite);
            }
        }
    }


    private void soudGestion(final Parasite created)
    {
        if (created.getSoundEffect() != null)
        {
            final Media sound = new Media(ParasitesUtils.getResourceUrl(created.getSoundEffect(), getClass()));
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        }
    }


}
