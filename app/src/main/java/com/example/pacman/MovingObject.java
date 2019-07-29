package com.example.pacman;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import java.util.ArrayList;

public class MovingObject implements GameObject{
    final static int LEFT = 0;
    final static int RIGHT = 1;
    final static int UP = 2;
    final static int DOWN = 3;

    //Motion info contains all essential
    //parameters that describes current motion
    protected MotionInfo motionInfo;
    protected MotionInfo motionInfoPrev;
    private boolean appear;

    //Bitmap
    protected ArrayList<Bitmap> viewList;
    protected int bitmapDimension;
    //protected int currentBitmapIndex;

    public void eat(){
        appear = false;
    }
    public boolean checkalive(){
        return appear;
    }
    public void alive(){
        appear = true;
    }

    public MotionInfo getMotionInfo() {
        return motionInfo;
    }

    public void setMotionInfo(MotionInfo motionInfo) {
        this.motionInfoPrev = this.motionInfo;
        this.motionInfo = motionInfo;
    }


    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(viewList.get(motionInfo.currDirection),
                motionInfo.posInScreen.x - (bitmapDimension/2),
                motionInfo.posInScreen.y - (bitmapDimension/2),
                null);
    }

    @Override
    public Rect getBoundingRect(Info info) {
        //Since most movingObject views are circle, the
        //(radius/(sideLength / 2)) = 0.5/(sqrt(2) / 2) = 0.7
        int margin = (int)(bitmapDimension / 2 * 0.7);
        int left = info.posInScreen_X() - margin;
        int top = info.posInScreen_Y() - margin;
        int right = info.posInScreen_X() + margin;
        int bottom = info.posInScreen_Y() + margin;
        return new Rect(left, top, right, bottom);
    }

    public Rect getPathRect() {
        Rect prevRect = getBoundingRect(motionInfoPrev);
        Rect currRect = getBoundingRect(motionInfo);

        Rect rect = new Rect();
        switch (motionInfoPrev.currDirection) {
            case LEFT:
                rect = new Rect(currRect.left, currRect.top, prevRect.right, currRect.bottom);
                break;
            case -1:
            case RIGHT:
                rect = new Rect(prevRect.left, currRect.top, currRect.right, currRect.bottom);
                break;
            case UP:
                rect =  new Rect(currRect.left, currRect.top, currRect.right, prevRect.bottom);
                break;
            case DOWN:
                rect = new Rect(currRect.left, prevRect.top, currRect.right, currRect.bottom);
                break;
        }
        return rect;
    }

    @Override
    public boolean collision(Rect pacmanPathRect) {
        return Rect.intersects(pacmanPathRect, getPathRect());
    }


    //Constructor
    public MovingObject(final MotionInfo motionInfo, ArrayList<Bitmap> viewList) {
        this.motionInfo = motionInfo;
        this.motionInfoPrev = motionInfo;
//        this.addObserver(motionObserver);
        this.viewList = viewList;
        this.bitmapDimension = viewList.get(0).getHeight();
    }
}
