package com.example.pacman;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.ArrayList;

public class Pacman implements GameObject{
    /*
    * create Pacman here
    */
    //coordinate
    private int x;
    private int y;

    //context of the game, used access Resource ptr
    private Context context;

    /*
    img for the pacman
    Note that this is the combination of pacman
    img in all directions
     */
    private ArrayList<Bitmap> pacmanViewList;

    /*
    The img in the source folder is a collection of
    pacman views. Up, Right, Left, Down. We need to
    cut them into a list of 4 views. We take the
    original collection file as a matrix of
    numRow * numCol
     */

    private int numRow;
    private int numCol;

    /*
    individual pacman view height/width
    this is crucial to centering the pacman
    on the coordinates
     */
    private int bitmapWidth;
    private int bitmapHeight;

    //direction means the pacman is going up, down, left, or right
    private int direction;

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(pacmanViewList.get(direction), (int)(x - (bitmapWidth/2)), (int)(y - (bitmapHeight/2)), null);
    }

    @Override
    public void updateStatus() {

    }

    public void updateStatus(int x, int y) {
        /*
        We want to know where is the player heading,
        so we can update the direction of pacman
         */
        int diffX = this.x - x;
        int diffY = this.y - y;
        int absDiffX = Math.abs(diffX);
        int absDiffY = Math.abs(diffY);

        /*
        If change in X axis is greater than that in Y,
        the pacman is either heading left or right.
        Otherwise, the pacman is either heading up or down
         */
        if(absDiffX > absDiffY) {
            //if diffX negative, moving left. Otherwise, right.
            if(diffX > 0) {
                this.direction = 0; //left is the 0's bitmap in pacmanViewList
            } else {
                this.direction = 1; //right is the 1's bitmap in pacmanViewList
            }
        } else {
            //if diffY negative, moving down. Otherwise, up.
            if(diffY > 0) {
                this.direction = 2; //up is the 0's bitmap in pacmanViewList
            } else {
                this.direction = 3; //down is the 1's bitmap in pacmanViewList
            }
        }

        this.x = x;
        this.y = y;
    }
/*
    public Bitmap getCurrentView(){
        return new Bitmap();
    }
*/

    public Pacman(Context context) {
        this.context = context;

        //currently, the collection is 2*2 with 4 views in total
        numRow = 2;
        numCol = 2;

        //load pacman img from resource
        Bitmap pacmanCollectionView = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman);

        //resize to fit screen
        int originalWidth = pacmanCollectionView.getWidth();
        int originalHeight = pacmanCollectionView.getHeight();
        double resizingFactor = 0.08;
        pacmanCollectionView = Bitmap.createScaledBitmap(pacmanCollectionView, (int)(originalWidth * resizingFactor)
                , (int)(originalHeight * resizingFactor), true);

        //split the collection into small bitmaps (Left, Right, Up, Down)
        BitmapDivider divider = new BitmapDivider(pacmanCollectionView);
        pacmanViewList = divider.split(numRow, numCol);

        /*
        initialize the size of bitmap after split
         */
        bitmapWidth = pacmanViewList.get(0).getWidth();
        bitmapHeight = pacmanViewList.get(0).getHeight();
    }
}
