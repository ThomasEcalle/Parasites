package com.parasites.controllers;

import com.parasites.network.OnlineServerObservable;
import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 * Created by Thomas Ecalle on 13/05/2017.
 */
public abstract class ParasitesFXController implements OnlineServerObservable
{

    protected void setTextInLabel(final Label label, final String message)
    {
        Platform.runLater(() ->
        {
            label.setText(message);
            label.setVisible(true);
        });
    }

}
