package com.example.pacman;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

public class PelletCell{

    private int type;
    private int pelletX;
    private int pelletY;
//    private int point;

    public PelletCell(int pelletX, int pelletY, int type) {
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
//    public int getPoint(){
//        return point;
//    }
}
