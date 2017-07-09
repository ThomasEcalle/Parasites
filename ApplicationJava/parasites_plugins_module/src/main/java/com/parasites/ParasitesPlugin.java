package com.parasites;

import javafx.scene.Node;

/**
 * Created by Thomas Ecalle on 09/07/2017.
 */
public interface ParasitesPlugin
{
    String getName();

    void setUserToken(final int id, final String token);

    Node getContent();
}
