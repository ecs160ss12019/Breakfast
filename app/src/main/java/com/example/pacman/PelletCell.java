package com.example.pacman;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
/*
    Type 0 - unavaliable slots for pellets
    Type 1 - avaliable slots for pellets
    Type 2 - avaliable slots for power pellet
 */
public class PelletCell{
    private int arcadeIndex;
    private int type;
    private int pelletX;
    private int pelletY;
//    private int point;

    public PelletCell(int pelletX, int pelletY, int type, int arcadeIndex) {
        this.arcadeIndex = arcadeIndex;
        this.type = type;
        this.pelletX = pelletX;
        this.pelletY = pelletY;
//        this.point = point;
    }

    public int getX(){
        return pelletX;
    }
    public int getY(){
        return pelletY;
    }
    public int getType(){
        return this.type;
    }
    public int getArcadeIndex() {
        return arcadeIndex;
    }

}
