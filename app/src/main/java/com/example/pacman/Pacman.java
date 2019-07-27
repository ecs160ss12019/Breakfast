package com.example.pacman;

import android.graphics.Bitmap;
import java.util.ArrayList;

public class Pacman extends MovingObject {

    public void setInputDirection(int inputDirection) {
        if (inputDirection != -1) {
            this.motionInfo.nextDirection = inputDirection;
        }
    }

    //Constructor
    public Pacman(final MotionInfo motionInfo, final ArrayList<Bitmap> viewList) {
        super(motionInfo, viewList);
    }
}

