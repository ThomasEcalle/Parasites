package com.parasites.controllers;

import com.parasites.annotations.ColumnFieldTarget;
import com.parasites.annotations.PressEnter;
import com.parasites.network.interfaces.OnlineServerObserver;
import com.parasites.utils.Toast;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Thomas Ecalle on 13/05/2017.
 */
public abstract class ParasitesFXController implements OnlineServerObserver, Initializable
{
    protected Stage stage;

    /**
     * Needed method in order to parse Controllers annotations
     *
     * @param controller
     */
    protected void parseAnnotations(final Object controller)
    {
        final Class controllerClass = controller.getClass();
        for (final Field field : controllerClass.getDeclaredFields())
        {
            if (field.isAnnotationPresent(PressEnter.class))
            {

                field.setAccessible(true);
                try
                {
                    final PressEnter pressEnter = field.getAnnotation(PressEnter.class);
                    final TextField textField = (TextField) field.get(controller);

                    textField.setOnKeyPressed((keyEvent) ->
                    {
                        if (keyEvent.getCode() == KeyCode.ENTER)
                        {
                            try
                            {
                                final Method method = controllerClass.getDeclaredMethod(pressEnter.value());
                                method.setAccessible(true);
                                method.invoke(controller);

                            } catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });


                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            if (field.isAnnotationPresent(ColumnFieldTarget.class))
            {

                field.setAccessible(true);
                try
                {
                    final ColumnFieldTarget columnFieldTarget = field.getAnnotation(ColumnFieldTarget.class);

                    final TableColumn column = (TableColumn) field.get(controller);
                    column.setCellValueFactory(new PropertyValueFactory<>(columnFieldTarget.value()));


                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }


    }

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
     * Refresh a ListView
     *
     * @param list
     * @param listView
     */
    protected void refreshListView(final List<?> list, final ListView listView)
    {
        Platform.runLater(() ->
        {
            listView.setItems(FXCollections.observableArrayList(list));
            listView.refresh();
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


    public Stage getStage()
    {
        return stage;
    }
}
