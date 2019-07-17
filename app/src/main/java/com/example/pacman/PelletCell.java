package com.example.pacman;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
/*
    Type 0 - slots but no pellets
    Type 1 - avaliable slots for power pellets
    Type 2 - avaliable slots for pellet
 */
public class PelletCell implements GameObject{
    private int arcadeIndex;
    private int type;
    private int pelletX;
    private int pelletY;

    //Constructor for pelletcell with given locations and type, also the arcadeIndex.
    public PelletCell(int pelletX, int pelletY, int type, int arcadeIndex) {
        this.arcadeIndex = arcadeIndex;
        this.type = type;
        this.pelletX = pelletX;
        this.pelletY = pelletY;
//        this.point = point;
    }
    //Overwritting the interface functions.
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
    //call override to draw method and draw itself on the screen
    //This method was implemented in PelletList class
    public void draw(Canvas canvas){
//        canvas.drawBitmap(pacmanViewList.get(currDirection),
//                x - (bitmapWidth/2), y - (bitmapHeight/2), null);
    }

    //update coordinate and status
    //Eaten pellet
    public void updateStatus(long fps){}
    //get position for pellets(not screen position)
    public int getCenterX(){return this.getX();}

    public int getCenterY(){return this.getY();}

    //set center position
    public void setCenter(int centerX, int centerY){}
    //getCurrDirection
    public int getCurrDirection(){return 0;}
    //getNextDirection
    public int getNextDirection(){return 0;}

    public ArrayList<Integer> getMotionInfo(){return new ArrayList<Integer>();}
}
