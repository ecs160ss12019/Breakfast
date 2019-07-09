package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    private ArrayList<Bitmap> blockViewList;

    /*
    The img in the source folder is a collection of
    block views. We need to cut them into a list of
    3 views. We take the original collection file as
    a matrix of oriFileRow * oriFileCol
     */
    private int oriFileRow;
    private int oriFileCol;

    /*
    individual block view height/width
    this is crucial to centering the block
    on the coordinates
     */
    private int bitmapWidth;
    private int bitmapHeight;

    /*
    When we draw the Arcade, we want to make sure it is
    at the middle of the screen. We keep the coordinate
    , in pixels, of the the top left hand corner.
     */
    private int leftMostX;
    private int topMostY;

    /*
    The pacman starting coordinates.
    The coordinated is referenced to the top left block
     */
    private int pacmanStartX;
    private int pacmanStartY;

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
                int X = leftMostX + bitmapWidth * i;
                int Y = topMostY + bitmapHeight * j;

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

    @Override
    public void updateStatus(long pfs) {

    }

    public Arcade(Context context, int horizontalPix, int verticalPix) {
        this.context = context;
        this.screenWidth = horizontalPix;
        this.screenHeight = verticalPix;

        /*
        we use an csv encoding file to specify the arcade.
        in this file,   '0' means outer boundaries
                        '1' means inner boundaries
                        '2' means path
                        '3' means round corners (not implemented now)
        In fact, this file might be preprocessed somewhere
        and passed in this constructor. However, here we are
        just prototyping, thus we are placing the parsed matrix information
        right here //TODO
        */

        int[][] encodingMatrix ={
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,2,2,2,1,2,2,2,2,1,2,2,2,2,1,2,2,2,0},
                {0,2,1,2,1,2,1,1,2,1,2,1,1,2,1,2,1,2,0},
                {0,2,2,2,2,2,2,2,2,1,2,2,2,2,2,2,2,2,0},
                {0,2,1,1,1,1,2,1,1,1,1,1,2,1,1,1,1,2,0},
                {2,2,1,1,1,1,2,2,2,2,2,2,2,1,1,1,1,2,2},
                {0,2,2,2,2,2,2,1,1,1,1,1,2,2,2,2,2,2,0},
                {0,2,1,2,1,1,2,1,1,1,1,1,2,1,1,2,1,2,0},
                {0,2,1,2,1,2,2,2,2,2,2,2,2,2,1,2,1,2,0},
                {0,2,1,2,2,2,1,2,1,1,1,2,1,2,2,2,1,2,0},
                {0,2,1,1,1,2,1,2,2,2,2,2,1,2,1,1,1,2,0},
                {0,2,2,2,2,2,1,2,1,2,1,2,1,2,2,2,2,2,0},
                {2,2,1,1,1,2,1,2,1,2,1,2,1,2,1,1,1,2,2},
                {0,2,2,2,2,2,2,2,1,2,1,2,2,2,2,2,2,2,0},
                {0,2,1,1,1,2,1,1,1,2,1,1,1,2,1,1,1,2,0},
                {0,2,1,2,2,2,2,2,2,2,2,2,2,2,2,2,1,2,0},
                {0,2,1,2,1,1,1,2,1,1,1,2,1,1,1,2,1,2,0},
                {0,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        };

        //initialize block num
        this.numRow = encodingMatrix.length;
        this.numCol = encodingMatrix[0].length;

        /*
        Another spec that should be included in the arcade file
        is the starting point of the pacman and the ghosts.
        This should be specified as coordinates.
        Now, we are only specifying the decoded spacman
        starting coordinates
         */
        this.pacmanStartX = 10;
        this.pacmanStartY = 9;

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

        this.bitmapWidth = this.bitmapHeight = verticalPix / numRow;

        /*
        now we are able to calculate the top left corner coordinate.
        left most is #----[--|--]----# at the position of
        '['. The coordinate should be half ot horizontal length - half
        of the matrix width in pixels.
         */
        double matrixWidthInPixel = numCol * bitmapWidth;
        double matrixHeightInPixel = numRow * bitmapHeight;
        leftMostX = (int) (horizontalPix - matrixWidthInPixel) / 2;
        topMostY = (int) (verticalPix - matrixHeightInPixel) / 2;

        //Now we initialize the matrix of blocks, and assign property to each
        for (int i = 0; i < numRow; i++) {
            ArrayList<ArcadeBlock> newLine = new ArrayList<>(numCol);
            for (int j = 0; j < numCol; j++) {
                ArcadeBlock newBlock = new ArcadeBlock(i, j, encodingMatrix[i][j]);
                newLine.add(newBlock);
            }
            blocks.add(newLine);
        }

        /*
        Now we get the img for each types of block.
        While now we are doing it in this class, same as we did
        in Pacman, we eventually should implement a imgManager
        to help edit this imgs.
         */

        //currently, the png file is a 1*3 matrix
        oriFileRow = 1;
        oriFileCol = 3;

        //load pacman img from resource
        Bitmap blockCollectionView = BitmapFactory.decodeResource(context.getResources(), R.drawable.blocks);

        /*
        resize the original file.
        We know that the height should be the same as blockHeight.
        Since this is the source file, we need to resize based on
        the oriFile height, width ratio
         */
        double ratio = blockCollectionView.getWidth() / blockCollectionView.getHeight();
        blockCollectionView = Bitmap.createScaledBitmap(blockCollectionView, (int)(ratio * bitmapHeight),
                bitmapHeight, true);

        BitmapDivider divider = new BitmapDivider(blockCollectionView);
        blockViewList = divider.split(1,3);
    }

}
