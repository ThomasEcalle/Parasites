package com.parasites.utils;

import javafx.scene.paint.Color;

/**
 * Created by spyro on 24/05/2017.
 */
public class InfoMessage {
    public String getMessage() {
        return message;
    }

    public Color getColor() {
        return color;
    }

    private String message;
    private Color color;

    public InfoMessage(String message, Color color){
        this.message = message;
        this.color = color;
    }
}
