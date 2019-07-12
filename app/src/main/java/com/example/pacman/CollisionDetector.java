package com.example.pacman;

import android.graphics.Rect;

import java.util.ArrayList;

/*
While there are 2 ways of detecting collisions.

1.
keep the distances between the reference object and obstacles.
On each update event, update the distance by maintaining the
distances. A 0 or negative distance means there is a collision.

This method is very memory expansive and hard to implement. The
Arcade inner boundaries are of irregular shape, and we may need
to keep the distance info between each block and bounding block.

If we can successfully implement this method, we can reduce time
complexity to O(1). However, I haven't yet figured it out...

2.
We find a list of potential obstacles, and check if those obstacles
have collided with the reference object. We know the dimension of the
reference object and other blocks, so we can find out a list of potential
obstacles based center distance evaluation. We then build bounding
Rectangles on the reference object and potential obstacles, and determine if
any of them intersect.
We handle collisions by examining there
bounding rectangles.

Here we implement the Method 2. Maybe in the future we will try on Method 1.
For now, it seems Method 2 is already efficient enough.
 */
public class CollisionDetector {
    //We want to pass in an object and a list of obstacles
    //if there is known to be only 1 obstacle, then make a list.
    public boolean collisionExist(Obstacle reference, ArrayList<Obstacle> obstacles) {
        /*
        if no obstacles, return false.
        It is crucial to check emptiness.
         */
        if (obstacles.isEmpty()) {
            return false;
        }

        for (Obstacle obstacle : obstacles) {
            /*
            We build bounding rectangles based on the coordinates of
            the objects' sides.
             */
            Rect rectA = new Rect((int)reference.xMin(), (int)reference.yMin(),
                    (int)reference.xMax(), (int)reference.yMax());
            Rect rectB = new Rect((int)obstacle.xMin(), (int)obstacle.yMin(),
                    (int)obstacle.xMax(), (int)obstacle.yMax());

            //check collision
            if (Rect.intersects(rectA, rectB)) return true;
        }
        return false;
    }

    //Constructor
    public CollisionDetector() {

    }
}
