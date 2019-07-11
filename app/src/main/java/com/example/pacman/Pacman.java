package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.ArrayList;

public class Pacman implements GameObject{
    /*
     * create Pacman here
     */
    //coordinate
    private float x;
    private float y;
    private float speed;
    private int mScreenX;
    private int mScreenY;

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

    // These variables are public and final
    // They can be directly accessed by
    // the instance (in PacmanGame)
    // because they are part of the same
    // package but cannot be changed
    final int LEFT = 0;
    final int RIGHT = 1;
    final int UP = 2;
    final int DOWN = 3;

    //direction means the pacman is going up, down, left, or right
    private int direction;

    //did the pacman move?
    private boolean moved = false;

    //The starting point need to be initialized after construction
    public void initStartingPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(pacmanViewList.get(direction), x - (bitmapWidth/2), y - (bitmapHeight/2), null);
    }

    @Override
    public void updateStatus(long fps) {
        if (moved) {
            // Move the pacman based on the direction variable
            // and the speed of the previous frame
            if (direction == LEFT) {
                x = x - speed / fps;
            }
            if (direction == RIGHT) {
                x = x + speed / fps;
            }
            if (direction == UP) {
                y = y - speed / fps;
            }
            if (direction == DOWN) {
                y = y + speed / fps;
            }

            // Stop the Pacman going off the screen
            if (x - bitmapWidth / 2 < 0) {
                x = bitmapWidth / 2;
            }
            if (x + bitmapWidth / 2 > mScreenX) {
                x = mScreenX - bitmapWidth / 2;
            }
            if (y - bitmapHeight / 2 < 0) {
                y = bitmapHeight / 2;
            }
            if (y + bitmapHeight / 2 > mScreenY) {
                y = mScreenY - bitmapHeight / 2;
            }
        }
    }

    public void updateMovementStatus(float x, float y, long fps) {
        /*
        We want to know where is the player heading,
        so we can update the direction of pacman
         */
        int roundedX = (int) x;
        int roundedY = (int) y;
        int diffX = (int)this.x - roundedX;
        int diffY = (int)this.y - roundedY;
        moved = false;

        /*
        We are not moving at all, we
        do not need to change direction.
         */
        if(!(diffX == 0 && diffY == 0)) {
            moved = true;
            int absDiffX = Math.abs(diffX);
            int absDiffY = Math.abs(diffY);

            /*
            If change in X axis is greater than that in Y,
            the pacman is either heading left or right.
            Otherwise, the pacman is either heading up or down
             */
            if (absDiffX > absDiffY) {
                //if diffX negative, moving left. Otherwise, right.
                if (diffX > 0) {
                    this.direction = LEFT; //left is the 0's bitmap in pacmanViewList
                } else {
                    this.direction = RIGHT; //right is the 1's bitmap in pacmanViewList
                }
            } else {
                //if diffY negative, moving down. Otherwise, up.
                if (diffY > 0) {
                    this.direction = UP; //up is the 0's bitmap in pacmanViewList
                } else {
                    this.direction = DOWN; //down is the 1's bitmap in pacmanViewList
                }
            }
        }

        updateStatus(fps);
    }

    //Constructor
    public Pacman(Context context, int sx, int sy) {
        this.context = context;
        mScreenX = sx;
        mScreenY = sy;
        this.direction = RIGHT;

        //currently, the collection is 2*2 with 4 views in total
        numRow = 2;
        numCol = 2;

        //load pacman img from resource
        Bitmap pacmanCollectionView = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman);

        //resize to fit screen
        int originalWidth = pacmanCollectionView.getWidth();
        int originalHeight = pacmanCollectionView.getHeight();
        double resizingFactor = 0.05;
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

        // Configure the speed of the Pacman
        // This code means the Pacman can cover the width of the screen in 4 second
        speed = mScreenX/4;
    }
}

