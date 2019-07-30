package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Observer;
import java.util.Random;

/*
* This is equivalent to the original cherry in Pac-Man game:
* random generate the cake onto the sceen and double the points earned;
* power up the 4 ghosts;
* the effect should disappear in few seconds;
* the cake should move around.
* */
public class Cake extends MovingObject implements GameObject {
    private boolean isDead;
    //eaten means whether points was added.
    private boolean eaten;
//    private GameObjectTimer timer;

//    public boolean isDead() {
//        return isDead;
//    }
//
//    public void setDead(boolean dead) {
//        isDead = dead;
//    }

//    public void updateLocation(long fps, Arcade arcade) {
//        /*
//        We cannot update is the fps is -1,
//        otherwise there will be a overflow
//        in speed/fps.
//         */
//        if (fps == -1 || fps == 0) {
//            return;
//        }
//
//
//        /***************************************/
//        //New Method
//        int mathematicalMove = mathematicalMoveDistance(fps);
//
//        if (nextDirection != currDirection && nextDirection != -1) {
//            //try new direction
//            boolean allowsTurn = arcadeAnalyzer.allowsToGo(posInArcade, nextDirection);
//
//            if (allowsTurn) {
//                //Turn and go
//                posInArcade = movedTo(mathematicalMove, nextDirection);
//
//                currDirection = nextDirection;
//                needToChangeDir = false;
//                return;
//            }
//        }
//
//        //Either not able to turn or not desired to turn
//        boolean allowsMove = arcadeAnalyzer.allowsToGo(posInArcade, currDirection);
//        if (allowsMove) {
//            //move and go
//            posInArcade = movedTo(mathematicalMove, currDirection);
//            return;
//        }
//
//        needToChangeDir = true;
//
//        //else no move, stay there
//    }
//
//    //mathematical movement distance
//    private int mathematicalMoveDistance(long fps) {
//        return (int)(speed / fps);
//    }
//
//    //move as far as possible
//    private TwoTuple movedTo(int mathematicalMove, int movingDirection) {
//        int movedDistance = 0;
//        TwoTuple currPos = posInArcade;
//
////        System.out.println("Starting to move from: " + posInArcade.first() + " " + posInArcade.second());
//        boolean allowsMove = arcadeAnalyzer.allowsToGo(currPos, movingDirection);
//        while (movedDistance <= mathematicalMove && allowsMove) {
//            movedDistance += arcadeAnalyzer.blockDimension;
//            currPos = TwoTuple.moveTo(currPos, movingDirection);
//
////            System.out.println("moved distance: " + movedDistance);
////            System.out.println("Moved to: " + currPos.first() + " " + currPos.second());
//        }
//
////        System.out.println("!!!!!");
////        System.out.println("Finished moving to: " + currPos.first() + " " + currPos.second());
//        return currPos;
//    }
//    public void eat(){
//        eaten = true;
//    }
//    public boolean checkalive(){
//        return eaten;
//    }

    public void setNextDirection() {
        Random random = new Random();
        this.motionInfo.nextDirection = random.nextInt(4);
    }

    //Constructor
    public Cake(final MotionInfo motionInfo, final ArrayList<Bitmap> viewList){
        super(motionInfo, viewList);

    }
}
