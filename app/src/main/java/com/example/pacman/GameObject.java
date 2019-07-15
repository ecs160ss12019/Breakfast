package com.example.pacman;

import android.graphics.Canvas;

import java.util.ArrayList;

/*
This is the interface that every game object
must implement
 */
public interface GameObject {
    //call override to draw method and draw itself on the screen
    public void draw(Canvas canvas);

    //update coordinate and status
    public void updateStatus(long fps);

    //get center position
    public int getCenterX();

    public int getCenterY();

    //set center position
    public void setCenter(int centerX, int centerY);

    /*
    These variables are public and final
    They can be directly accessed by
    the instance (in PacmanGame)
    because they are part of the same
    package but cannot be changed

    This is crucial to all moving objects
    final int LEFT = 0;
    final int RIGHT = 1;
    final int UP = 2;
    final int DOWN = 3;
     */

    //getCurrDirection
    public int getCurrDirection();

    //getNextDirection
    public int getNextDirection();

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
    public ArrayList<Integer> getMotionInfo();
}
