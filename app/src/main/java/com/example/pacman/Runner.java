package com.example.pacman;

import android.graphics.Canvas;

import java.util.ArrayList;

public class Runner implements GameObject, CollisionObserver {

    /*
    individual view height/width
    this is crucial to centering the pacman
    on the coordinates
     */
    int bitmapWidth;
    int bitmapHeight;

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
    int currDirection;
    /*
    nextDirection means the user wants to head to this direction.
    We need to verify if this direction is okay to goto.
     */
    int nextDirection;

    // block
    TwoTuple posInArcade;
    TwoTuple blockPosStart; // the block which the runner start with in one frame, set the same as posInArcade at the beginning of the frame
    TwoTuple blockPosEnd; // the block which the runner end with in one frame
    ArrayList<TwoTuple> blockRunThrough; // the blocks which the runner run through in one frame
    //coordinate
    TwoTuple position;
    TwoTuple currDirectionNextPosition;
    TwoTuple nextDirectionNextPosition;

    TwoTuple mScreen;

    float speed;

    private CollisionSubject collision;
    private boolean isDead;

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public int getBitmapWidth() {
        return bitmapWidth;
    }

    public int getBitmapHeight() {
        return bitmapHeight;
    }

    //set current position
    public void setPosition(TwoTuple position) {
        this.position = position;
    }

    @Override
    public int getPositionX() { return this.position.x; }

    @Override
    public int getPositionY() { return this.position.y; }

    public int getCurrDirection() {
        return currDirection;
    }

    public int getNextDirection() {
        return nextDirection;
    }

    public Runner(CollisionSubject collision) {
        this.collision = collision;
        collision.registerObserver(this);
    }

    //getMotionInfo
    /*
    Array of 5
    array[0] : currX
    array[1] : currY
    array[2] : currDirectionNextX
    array[3] : currDirectionNextY
    array[4] : currDirection

    Note that currDirectionNextX means next x position if continue travelling
    on currDirection (not in nextDirection), same for nextY.
     */
    public ArrayList<Integer> getMotionInfo() {
        ArrayList<Integer> motion = new ArrayList<>();
        motion.add(this.getPositionX());
        motion.add(this.getPositionY());
        motion.add(currDirectionNextPosition.x);
        motion.add(currDirectionNextPosition.y);
        motion.add(currDirection);
        motion.add(nextDirection);
        motion.add(nextDirectionNextPosition.x);
        motion.add(nextDirectionNextPosition.y);
        return motion;
    }

    /*
    We use this func to calculate the after move location in a direction,
    no matter the direction is valid or not.
     */
    TwoTuple move(int direction, long fps) {
        int nextX = this.position.x;
        int nextY = this.position.y;



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
        if (nextX + bitmapWidth / 2 > mScreen.x) {
            nextX = mScreen.x - bitmapWidth / 2;
        }
        if (nextY - bitmapHeight / 2 < 0) {
            nextY = bitmapHeight / 2;
        }
        if (nextY + bitmapHeight / 2 > mScreen.y) {
            nextY = mScreen.y - bitmapHeight / 2;
        }

        return new TwoTuple(nextX, nextY);
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public void updateStatus(long fps) {

    }

    @Override
    public void udpate(ArrayList<TwoTuple> route) {

    }
}
