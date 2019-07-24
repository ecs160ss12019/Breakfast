package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Observer;
import java.util.Queue;
import java.util.Random;

public class Ghost extends MovingObject {
    private DirectionQueue directionQueue;

    public void setNextDirection() {
        Random random = new Random();
        this.motionInfo.nextDirection = random.nextInt(4);
    }

    //Constructor
    public Ghost(final MotionInfo motionInfo, final ArrayList<Bitmap> viewList) {
        super(motionInfo, viewList);

        this.directionQueue = new DirectionQueue();
        directionQueue.enqueue(this.motionInfo.nextDirection);
    }
}
