package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import java.util.ArrayList;

public class StationaryObject implements GameObject{
    //Motion info contains all essential
    //parameters that describes current motion
    protected StaticInfo staticInfo;
    private boolean appear;

    //Bitmap
    private ArrayList<Bitmap> viewList;
    private int bitmapDimension;
    protected int currentBitmapIndex = 0;


    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(viewList.get(currentBitmapIndex),
                staticInfo.posInScreen.x - (bitmapDimension/2),
                staticInfo.posInScreen.y - (bitmapDimension/2),
                null);
    }

    @Override
    public Rect getBoundingRect(Info info) {
        //Since most stationaryObject views are circle, the
        //(radius/(sideLength / 2)) = 0.5/(sqrt(2) / 2) = 0.7
        int margin = (int)(bitmapDimension / 2 * 0.7);
        int left = info.posInScreen_X() - margin;
        int top = info.posInScreen_Y() - margin;
        int right = info.posInScreen_X() + margin;
        int bottom = info.posInScreen_Y() + margin;
        return new Rect(left, top, right, bottom);
    }

    @Override
    public boolean collision(Rect pacmanPathRect) {
        return Rect.intersects(pacmanPathRect, getBoundingRect(staticInfo));
    }

    //Constructor
    public StationaryObject(final StaticInfo staticInfo, ArrayList<Bitmap> viewList) {
        this.staticInfo = staticInfo;
//        this.addObserver(motionObserver);
        this.viewList = viewList;
        this.bitmapDimension = viewList.get(0).getHeight();
    }
}
