package com.example.pacman;

import android.content.Context;
import android.graphics.Canvas;

import java.io.IOException;
import java.util.ArrayList;

public class ArcadeList {
    private ArrayList<Arcade> arcades;
    private Context context;
    private Canvas canvas;

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
    public void draw() {
        for (Arcade arcade : arcades) {
            if (inUse(arcade)) {
                //extract some info
                /*
                We first deal with the standard block size,
                or we should call it the bitmap size.
                */
                int numRow = arcade.getNumRow();
                int numCol = arcade.getNumCol();

                /*
                calculate proper block size
                Since the arcade is in square shape, we will assign
                the width to be the same as the height.
                The reason for choosing the height as the standard is
                because of the screen is always in landscape mode, where
                the horizontal length is longer.
                We fill the height, thus the optimal size is 1/numCol percent
                for each block.
                 */
                int blockWidth;
                int blockHeight;
                blockWidth = blockHeight = screenHeight / numRow;

                //update reference. In the future, we will update these funcs
                int xReference = updateReferenceX(numCol, blockWidth);
                int yReference = updateReferenceY(numRow, blockHeight);
                drawArcade(arcade, numRow, numCol,
                        blockWidth, blockHeight, xReference, yReference);
            }
        }
    }

    /*
    Here we draw 1 Arcade on the screen.
    It is true that we do not need to pass
    in the left and upper reference since they
    are defined as global. However, since we
    may need to draw multiple arcades in the future,
    we will pass in different referencing coordinates
    for different Arcade. We don't loss generality
    here
     */
    public void drawArcade(Arcade arcade, int numRow, int numCol,
                           int blockWidth, int blockHeight,
                           int xReference, int yReference) {
        /*
        Drawing the arcade is essentially drawing
        all the blocks one by one
         */
        /*
        The reference point is the top left hand corner coordinate
        we previously calculated.
        #----[---|---]----#
        #----[-P-|---]----#
        #----[---|---]----#
        For block P, the position in pixel is calculated by:
        X = referenceX + blockWidth * i
        Y = referenceY + blockHeight * j
        No need to handle margin since we are aligning edges
         */
        for (int i = 0; i < numRow; i++) {
            for (int j = 0; j < numCol; j++) {
                int X = xReference + blockWidth * i;
                int Y = yReference + blockHeight * j;

                //TODO
                /*
                THIS seems to be a bug,
                it should be get(i).get(j), but for some reasons
                I do not know, that is wrong.
                Strange.
                 */
                int type = blocks.get(j).get(i).getType();
                canvas.drawBitmap(blockViewList.get(type), X, Y, null);
            }
        }
    }

    public ArcadeList(Context context, Canvas canvas, int screenWidth, int screenHeight, int resourceID) {
        this.context = context;
        this.canvas = canvas;
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
    }
}
