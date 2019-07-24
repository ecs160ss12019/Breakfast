package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Random;

public class Ghost extends Runner implements GameObject, CollisionObserver {

    boolean alive;

    //coordinate
    private String GhostName;

    //context of the game, used access Resource ptr
    //context of the game, used access Resource ptr
    private Context context;

    private Bitmap ghostView;

    private int bitmapWidth;
    private int bitmapHeight;
    private boolean needToChangeDir = false;

    private Pacman pacman; // used for chase and kill Pacman

    // ** for ghostbehavior;
    //public Ghost(Context context, int sx, int sy, Arcade arcade,
    //             Pacman pacman, Bitmap ghostView,  int direction,
    //             float speed, String name)

    private Ghost(Context context, TwoTuple screen, Bitmap ghostView, float speed, String name, Pacman pacman, CollisionSubject collision) {
        super(screen, speed, collision);

        this.currDirection = UP;
        this.nextDirection = -1;

        this.context = context;
        this.ghostView = ghostView;
        bitmapWidth = ghostView.getWidth();
        bitmapHeight = ghostView.getHeight();
        this.GhostName = name;
        this.pacman = pacman;
    }

    // Constructor1:
    public Ghost(Context context, TwoTuple screen, Bitmap ghostView, float speed, String name,
                 Pacman pacman, CollisionSubject collision, Arcade arcade) {
        this(context, screen, ghostView, speed, name, pacman, collision);

        this.arcade = arcade;
        this.posInScreen = new TwoTuple(arcade.getGhostPosition_pix());
        this.posInArcade = new TwoTuple(arcade.ghostPosition);
        this.posInArcadeInit = posInArcade;

        motionInArcade = new MotionInArcade(arcade);

    }

    //Constructor2
    public Ghost(Context context, TwoTuple screen, Bitmap ghostView, float speed, String name,
                 Pacman pacman, CollisionSubject collision, Arcade arcade, ArcadeAnalyzer arcadeAnalyzer) {
        this(context, screen, ghostView, speed, name, pacman, collision);

        this.arcade = arcade;
        this.posInArcade = new TwoTuple(arcade.ghostPosition);
        this.posInScreen = arcade.mapScreen(posInArcade);
        this.posInArcadeInit = posInArcade;

        this.arcadeAnalyzer = arcadeAnalyzer;
        this.blockDimension = arcadeAnalyzer.blockDimension;
    }

    @Override
    public void draw(Canvas canvas) {
        // TwoTuple screenPos = arcade.mapScreen(posInArcade);
        //System.out.println("Draw Ghost");
        canvas.drawBitmap(ghostView, posInScreen.first() - bitmapWidth / 2,
                posInScreen.second() - bitmapHeight / 2, null);
    }

    /*
    We use this func to calculate the after move location in a direction,
    no matter the direction is valid or not.
     */
//    private TwoTuple move(int direction, long fps) {
//        int nextX = this.x;
//        int nextY = this.y;
//
//        // Move the pacman based on the direction variable
//        // and the speed of the previous frame
//        if (direction == LEFT) {
//            nextX = (int) (nextX - speed / fps);
//        }
//        if (direction == RIGHT) {
//            nextX = (int) (nextX + speed / fps);
//        }
//        if (direction == UP) {
//            nextY = (int) (nextY - speed / fps);
//        }
//        if (direction == DOWN) {
//            nextY = (int) (nextY + speed / fps);
//        }
//
//        // Stop the Pacman going off the screen
//        if (nextX - bitmapWidth / 2 < 0) {
//            nextX = bitmapWidth / 2;
//        }
//        if (nextX + bitmapWidth / 2 > mScreenX) {
//            nextX = mScreenX - bitmapWidth / 2;
//        }
//        if (nextY - bitmapHeight / 2 < 0) {
//            nextY = bitmapHeight / 2;
//        }
//        if (nextY + bitmapHeight / 2 > mScreenY) {
//            nextY = mScreenY - bitmapHeight / 2;
//        }
//
//        return new TwoTuple(nextX, nextY);
//        canvas.drawBitmap(ghostView, this.position.x-bitmapWidth/2, this.position.y-bitmapHeight/2, null);
//    }

//    public void updateLocation(long fps) {
//        /*
//        We cannot update is the fps is -1,
//        otherwise there will be a overflow
//        in speed/fps.
//         */
//        if (fps == -1 || fps == 0) {
//            return;
//        }
//
////        /***************************************/
////        //New Method
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
//           }
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
////
////        //else no move, stay there
//    }

//    //mathematical movement distance
//    private int mathematicalMoveDistance(long fps) {
//        return (int) (speed / fps);
//    }
//
//    //move as far as possible
//    private TwoTuple movedTo(int mathematicalMove, int movingDirection) {
//        int movedDistance = 0;
//        TwoTuple currPos = posInArcade;
//
////        System.out.println("Starting to move from: " + posInArcade.first() + " " + posInArcade.second());
//        boolean allowsMove = arcadeAnalyzer.allowsToGo(currPos, movingDirection);
//        // System.out.println("mathematicalMove: " + mathematicalMove);
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

