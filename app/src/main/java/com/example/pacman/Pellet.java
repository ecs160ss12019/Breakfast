package com.example.pacman;


import android.graphics.Canvas;

public interface Pellet {
    public void draw(Canvas canvas);
    public void updateStatus(long fps);
}
