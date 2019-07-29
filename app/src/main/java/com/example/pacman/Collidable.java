package com.example.pacman;

import android.graphics.Rect;

public interface Collidable {
    public boolean checkCollisionWith(Rect pacmanBoundingRect);
}
