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
    public GhostBehaviour ghostBehaviour;

    //Constructor
    public Ghost(final MotionInfo motionInfo, final ArrayList<Bitmap> viewList, GhostBehaviour ghostBehaviour) {
        super(motionInfo, viewList);

        this.ghostBehaviour = ghostBehaviour;
    }
}
