package com.example.pacman;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Pair;

public class PowerPellet implements Pellet{
    private int type = 1;
    private int mScreenX;
    private int mScreenY;
    private int point = 5;

    public PowerPellet(){

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