    // ghost kill pacman
    @Override
    public void update(ArrayList<TwoTuple> route) {
        for(TwoTuple ghostBlock : this.blockRunThrough) {
            for(TwoTuple pacmanBlock: route) {
                if (ghostBlock.equals(pacmanBlock)) {
                    this.pacman.setDead(true);
                }
            }
        }
    }

    //**For ghost behavior;

    //This is the GhostBehavior Function, each of the ghost has a unique behavior;
    //The red Ghost will keep tracing the pacman;
    //The pink ghost will get in front of pacman to cut the him off;
    //The blue ghost will patrol a area
    //the yellow ghost will just move randomly;
    public void GhostBehavior(long fps){
        switch (this.GhostName){
            case "Red":
                int HorizontalGap = pacman.posInScreen.x - this.posInScreen.x;
                int VerticleGap = pacman.posInScreen.y - this.posInScreen.y;
                if(Math.abs(HorizontalGap) > Math.abs(VerticleGap)){
                    if(HorizontalGap < 0 ){
                        nextDirection = LEFT;
                        updateLocationByBlockFirst(fps);
                        if(needToChangeDir){
                            if(VerticleGap < 0){
                                nextDirection = DOWN;
                                updateLocationByBlockFirst(fps);
                                if(needToChangeDir) {
                                    nextDirection = UP;
                                    updateLocationByBlockFirst(fps);
                                }
                            }
                            else{
                                nextDirection = UP;
                                updateLocationByBlockFirst(fps);
                                if(needToChangeDir){
                                    nextDirection = DOWN;
                                    updateLocationByBlockFirst(fps);
                                }
                            }
                        }
                    }
                    else{
                        nextDirection = RIGHT;
                        updateLocationByBlockFirst(fps);
                        if(needToChangeDir){
                            if(VerticleGap < 0){
                                nextDirection = DOWN;
                                updateLocationByBlockFirst(fps);
                                if(needToChangeDir) {
                                    nextDirection = UP;
                                    updateLocationByBlockFirst(fps);
                                }
                            }
                            else{
                                nextDirection = UP;
                                updateLocationByBlockFirst(fps);
                                if(needToChangeDir){
                                    nextDirection = DOWN;
                                    updateLocationByBlockFirst(fps);
                                }
                            }
                        }
                    }
                }
                else{
                    if(VerticleGap < 0){
                        nextDirection = DOWN;
                        updateLocationByBlockFirst(fps);
                        if(needToChangeDir){
                            if(HorizontalGap < 0){
                                nextDirection = LEFT;
                                updateLocationByBlockFirst(fps);
                                if(needToChangeDir){
                                    nextDirection = RIGHT;
                                    updateLocationByBlockFirst(fps);
                                }
                            }
                            else{
                                nextDirection = RIGHT;
                                updateLocationByBlockFirst(fps);
                                if(needToChangeDir){
                                    nextDirection = LEFT;
                                    updateLocationByBlockFirst(fps);
                                }
                            }
                        }
                    }
                    else{
                        nextDirection = UP;
                        updateLocationByBlockFirst(fps);
                        if(needToChangeDir){
                            if(HorizontalGap < 0){
                                nextDirection = LEFT;
                                updateLocationByBlockFirst(fps);
                                if(needToChangeDir){
                                    nextDirection = RIGHT;
                                    updateLocationByBlockFirst(fps);
                                }
                            }
                            else{
                                nextDirection = RIGHT;
                                updateLocationByBlockFirst(fps);
                                if(needToChangeDir){
                                    nextDirection = LEFT;
                                    updateLocationByBlockFirst(fps);
                                }
                            }
                        }
                    }
                }

                break;
            case "Pink":
                //int HorizontalGap1 = pacman.getNextposX()-this.x;
                //int VerticleGap1 = pacman.getNextposY()-this.y;
                int HorizontalGap1 = pacman.posInScreen.x-this.posInScreen.x;
                int VerticleGap1 = pacman.posInScreen.y-this.posInScreen.y;
                if(Math.abs(HorizontalGap1) > Math.abs(VerticleGap1)){
                    if(HorizontalGap1 < 0 ){
                        nextDirection = LEFT;
                        updateLocationByBlockFirst(fps);
                        if(needToChangeDir){
                            if(VerticleGap1 < 0){
                                nextDirection = DOWN;
                                updateLocationByBlockFirst(fps);
                                if(needToChangeDir) {
                                    nextDirection = UP;
                                    updateLocationByBlockFirst(fps);
                                }
                            }
                            else{
                                nextDirection = UP;
                                updateLocationByBlockFirst(fps);
                                if(needToChangeDir){
                                    nextDirection = DOWN;
                                    updateLocationByBlockFirst(fps);
                                }
                            }
                        }
                    }
                    else{
                        nextDirection = RIGHT;
                        updateLocationByBlockFirst(fps);
                        if(needToChangeDir){
                            if(VerticleGap1 < 0){
                                nextDirection = DOWN;
                                updateLocationByBlockFirst(fps);
                                if(needToChangeDir) {
                                    nextDirection = UP;
                                    updateLocationByBlockFirst(fps);
                                }
                            }
                            else{
                                nextDirection = UP;
                                updateLocationByBlockFirst(fps);
                                if(needToChangeDir){
                                    nextDirection = DOWN;
                                    updateLocationByBlockFirst(fps);
                                }
                            }
                        }
                    }
                }
                else{
                    if(VerticleGap1 < 0){
                        nextDirection = DOWN;
                        updateLocationByBlockFirst(fps);
                        if(needToChangeDir){
                            if(HorizontalGap1 < 0){
                                nextDirection = LEFT;
                                updateLocationByBlockFirst(fps);
                                if(needToChangeDir){
                                    nextDirection = RIGHT;
                                    updateLocationByBlockFirst(fps);
                                }
                            }
                            else{
                                nextDirection = RIGHT;
                                updateLocationByBlockFirst(fps);
                                if(needToChangeDir){
                                    nextDirection = LEFT;
                                    updateLocationByBlockFirst(fps);
                                }
                            }
                        }
                    }
                    else{
                        nextDirection = UP;
                        updateLocationByBlockFirst(fps);
                        if(needToChangeDir){
                            if(HorizontalGap1 < 0){
                                nextDirection = LEFT;
                                updateLocationByBlockFirst(fps);
                                if(needToChangeDir){
                                    nextDirection = RIGHT;
                                    updateLocationByBlockFirst(fps);
                                }
                            }
                            else{
                                nextDirection = RIGHT;
                                updateLocationByBlockFirst(fps);
                                if(needToChangeDir){
                                    nextDirection = LEFT;
                                    updateLocationByBlockFirst(fps);
                                }
                            }
                        }
                    }
                }
                break;

            case "Blue":
                int inputDirection = -1;
                if(needToChangeDir) {
                    Random randomGenerator = new Random();
                    inputDirection = randomGenerator.nextInt(4);
                }
                switch (inputDirection) {
                    case -1:
                        //nextDirection = -1;
                        break;
                    case 0:
                        nextDirection = UP;
                        break;
                    case 1:
                        nextDirection = DOWN;
                        break;
                    case 2:
                        nextDirection = LEFT;
                        break;
                    case 3:
                        nextDirection = RIGHT;
                        break;
                }
                updateLocationByBlockFirst(fps);
                break;

            case "Yellow":
                int inputDirection1 = -1;
                if(needToChangeDir) {
                    Random randomGenerator = new Random();
                    inputDirection1 = randomGenerator.nextInt(4);
                }
                switch (inputDirection1) {
                    case -1:
                        //nextDirection = -1;
                        break;
                    case 0:
                        nextDirection = UP;
                        break;
                    case 1:
                        nextDirection = DOWN;
                        break;
                    case 2:
                        nextDirection = LEFT;
                        break;
                    case 3:
                        nextDirection = RIGHT;
                        break;
                }
                updateLocationByBlockFirst(fps);
                break;
        }

    }
}
