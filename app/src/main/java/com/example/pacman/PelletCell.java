package com.example.pacman;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

public class PelletCell implements Pellet{

    private int type = 0;
    private int mScreenX;
    private int mScreenY;
    private int point = 1;

    public PelletCell(){

    }
    public int getX(){
        return mScreenX;

    }
    public int getY(){
        return mScreenY;

    }
    public int getType(){
        return this.type;
    }
    public int getPoint(){
        return point;
    }

}
