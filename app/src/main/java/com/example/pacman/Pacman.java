package com.example.pacman;

import android.graphics.Bitmap;
import java.util.ArrayList;

public class Pacman extends MovingObject {

    public static int totalLives = 3;

    private static Pacman pacman;
    private static boolean initialized = false;

    public void setInputDirection(int inputDirection) {
        if (inputDirection != -1) {
            this.motionInfo.nextDirection = inputDirection;
        }
    }

    private void setBitmaps() {

    }

    //Constructor
    public Pacman(final MotionInfo motionInfo, final ArrayList<Bitmap> viewList) {
        super(motionInfo, viewList);
    }

    // Singleton, because we only have one Pacman in entire game, also we can retrieve Pacman from any GameObjectCollection
    public static synchronized Pacman getInstance(final MotionInfo motionInfo, final ArrayList<Bitmap> viewList) {
        if(initialized) return pacman;
        //        TwoTuple pacmanInitPos = new TwoTuple(arcade.pacmanPosition);
        //        ArrayList<Bitmap> pacmanViewList = BitmapDivider.splitAndResize(
        //                bitmapDivider.loadBitmap(R.drawable.pacman),
        //                new TwoTuple(2,2),
        //                new TwoTuple(mScreen.y / 15, mScreen.y / 15));
        //        MotionInfo pacmanInitMotion = new MotionInfo(
        //                pacmanInitPos,
        //                arcade.mapScreen(pacmanInitPos),
        //                0, RIGHT, -1, gameMode.getPacmanSpeed());
        pacman = new Pacman(motionInfo, viewList);
        initialized = true;
        return pacman;
    }

    public static synchronized Pacman getInstance() {
        if(initialized) return pacman;
        return null;
    }
}

