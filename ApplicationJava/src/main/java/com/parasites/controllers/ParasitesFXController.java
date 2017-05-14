package com.parasites.controllers;

import com.parasites.network.OnlineServerObservable;
import com.parasites.utils.Toast;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.List;

/**
 * Created by Thomas Ecalle on 13/05/2017.
 */
public abstract class ParasitesFXController implements OnlineServerObservable
{
    protected Stage stage;

    public void setStage(final Stage stage)
    {
        this.stage = stage;
    }

    /**
     * Set a text in a label , out of the Main Thread
     *
     * @param label
     * @param message
     */
    protected void setTextInLabel(final Label label, final String message)
    {
        Platform.runLater(() ->
        {
            label.setText(message);
            label.setVisible(true);
        });
    }

    /**
     * Refresh a TableView
     *
     * @param list
     * @param tableView
     */
    protected void refreshTableView(final List<?> list, final TableView tableView)
    {
        Platform.runLater(() ->
        {
            tableView.setItems(FXCollections.observableArrayList(list));
            tableView.refresh();
        });

    }

    /**
     * Show a notification as the Android toats
     *
     * @param message
     */
    protected void showNotification(final String message)
    {
        int toastMsgTime = 3000; //3.5 seconds
        int fadeInTime = 500; //0.5 seconds
        int fadeOutTime = 500; //0.5 seconds
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(stage, message, toastMsgTime, fadeInTime, fadeOutTime);
            }
        });
    }

}
