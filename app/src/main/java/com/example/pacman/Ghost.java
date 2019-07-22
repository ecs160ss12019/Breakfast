package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Random;

public class Ghost extends Runner implements GameObject, CollisionObserver {

    boolean alive;

    //ADDED variables
    private ArcadeAnalyzer arcadeAnalyzer;

    //coordinate
    private String GhostName;
    int blockDimension;


    //context of the game, used access Resource ptr
    //context of the game, used access Resource ptr
    private Context context;

    private Bitmap ghostView;

    private int bitmapWidth;
    private int bitmapHeight;
    private boolean needToChangeDir = false;

    private Pacman pacman; // used for chase and kill Pacman
/*
    // ** for ghostbehavior;
    //public Ghost(Context context, int sx, int sy, Arcade arcade,
    //             Pacman pacman, Bitmap ghostView,  int direction,
    //             float speed, String name)
     public Ghost(Context context, TwoTuple screenResolution, Arcade arcade,
                 Pacman pacman, Bitmap ghostView,
                    float speed, CollisionSubject collision, String Name) {
         super(screenResolution, speed, collision);
        this.context = context;
        this.currDirection = UP;
        this.nextDirection = -1;
        this.GhostName = Name;

        this.ghostView = ghostView;
        bitmapWidth = ghostView.getWidth();
        bitmapHeight = ghostView.getHeight();

        this.posInScreen = new TwoTuple(arcade.getGhostPosition_pix());
        this.pacman = pacman;

        motionInArcade = new MotionInArcade(arcade);
        this.arcade = arcade;
    }
 */

    //Currently used Constructor
    public Ghost(Context context, TwoTuple screen, Arcade arcade,
                 Pacman pacman, Bitmap ghostView, ArcadeAnalyzer arcadeAnalyzer,
                 float speed, CollisionSubject collision, String Name) {
         super(screen, speed, collision);
        this.context = context;
        this.currDirection = UP;
        this.nextDirection = -1;
        this.GhostName = Name;

        this.ghostView = ghostView;
        bitmapWidth = ghostView.getWidth();
        bitmapHeight = ghostView.getHeight();
        this.blockDimension = arcadeAnalyzer.blockDimension;

        this.pacman = pacman;

        motionInArcade = new MotionInArcade(arcade);

        this.arcadeAnalyzer = arcadeAnalyzer;
        this.posInArcade = new TwoTuple(arcade.ghostPosition);
        this.posInScreen = new TwoTuple(arcade.getGhostPosition_pix());
        this.posInArcadeInit = posInArcade;
        this.arcade = arcade;
    }

//    @Override
//    public void draw(Canvas canvas) {
//        canvas.drawBitmap(ghostView, x - bitmapWidth / 2, y - bitmapHeight / 2, null);
//    }

