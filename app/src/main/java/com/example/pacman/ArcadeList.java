package com.example.pacman;

import android.content.Context;
import android.graphics.Canvas;

import java.io.IOException;
import java.util.ArrayList;

public class ArcadeList {
    private ArrayList<Arcade> arcades;
    private Context context;

    //screen res
    private int screenWidth;
    private int screenHeight;

    /*
    update reference points for Arcades.
    For now we will simply calculate the reference
    point for the one and only center arcade on the
    screen
     */
    /*
    now we are able to calculate the top left corner coordinate.
    left most is #----[--|--]----# at the position of
    '['. The coordinate should be half ot horizontal length - half
    of the matrix width in pixels.
     */
    public int updateReferenceX(int numCol, int blockWidth) {
        double matrixWidthInPixel = numCol * blockWidth;
        return  (int) (screenWidth - matrixWidthInPixel) / 2;
    }

    public int updateReferenceY(int numRow, int blockHeight) {
        double matrixHeightInPixel = numRow * blockHeight;
        return (int) (screenHeight - matrixHeightInPixel) / 2;
    }


    /*
    Traverse through all Arcade,
    draw the one Arcade that is
    in use.
    */
    /*
    Now we only have 1 Arcade active
    at a time. In the future, however,
    we might have multiple Arcades in use
    at the same time. That is why we are
    traversing through the list instead of
    drawing it 1 by 1.
     */
    public void draw(Canvas canvas) {
        for (Arcade arcade : arcades) {
            if (arcade.inUse) {
                arcade.draw(canvas);
            }
        }
    }

    public ArcadeList(Context context, int screenWidth, int screenHeight, int resourceID) {
        this.context = context;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        /*
        Now we extract Arcades from the JSON file,
        here we use the ArcadeDecoder. I know that
        having resourceID as a parameter in constructor
        is not optimal. However, needing to initiate an
        ArcadeDecoder in the Pacman class is even more
        ugly.
         */
        ArcadeDecoder decoder = new ArcadeDecoder(context, resourceID);

        //decode() require a try-catch block
        try {
            arcades = decoder.decode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //deploy parameters after init
        for (Arcade arcade : arcades) {
            arcade.deployParameter(screenWidth, screenHeight, "");
        }
    }
}
