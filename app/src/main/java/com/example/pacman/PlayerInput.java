package com.example.pacman;

import android.view.MotionEvent;

/*
This class handles the player input
whenever there is a touch event, it should
be handled here. Eventually this will return
the type of input to PacmanGame
 */
public class PlayerInput {
    //this are the raw input in pixels
    private double inputX;
    private double inputY;

    public double getInputX() {
        return inputX;
    }

    public double getInputY() {
        return inputY;
    }

    public int inputType(MotionEvent motionEvent) {
        //TODO
        return 0;
    }

    public PlayerInput() {

    }
}
