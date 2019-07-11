package com.example.pacman;

public class Obstacle {
    int x_pix;
    int y_pix;

    int boundingWidth;
    int boundingHeight;

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
