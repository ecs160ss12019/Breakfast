package com.example.pacman;

import android.graphics.Canvas;

/*
This is the interface that every game object
must implement
 */
public interface GameObject {
    //call override to draw method and draw itself on the screen
    public void draw(Canvas canvas);

    //update coordinate and status
    public void updateStatus(long fps);
}
