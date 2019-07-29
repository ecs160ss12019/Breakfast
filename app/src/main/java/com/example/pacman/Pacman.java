package com.example.pacman;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

public class Pacman extends MovingObject {

    public static int totalLives = 3;

    private static Pacman pacman;
    private static boolean initialized = false;
    private boolean openMouse = false;
    private int diedViewIndex = 0;
    private List<Bitmap> normalViewList;
    private List<Bitmap> diedViewList;

    public void setInputDirection(int inputDirection) {
        if (inputDirection != -1) {
            this.motionInfo.nextDirection = inputDirection;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        Bitmap currentView;
        if(openMouse) {
            currentView = normalViewList.get(motionInfo.currDirection*2+1);
            openMouse = false;
        } else {
            currentView = normalViewList.get(motionInfo.currDirection*2);
            openMouse = true;
        }
        canvas.drawBitmap(currentView,
                motionInfo.posInScreen.x - (bitmapDimension/2),
                motionInfo.posInScreen.y - (bitmapDimension/2),
                null);
    }

    public void drawDied(Canvas canvas) {
        System.out.println("Pacman is died");
        //for (Bitmap diedView : diedViewList) {
        canvas.drawBitmap(diedViewList.get(diedViewIndex),
                    motionInfo.posInScreen.x - (bitmapDimension/2),
                    motionInfo.posInScreen.y - (bitmapDimension/2),
                    null);
        if(diedViewIndex < diedViewList.size()-1) diedViewIndex +=1;
        else {
            diedViewIndex = 0;
            this.alive();
        }
//            new android.os.Handler(Looper.getMainLooper()).postDelayed(
//                    new Runnable() {
//                        public void run() {
//                            Log.i("tag", "This'll run 300 milliseconds later");
//                        }
//                    },
//                    300);
            
//            try {
//                TimeUnit.MILLISECONDS.sleep(300);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

        //}
    }

    //Constructor
    public Pacman(final MotionInfo motionInfo, final ArrayList<Bitmap> viewList) {
        super(motionInfo, viewList);
        normalViewList = viewList.subList(0,8);
        diedViewList = viewList.subList(8,22);
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