    @Override
    public void draw(Canvas canvas) {
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

    public void updateLocation(long fps) {
        /*
        We cannot update is the fps is -1,
        otherwise there will be a overflow
        in speed/fps.
         */
        if (fps == -1 || fps == 0) {
            return;
        }

//        /***************************************/
//        //New Method
        int mathematicalMove = mathematicalMoveDistance(fps);

        if (mathematicalMove == 0) return;

        if (nextDirection != currDirection && nextDirection != -1) {
            //System.out.println("Want to turn");

            //try new direction
            boolean allowsTurn = arcadeAnalyzer.allowsToGo(posInArcade, nextDirection);

            if (allowsTurn) {
                //System.out.println("Allows to turn");
                //Turn and go
                //movedTo(mathematicalMove, nextDirection);

                if (essentialCheck(mathematicalMove)) {
                    //posInArcade = TwoTuple.moveTo(posInArcade, nextDirection);
                    posInScreen = arcade.mapScreen(posInArcade);
                    pixelGap = 0;
                    currDirection = nextDirection;
                    needToChangeDir = false;
                    return;
                }

                //System.out.println("Did not turn");
            }
        }

        needToChangeDir = true;
        movedTo(mathematicalMove, currDirection);
//
//        //else no move, stay there
    }

    private boolean essentialCheck(int mathGap) {
        //Will it actually go pass the
        //center of the turning block
        //if we continue the previous motion?
        //To due with this, lets try to
        //add the gap on first!

        //The turning point on screen
        TwoTuple turningPoint = arcade.mapScreen(posInArcade);

        //get sign
        int Sign_x_prev = (int)Math.signum(posInScreen.x - turningPoint.x);
        int Sign_y_prev = (int)Math.signum(posInScreen.y - turningPoint.y);

        //try to add gap
        TwoTuple posPost = TwoTuple.addPixelGap(posInScreen, currDirection, mathGap);

        //update sign
        int Sign_x_post = (int)Math.signum(posPost.x - turningPoint.x);
        int Sign_y_post = (int)Math.signum(posPost.y - turningPoint.y);

//        System.out.println("Checking essential");
//        System.out.println("Curr: " + posInScreen.x + " " + posInScreen.y);
//        System.out.println("Turn: " + turningPoint.x + " " + turningPoint.y);
//        System.out.println("post: " + posPost.x + " " + posPost.y);
//        System.out.println(" ");
//        System.out.println("Checking sign");
//        System.out.println("Curr Diff: " + Sign_x_prev + " " + Sign_y_prev);
//        System.out.println("Post Diff: " + Sign_x_post + " " + Sign_y_post);

        //We must advance to center!
        //Or we are on the center
        if (Sign_x_post != Sign_x_prev || Sign_y_post != Sign_y_prev ||
                (Sign_x_prev == 0 && Sign_y_prev == 0)) {
            System.out.println("true");
            return true;
        }
        System.out.println("false");
        return false;
    }

    //mathematical movement distance
    //private int mathematicalMoveDistance(long fps) {
    //    return (int) (speed / fps);
    //}

    private int mathematicalMoveDistance(long fps) {
        return (int)(75/fps);
    }
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

//        System.out.println("!!!!!");
//        System.out.println("Finished moving to: " + currPos.first() + " " + currPos.second());
        return currPos;
    }

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

    //**For ghost behavior;

