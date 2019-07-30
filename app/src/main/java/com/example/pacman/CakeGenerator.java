package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;


public class CakeGenerator {
    //INIT Cake
    final static int LEFT = 0;
    final static int RIGHT = 1;
    final static int UP = 2;
    final static int DOWN = 3;
    MovingObject cake;
    GameObjectTimer timer;

    public  CakeGenerator(final Arcade arcade, Context context,
                         final TwoTuple mScreen, final GameMode gameMode){
        TwoTuple cakeInitPos = new TwoTuple(arcade.cakePosition);
        final BitmapDivider bitmapDivider = new BitmapDivider(context);
        MotionInfo cakeInitMotion = new MotionInfo(
                cakeInitPos,
                arcade.mapScreen(cakeInitPos),
                0, UP, UP, gameMode.getPacmanSpeed());
        ArrayList<Bitmap> cakeViewList = BitmapDivider.splitAndResize(
                bitmapDivider.loadBitmap(R.drawable.cake),
                new TwoTuple(1,1),
                new TwoTuple(mScreen.y / 13, mScreen.y / 13));
        ArrayList<Bitmap> cakeViews = new ArrayList<>();
        cakeViews.add(cakeViewList.get(0));
        cakeViews.add(cakeViewList.get(0));
        cakeViews.add(cakeViewList.get(0));
        cakeViews.add(cakeViewList.get(0));
        this.cake = new Cake(cakeInitMotion, cakeViews);
    }
    public MovingObject getCake(){
        return cake;
    }

}
