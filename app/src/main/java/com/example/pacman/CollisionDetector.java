package com.example.pacman;

import android.graphics.Rect;

/*
We handle collisions by examining there
bounding rectangles.
 */
public class CollisionDetector {
    public boolean collisionAB(Rect A, Rect B) {
        return Rect.intersects(A, B);
    }

    //Constructor
    public CollisionDetector() {

    }
}
