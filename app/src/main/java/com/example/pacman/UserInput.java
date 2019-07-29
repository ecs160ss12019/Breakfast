package com.example.pacman;

/*
Since onTouchEvent is, by Android
default, going to run in the main
thread, we do not want to do too many
things in the onTouchEvent func, otherwise,
the user will experience a delay.

We take the UserInput and process all other
stuff based on this object.
 */

public class UserInput {
    private float x;
    private float y;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void updateUserInput(float x, float y) {
        this.x = x;
        this.y = y;
    }

    //Constructor
    public UserInput() {
        this.x = 0;
        this.y = 0;
    }
}
