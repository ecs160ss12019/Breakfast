package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;

/*
This is the interface that every game object
must implement
 */
public interface GameObject {
    //call override to draw method and draw itself on the screen
    void draw(Canvas canvas);

    //to check collision, provide the bounding rect
    Rect getBoundingRect(Info info);

    //check collision
    boolean collision(Rect pacmanPathRect);
}
