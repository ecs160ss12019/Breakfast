package com.example.pacman;

import android.graphics.Rect;

import java.util.ArrayList;

/*
We handle collisions by examining there
bounding rectangles.
 */
public class CollisionDetector {


    //We want to pass in an object and a list of obstacles
    //if there is known to be only 1 obstacles, make a list
    public boolean collisionExist(Obstacle reference, ArrayList<Obstacle> obstacles) {
        //if no obstacles, return false
        if (obstacles.isEmpty()) {
            return false;
        }

        for (Obstacle obstacle : obstacles) {
            Rect rectA = new Rect((int)reference.xMin(), (int)reference.yMin(),
                    (int)reference.xMax(), (int)reference.yMax());
            Rect rectB = new Rect((int)obstacle.xMin(), (int)obstacle.yMin(),
                    (int)obstacle.xMax(), (int)obstacle.yMax());
            if (Rect.intersects(rectA, rectB)) return true;
        }
        return false;
    }

    //Constructor
    public CollisionDetector() {

    }
}
