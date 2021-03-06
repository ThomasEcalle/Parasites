package com.parasites.view;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import com.parasites.Constants;
import com.parasites.engine.pieces.*;
import com.parasites.utils.ParasitesUtils;

/**
 * Created by Thomas Ecalle on 26/02/2017.
 */
public final class GraphicParasitesChoices extends GridPane
{
    private Parasite[] parasites = new Parasite[]{
            new Queen(-1, null),
            new Builder(-1, null),
            new Warrior(-1, null),
            new Defender(-1, null)
    };

    private Image[] images = new Image[]{
            new Image(ParasitesUtils.getResourceUrl(Constants.QUEEN_NAME, getClass())),
            new Image(ParasitesUtils.getResourceUrl(Constants.BUILDER_NAME, getClass())),
            new Image(ParasitesUtils.getResourceUrl(Constants.WARRIOR_NAME, getClass())),
            new Image(ParasitesUtils.getResourceUrl(Constants.DEFENDER_NAME, getClass()))
    };

    public GraphicParasitesChoices()
    {
        super();
        generate(images.length);

    }

    private void generate(int numberOfParasites)
    {
        for (int i = 0; i < numberOfParasites; i++)
        {
            final ImageView imageView = new ImageView(images[i]);
            add(imageView, 0, i);
        }
    }


    public void hideParasite(final Parasite parasite)
    {
        final int creationPoints = parasite.getCreationPoints();
        for (int i = 0; i < parasites.length; i++)
        {
            if (parasites[i].getCost() > creationPoints)
            {
                System.out.println("not enough creation points to create a : " + parasites[i].toString());
                final Label label = new Label();

                getChildren().remove(i);
                add(label, 0, i);
            } else
            {
                final ImageView imageView = new ImageView(images[i]);
                add(imageView, 0, i);
            }
        }
    }
}
