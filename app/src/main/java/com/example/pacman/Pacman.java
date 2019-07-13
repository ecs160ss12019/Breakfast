package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Pair;

import java.util.ArrayList;

public class Pacman implements GameObject{
    /*
     * create Pacman here
     */
    //coordinate
    private int x;
    private int y;
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

    //currDirection means the pacman is going up, down, left, or right
    private int currDirection;

    /*
    nextDirection means the user wants to head to this direction.
    We need to verify if this direction is okay to goto.
     */
    private int nextDirection;;

    //did the pacman move?
    private boolean moved;

    //The starting point need to be initialized after construction
    //if collision, use this to roll back
    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return (int)x;
    }

    public int getY() {
        return (int)y;
    }

    public int getBitmapWidth() {
        return bitmapWidth;
    }

    public int getBitmapHeight() {
        return bitmapHeight;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(pacmanViewList.get(currDirection),
                x - (bitmapWidth/2), y - (bitmapHeight/2), null);
    }

    /*
    We use this func to calculate the after move location in a direction,
    no matter the direction is valid or not.
     */
    private Pair<Integer, Integer> move(int direction, long fps) {
        int nextX = this.x;
        int nextY = this.y;

        // Move the pacman based on the direction variable
        // and the speed of the previous frame
        if (direction == LEFT) {
            nextX = (int)(nextX - speed / fps);
        }
        if (direction == RIGHT) {
            nextX = (int)(nextX + speed / fps);
        }
        if (direction == UP) {
            nextY = (int)(nextY - speed / fps);
        }
        if (direction == DOWN) {
            nextY = (int)(nextY + speed / fps);
        }

        // Stop the Pacman going off the screen
        if (nextX - bitmapWidth / 2 < 0) {
            nextX = bitmapWidth / 2;
        }
        if (nextX + bitmapWidth / 2 > mScreenX) {
            nextX = mScreenX - bitmapWidth / 2;
        }
        if (nextY - bitmapHeight / 2 < 0) {
            nextY = bitmapHeight / 2;
        }
        if (nextY + bitmapHeight / 2 > mScreenY) {
            nextY = mScreenY - bitmapHeight / 2;
        }

        return new Pair<>(nextX, nextY);
    }

    @Override
    public void updateStatus(long fps){

    }

    public void updateStatus(long fps, Arcade arcade) {
        /*
        We cannot update is the fps is -1,
        otherwise there will be a overflow
        in speed/fps.
         */
        if (fps == -1) {
            return;
        }

        CollisionDetector collisionDetector = new CollisionDetector();

        int nextX = 0;
        int nextY = 0;

        /*
        if nextDirection is not -1, try to
        move in that direction.
        If it works, update currDirection,
        return.
         */

        /*
        We first do the unit update, then check collision.
        We do need to keep the previous location so that
        if there is a collision, we prevent it from moving and
        roll back.

        This piece of code is working but need
        to be rewrite. It is now too crowded and
        ugly.
         */
        if (nextDirection != -1) {
            Pair<Integer, Integer> next = move(nextDirection, fps);
            nextX = next.first;
            nextY = next.second;

            //Check collision
            ArrayList<Obstacle> obstacles =arcade.getObstacleList(nextX, nextY);
            Obstacle pacmanReference = new Obstacle(nextX, nextY,
                    (int)(bitmapWidth * 0.8), (int)(bitmapHeight * 0.8));

            boolean collision = collisionDetector.collisionExist(pacmanReference, obstacles);
            if(!collision) {
                //there is no collision, update and return
                set(nextX, nextY);
                currDirection = nextDirection;
                return;
            }
        }

        /*
        Either there is no new user input direction,
        or that direction does not work.
        Try moving in current direction, if it do
        not work as well, stay in current position
         */
        Pair<Integer, Integer> next = move(currDirection, fps);
        nextX = next.first;
        nextY = next.second;

        //Check collision
        ArrayList<Obstacle> obstacles = arcade.getObstacleList(nextX, nextY);
        Obstacle pacmanReference = new Obstacle(nextX, nextY,
                (int)(bitmapWidth * 0.8), (int)(bitmapHeight * 0.8));

        boolean collision = collisionDetector.collisionExist(pacmanReference, obstacles);
        if(!collision) {
            //there is no collision, update and return
            set(nextX, nextY);
        }
    }

    public void updateMovementStatus(int inputDirection, long fps,
                                     Arcade arcade) {
        /*
        We want to know where is the player heading,
        so we can update the currDirection of pacman
         */

        /*
        int roundedX = (int) x;
        int roundedY = (int) y;
        int diffX = (int)this.x - roundedX;
        int diffY = (int)this.y - roundedY;
        moved = false;


        We are not moving at all, we
        do not need to change currDirection.

        if(!(diffX == 0 && diffY == 0)) {
            moved = true;
            int absDiffX = Math.abs(diffX);
            int absDiffY = Math.abs(diffY);


            If change in X axis is greater than that in Y,
            the pacman is either heading left or right.
            Otherwise, the pacman is either heading up or down

            if (absDiffX > absDiffY) {
                //if diffX negative, moving left. Otherwise, right.
                if (diffX > 0) {
                    this.currDirection = LEFT; //left is the 0's bitmap in pacmanViewList
                } else {
                    this.currDirection = RIGHT; //right is the 1's bitmap in pacmanViewList
                }
            } else {
                //if diffY negative, moving down. Otherwise, up.
                if (diffY > 0) {
                    this.currDirection = UP; //up is the 0's bitmap in pacmanViewList
                } else {
                    this.currDirection = DOWN; //down is the 1's bitmap in pacmanViewList
                }
            }
        }
        */

        /*
        We are not using buttons thus there is
        no need to calculate the currDirection.

        Later we will align Pacman currDirection with
        button currDirection
         */

        /*
        The user wants to change direction if
        there is a touch on the NavigationButton
        and input Direction != -1.

        There is no need to update nextDirection if
        input is -1.
         */
        switch (inputDirection) {
            case -1:
                //nextDirection = -1;
                break;
            case 0:
                nextDirection = UP;
                break;
            case 1:
                nextDirection = DOWN;
                break;
            case 2:
                nextDirection = LEFT;
                break;
            case 3:
                nextDirection = RIGHT;
                break;
        }
        updateStatus(fps, arcade);
    }

    //Constructor
    public Pacman(Context context, int sx, int sy, Pair<Integer, Integer> optimalSize) {
        this.context = context;
        mScreenX = sx;
        mScreenY = sy;
        this.currDirection = RIGHT;
        this.nextDirection = -1;

        //currently, the collection is 2*2 with 4 views in total
        numRow = 2;
        numCol = 2;

        //load pacman img from resource
        Bitmap pacmanCollectionView = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman);

        /*
        resize to fit screen and
        split the collection into small bitmaps (Left, Right, Up, Down)
        */
        BitmapDivider divider = new BitmapDivider(pacmanCollectionView);
        ArrayList<Bitmap> unsizedPacmanViewList = divider.split(numRow, numCol);

        pacmanViewList = new ArrayList<>();
        for (int i = 0; i < unsizedPacmanViewList.size(); i++) {
            Bitmap bitmap = Bitmap.createScaledBitmap(unsizedPacmanViewList.get(i),
                    optimalSize.first, optimalSize.second, true);
            pacmanViewList.add(bitmap);
        }

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

