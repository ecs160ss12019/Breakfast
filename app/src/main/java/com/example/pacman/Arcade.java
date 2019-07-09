package com.example.pacman;

import android.content.Context;
import android.graphics.Canvas;

import java.util.ArrayList;

/*
this class is the arcade that the pacman
and other game characters walk on.
There is only 1 arcade in each game.
 */
public class Arcade implements GameObject{
    private Context context;

    //the matrix of building blocks
    private ArrayList<ArrayList<ArcadeBlock>> blocks = new ArrayList<>();

    //screen res
    private int screenWidth;
    private int screenHeight;

    //block num
    private int numRow;
    private int numCol;

    //block size
    private int blockWidth;
    private int blockHeight;

    @Override
    public void draw(Canvas canvas) {
        /*
        Drawing the arcade is essentially drawing
        all the blocks one by one
         */

        /*
        While we can either draw the blocks from this class
        or the ArcadeBlock class. There are pros and cons.
        pros: Drawing the blocks from here can save system resources
              we do not need to pass the canvas, the bitmap objects all
              around.
        cons: Drawing the blocks from here can make code ugly.

        For now, we can stick to this class. //TODO
         */

        
    }

    @Override
    public void updateStatus() {

    }

    public Arcade(Context context, int horizontalPix, int verticalPix) {
        this.context = context;
        this.screenWidth = horizontalPix;
        this.screenHeight = verticalPix;

        /*
        we use an csv encoding file to specify the arcade.
        in this file,   '0' means path
                        '1' means outer boundaries
                        '2' means inner boundaries
                        '3' means round corners (not implemented now)
        In fact, this file might be preprocessed somewhere
        and passed in this constructor. However, here we are
        just prototyping, thus we are placing the parsed matrix information
        right here //TODO
        */

        int[][] encodingMatrix ={
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,2,0,0,0,0,2,0,0,0,0,2,0,0,0,1},
            {1,0,2,0,2,0,2,2,0,2,0,2,2,0,2,0,2,0,1},
            {1,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,1},
            {1,0,2,2,2,2,0,2,2,2,2,2,0,2,2,2,2,0,1},
            {0,0,2,2,2,2,0,0,0,0,0,0,0,2,2,2,2,0,0},
            {1,0,0,0,0,0,0,2,2,2,2,2,0,0,0,0,0,0,1},
            {1,0,2,0,2,2,0,2,2,2,2,2,0,2,2,0,2,0,1},
            {1,0,2,0,2,0,0,0,0,0,0,0,0,0,2,0,2,0,1},
            {1,0,2,0,0,0,2,0,2,2,2,0,2,0,0,0,2,0,1},
            {1,0,2,2,2,0,2,0,0,0,0,0,2,0,2,2,2,0,1},
            {1,0,0,0,0,0,2,0,2,0,2,0,2,0,0,0,0,0,1},
            {0,0,2,2,2,0,2,0,2,0,2,0,2,0,2,2,2,0,0},
            {1,0,0,0,0,0,0,0,2,0,2,0,0,0,0,0,0,0,1},
            {1,0,2,2,2,0,2,2,2,0,2,2,2,0,2,2,2,0,1},
            {1,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,1},
            {1,0,2,0,2,2,2,0,2,2,2,0,2,2,2,0,2,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        };

        //initialize block num
        this.numRow = encodingMatrix.length;
        this.numCol = encodingMatrix[0].length;

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

        this.blockWidth = this.blockHeight = horizontalPix / numRow;

        //Now we initialize the matrix of blocks, and assign property to each
        for (int i = 0; i < numRow; i++) {
            blocks.add(new ArrayList<ArcadeBlock>());
            for (int j = 0; j < numCol; j++) {
                ArcadeBlock newBlock = new ArcadeBlock(i, j, encodingMatrix[i][j]);
                blocks.get(i).add(newBlock);
            }
        }

    }

}
