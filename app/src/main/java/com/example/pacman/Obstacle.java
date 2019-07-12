package com.example.pacman;

/*
Every GameObject can be considered as an Obstacle.
This class will assist collision detection.
The reason we want this class is to calculate the
dimension of the bounding box of a moving or stationary
GameObject.
 */
public class Obstacle {
    int x_pix;
    int y_pix;

    int boundingWidth;
    int boundingHeight;

    /*
    We calculate the left, right, up, bottom
    boundaries of the bounding rectangle.
     */
    public double xMax() {
        return x_pix + boundingWidth / 2;
    }

    public double xMin() {
        return x_pix - boundingWidth / 2;
    }

    public double yMax() {
        return y_pix + boundingHeight / 2;
    }

    public double yMin() {
        return y_pix - boundingHeight / 2;
    }

    //Constructor
    public Obstacle(int x_pix, int y_pix, int boundingWidth, int boundingHeight) {
        this.x_pix = x_pix;
        this.y_pix = y_pix;
        this.boundingWidth = boundingWidth;
        this.boundingHeight = boundingHeight;
    }
}
