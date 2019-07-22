package com.example.pacman;

import android.graphics.Canvas;

/*
This is the interface that every game object
must implement
 */
public interface GameObject {
    //call override to draw method and draw itself on the screen
    public void draw(Canvas canvas);

    //get current position
    public int getPositionX();

    public int getPositionY();
}
