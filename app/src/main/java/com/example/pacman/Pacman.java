package com.example.pacman;

import android.graphics.Canvas;

public class Pacman implements GameObject{
    /*
    * create Pacman here
    */
    //coordinate
    private int x;
    private int y;

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public void updateStatus() {

    }

    public void updateStatus(int _x, int _y) {
        this.x = _x;
        this.y = _y;
    }

    public Pacman() {

    }
}
