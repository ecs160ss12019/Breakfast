package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Observer;
import java.util.Queue;
import java.util.Random;

public class Ghost extends MovingObject {
    public int id;
    public GhostBehaviour ghostBehaviour;
    public GameObjectTimer gameObjectTimer;

    //Constructor
    public Ghost(final int id, final MotionInfo motionInfo, final ArrayList<Bitmap> viewList,
                 GhostBehaviour ghostBehaviour, long CountDownTime) {
        super(motionInfo, viewList);

        this.id = id;
        this.ghostBehaviour = ghostBehaviour;

        this.gameObjectTimer = new GameObjectTimer(CountDownTime);
    }
}