    //This is the GhostBehavior Function, each of the ghost has a unique behavior;
    //The red Ghost will keep tracing the pacman;
    //The pink ghost will get in front of pacman to cut the him off;
    //The blue ghost will patrol a area
    //the yellow ghost will just move randomly;
    public void GhostBehavior(long fps){
        switch (this.GhostName){
            case "Red":
                int HorizontalGap = pacman.getCurrentX()-this.posInArcade.x;
                int VerticleGap = pacman.getCurrentY()-this.posInArcade.y;
                if(Math.abs(HorizontalGap) > Math.abs(VerticleGap)){
                    if(HorizontalGap < 0 ){
                        nextDirection = LEFT;
                        updateLocation(fps);
                        if(needToChangeDir){
                            if(VerticleGap < 0){
                                nextDirection = DOWN;
                                updateLocation(fps);
                                if(needToChangeDir) {
                                    nextDirection = UP;
                                    updateLocation(fps);
                                }
                            }
                            else{
                                nextDirection = UP;
                                updateLocation(fps);
                                if(needToChangeDir){
                                    nextDirection = DOWN;
                                    updateLocation(fps);
                                }
                            }
                        }
                    }
                    else{
                        nextDirection = RIGHT;
                        updateLocation(fps);
                        if(needToChangeDir){
                            if(VerticleGap < 0){
                                nextDirection = DOWN;
                                updateLocation(fps);
                                if(needToChangeDir) {
                                    nextDirection = UP;
                                    updateLocation(fps);
                                }
                            }
                            else{
                                nextDirection = UP;
                                updateLocation(fps);
                                if(needToChangeDir){
                                    nextDirection = DOWN;
                                    updateLocation(fps);
                                }
                            }
                        }
                    }
                }
                else{
                    if(VerticleGap < 0){
                        nextDirection = DOWN;
                        updateLocation(fps);
                        if(needToChangeDir){
                            if(HorizontalGap < 0){
                                nextDirection = LEFT;
                                updateLocation(fps);
                                if(needToChangeDir){
                                    nextDirection = RIGHT;
                                    updateLocation(fps);
                                }
                            }
                            else{
                                nextDirection = RIGHT;
                                updateLocation(fps);
                                if(needToChangeDir){
                                    nextDirection = LEFT;
                                    updateLocation(fps);
                                }
                            }
                        }
                    }
                    else{
                        nextDirection = UP;
                        updateLocation(fps);
                        if(needToChangeDir){
                            if(HorizontalGap < 0){
                                nextDirection = LEFT;
                                updateLocation(fps);
                                if(needToChangeDir){
                                    nextDirection = RIGHT;
                                    updateLocation(fps);
                                }
                            }
                            else{
                                nextDirection = RIGHT;
                                updateLocation(fps);
                                if(needToChangeDir){
                                    nextDirection = LEFT;
                                    updateLocation(fps);
                                }
                            }
                        }
                    }
                }

                break;
            case "Pink":
                //int HorizontalGap1 = pacman.getNextposX()-this.x;
                //int VerticleGap1 = pacman.getNextposY()-this.y;
                int HorizontalGap1 = pacman.getCurrentX()-this.posInArcade.x;
                int VerticleGap1 = pacman.getCurrentY()-this.posInArcade.y;
                if(Math.abs(HorizontalGap1) > Math.abs(VerticleGap1)){
                    if(HorizontalGap1 < 0 ){
                        nextDirection = LEFT;
                        updateLocation(fps);
                        if(needToChangeDir){
                            if(VerticleGap1 < 0){
                                nextDirection = DOWN;
                                updateLocation(fps);
                                if(needToChangeDir) {
                                    nextDirection = UP;
                                    updateLocation(fps);
                                }
                            }
                            else{
                                nextDirection = UP;
                                updateLocation(fps);
                                if(needToChangeDir){
                                    nextDirection = DOWN;
                                    updateLocation(fps);
                                }
                            }
                        }
                    }
                    else{
                        nextDirection = RIGHT;
                        updateLocation(fps);
                        if(needToChangeDir){
                            if(VerticleGap1 < 0){
                                nextDirection = DOWN;
                                updateLocation(fps);
                                if(needToChangeDir) {
                                    nextDirection = UP;
                                    updateLocation(fps);
                                }
                            }
                            else{
                                nextDirection = UP;
                                updateLocation(fps);
                                if(needToChangeDir){
                                    nextDirection = DOWN;
                                    updateLocation(fps);
                                }
                            }
                        }
                    }
                }
                else{
                    if(VerticleGap1 < 0){
                        nextDirection = DOWN;
                        updateLocation(fps);
                        if(needToChangeDir){
                            if(HorizontalGap1 < 0){
                                nextDirection = LEFT;
                                updateLocation(fps);
                                if(needToChangeDir){
                                    nextDirection = RIGHT;
                                    updateLocation(fps);
                                }
                            }
                            else{
                                nextDirection = RIGHT;
                                updateLocation(fps);
                                if(needToChangeDir){
                                    nextDirection = LEFT;
                                    updateLocation(fps);
                                }
                            }
                        }
                    }
                    else{
                        nextDirection = UP;
                        updateLocation(fps);
                        if(needToChangeDir){
                            if(HorizontalGap1 < 0){
                                nextDirection = LEFT;
                                updateLocation(fps);
                                if(needToChangeDir){
                                    nextDirection = RIGHT;
                                    updateLocation(fps);
                                }
                            }
                            else{
                                nextDirection = RIGHT;
                                updateLocation(fps);
                                if(needToChangeDir){
                                    nextDirection = LEFT;
                                    updateLocation(fps);
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
                updateLocation(fps);
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
                updateLocation(fps);
                break;
        }

    }
}
