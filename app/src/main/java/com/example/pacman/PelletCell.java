package com.example.pacman;

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

    private boolean isDead;
    public boolean isDead() { return isDead; }
    public void setDead(boolean dead) { isDead = dead; }

    private CollisionSubject collision;

    // Constructor for pelletcell with given locations and type, also the arcadeIndex.
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
    public TwoTuple getPositionInArcade() { return new TwoTuple(pelletX, pelletY); }
    public int getType(){ return this.type; }
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
    public int getPositionX(){return this.getX();}

    public int getPositionY(){return this.getY();}

    //set position
    public void setCenter(int centerX, int centerY){}
    //getCurrDirection
    public int getCurrDirection(){return 0;}
    //getNextDirection
    public int getNextDirection(){return 0;}

    public ArrayList<Integer> getMotionInfo(){return new ArrayList<Integer>();}
}
