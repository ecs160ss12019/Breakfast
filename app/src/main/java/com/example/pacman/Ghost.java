package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

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
        System.out.println("ghost posInScreen: " + posInScreen.first() + " " + posInScreen.second());
        // TwoTuple screenPos = arcade.mapScreen(posInArcade);
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


/*
    //move as far as possible
    private TwoTuple movedTo(int mathematicalMove, int movingDirection) {
        int movedDistance = 0;
        TwoTuple currPos = posInArcade;

//        System.out.println("Starting to move from: " + posInArcade.first() + " " + posInArcade.second());
        boolean allowsMove = arcadeAnalyzer.allowsToGo(currPos, movingDirection);
        // System.out.println("mathematicalMove: " + mathematicalMove);
        while (movedDistance <= mathematicalMove && allowsMove) {
            movedDistance += arcadeAnalyzer.blockDimension;
            currPos = TwoTuple.moveTo(currPos, movingDirection);

//            System.out.println("moved distance: " + movedDistance);
//            System.out.println("Moved to: " + currPos.first() + " " + currPos.second());
        }
>>>>>>> f5a6ca20222e7cc34ab58b02775e8d6978d4dcf0

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
    */

    private void movedTo(int mathematicalMove, int movingDirection) {
        int gap = mathematicalMove + pixelGap;
//        System.out.println("Math: " + mathematicalMove + ", PG: " + pixelGap);
//        System.out.println("Gap: " + gap + ", Block: " + gap / blockDimension);
//        System.out.println("CurrArcadePos: " + posInArcade.x + " " + posInArcade.y);
//        System.out.println("Starting to move from: " + posInArcade.first() + " " + posInArcade.second());
        boolean allowsMove = arcadeAnalyzer.allowsToGo(posInArcade, movingDirection);
//        while (movedDistance <= mathematicalMove && allowsMove) {
//            movedDistance += arcadeAnalyzer.blockDimension;
//            currPos = TwoTuple.moveTo(currPos, movingDirection);
//
////            System.out.println("moved distance: " + movedDistance);
////            System.out.println("Moved to: " + currPos.first() + " " + currPos.second());
//        }

//        System.out.println("!!!!!");
//        System.out.println("Finished moving to: " + currPos.first() + " " + currPos.second());

//        System.out.println("Before Looping: " + posInArcade.x + " " + posInArcade.y);
        while (true) {
            if (gap == 0) {
                posInScreen = arcade.mapScreen(posInArcade);
                pixelGap = 0;
                return;
            }

            if (!allowsMove) {
//                System.out.println("Arcade Pos: " + " ROW: " + posInArcade.x + " " + " COL: " + posInArcade.y);
//                System.out.println("Screen Pos: " + " X: " + posInScreen.x + " " + " Y: " + posInScreen.y);
//                System.out.println(" ");
//                System.out.println(" ");

                //Collide to wall, but still attempt to move, must stay at center
                if (gap > 0) {
                    posInScreen = arcade.mapScreen(posInArcade);
                    pixelGap = 0;
                    return;
                }

                //obstacle on right, gap subtracted to smaller than 0
                gap = gap + blockDimension;

//                boolean mustAdvanceToCenter = essentialCheck(gap);
//                if (mustAdvanceToCenter && gap != 0) {
//                    System.out.println("cannot close up");
//                    posInScreen = arcade.mapScreen(posInArcade);
//                    pixelGap = 0;
//                    return;
//                }

                //Just close up, it is okay
                //System.out.println("Closing up");
                pixelGap = gap - blockDimension;

                posInScreen = arcade.mapScreen(posInArcade);
                posInScreen = TwoTuple.addPixelGap(posInScreen, currDirection, pixelGap);

//                System.out.println("Case 1 done: " + posInArcade.x + " " + posInArcade.y);
//                System.out.println("Update screen Pos: " + posInScreen.x + " " + posInScreen.y);
                return;
            }

            //Now next block on this direction must be valid
            if (gap < 0) {
//                System.out.println("Case 1");
//                System.out.println("Arcade Pos: " + " ROW: " + posInArcade.x + " " + " COL: " + posInArcade.y);
//                System.out.println("Screen Pos: " + " X: " + posInScreen.x + " " + " Y: " + posInScreen.y);
//                System.out.println(" ");
//                System.out.println(" ");

                //gap < block size
                gap = gap + blockDimension;

//                System.out.println("Case 1: margin = " + gap);

                //posInArcade = TwoTuple.moveTo(posInArcade, currDirection);

                pixelGap = gap - blockDimension;

//                if (arcadeAnalyzer.allowsToGo(posInArcade, movingDirection)) {
//                    System.out.println("Allows");
//                    pixelGap = gap - blockDimension;
//                } else {
//                    if (essentialCheck(gap)) {
//                        System.out.println("Essential");
//                        pixelGap = 0;
//                    } else {
//                        System.out.println("not Essential");
//                        pixelGap = gap - blockDimension;
//                    }
//                }

                posInScreen = arcade.mapScreen(posInArcade);
                posInScreen = TwoTuple.addPixelGap(posInScreen, currDirection, pixelGap);

//                System.out.println("Case 1 done: " + posInArcade.x + " " + posInArcade.y);
//                System.out.println("Update screen Pos: " + posInScreen.x + " " + posInScreen.y);
                return;
            }

            //gap < 1/2 dimension, no change in arcade, change in screen
//            //return after done
////            if(gap < blockDimension / 2) {
//////                System.out.println("Case 2");
//////                System.out.println("Arcade Pos: " + " ROW: " + posInArcade.x + " " + " COL: " + posInArcade.y);
//////                System.out.println("Screen Pos: " + " X: " + posInScreen.x + " " + " Y: " + posInScreen.y);
//////                System.out.println(" ");
//////                System.out.println(" ");
////                //no need to move one more
////                //posInArcade = TwoTuple.moveTo(posInArcade, movingDirection);
////                if (arcadeAnalyzer.allowsToGo(posInArcade, movingDirection)) {
////                    posInScreen = TwoTuple.addPixelGap(posInScreen, movingDirection, gap);
////                }
////
////                return;
////            }
//
//            //gap < dimension, gap >= 1/2 dimension, change in arcade, change in screen
//            //return after done
////            if(gap < blockDimension) {
//////                System.out.println("Case 3");
//////                System.out.println("Arcade Pos: " + " ROW: " + posInArcade.x + " " + " COL: " + posInArcade.y);
//////                System.out.println("Screen Pos: " + " X: " + posInScreen.x + " " + " Y: " + posInScreen.y);
//////                System.out.println(" ");
//////                System.out.println(" ");
////                //move one more
////                posInArcade = TwoTuple.moveTo(posInArcade, movingDirection);
////                if (arcadeAnalyzer.allowsToGo(posInArcade, movingDirection)) {
////                    posInScreen = TwoTuple.addPixelGap(posInScreen, movingDirection, gap);
////                } else {
////                    posInScreen = arcade.mapScreen(posInArcade);
////                }
////                return;
////            }
//
//            //gap > dimension, change in arcade, change in screen
//            //continue after done
////            System.out.println("Case 4");
////            System.out.println("Gap: " + gap);
////            System.out.println("Arcade Pos: " + " ROW: " + posInArcade.x + " " + " COL: " + posInArcade.y);
////            System.out.println("Screen Pos: " + " X: " + posInScreen.x + " " + " Y: " + posInScreen.y);
////            System.out.println(" ");
////            System.out.println(" ");
            gap -= blockDimension;
            posInArcade = TwoTuple.moveTo(posInArcade, movingDirection);
            allowsMove = arcadeAnalyzer.allowsToGo(posInArcade, movingDirection);
            //System.out.println("Looped Once: " + posInArcade.x + " " + posInArcade.y);
        }
    }

    /*
    //**For ghost behavior;

    //This is the GhostBehavior Function, each of the ghost has a unique behavior;
    //The red Ghost will keep tracing the pacman;
    //The pink ghost will get in front of pacman to cut the him off;
    //The blue ghost will patrol a area
    //the yellow ghost will just move randomly;
    public void GhostBehavior(long fps){
        switch (this.GhostName){
            case "Red":
<<<<<<< HEAD
                int HorizontalGap = pacman.posInScreen.x - this.posInScreen.x;
                int VerticleGap = pacman.posInScreen.y - this.posInScreen.y;
=======
                int HorizontalGap = pacman.getCurrentX()-this.posInArcade.x;
                int VerticleGap = pacman.getCurrentY()-this.posInArcade.y;
>>>>>>> f5a6ca20222e7cc34ab58b02775e8d6978d4dcf0
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
<<<<<<< HEAD
                int HorizontalGap1 = pacman.posInScreen.x-this.posInScreen.x;
                int VerticleGap1 = pacman.posInScreen.y-this.posInScreen.y;
=======
                int HorizontalGap1 = pacman.getCurrentX()-this.posInArcade.x;
                int VerticleGap1 = pacman.getCurrentY()-this.posInArcade.y;
>>>>>>> f5a6ca20222e7cc34ab58b02775e8d6978d4dcf0
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
    */

    public void GhostBehavior(long fps) {
        if(pacman == null) return; // we don't need to setup ghost behavior when there is no Pacman in the current arcade .
        GhostBehavior GB = new GhostBehavior(pacman.posInArcade, this.posInArcade, pacman.currDirection, arcadeAnalyzer);
        switch (this.GhostName) {
            case "Red":
                nextDirection = GB.chase();
                updateLocationByBlockFirst(fps);
            case "Pink":
                nextDirection = GB.chase();
                updateLocationByBlockFirst(fps);
            case "Blue":
                nextDirection = GB.RandomMove();
                updateLocationByBlockFirst(fps);
            case "Yellow":
                nextDirection = GB.RandomMove();
                updateLocationByBlockFirst(fps);
        }
    }
}
