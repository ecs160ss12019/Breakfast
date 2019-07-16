package com.example.pacman;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Pair;

public class PowerPellet extends PelletCell{
    private int arcadeIndex;
    private int type = 1;
    private int pelletX;
    private int pelletY;
//    private int point = 5;

    public PowerPellet(int pelletX, int pelletY, int type, int type1, int pelletX1, int pelletY1,int arcadeindex ) {
        super(pelletX, pelletY, type,arcadeindex );
        this.type = type1;
        this.pelletX = pelletX1;
        this.pelletY = pelletY1;
        this.arcadeIndex = arcadeindex;
    }

    public int getX(){
        return this.pelletX;
    }
    public int getY(){
        return this.pelletY;
    }
    public int getType(){
        return this.type;
    }
//    public int getPoint(){
//        return this.point;
//    }

}
