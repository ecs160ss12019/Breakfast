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
        this.posInArcade = new TwoTuple(arcade.ghostPosition);
        this.pacman = pacman;

        motionInArcade = new MotionInArcade(arcade);
        this.arcade = arcade;
    }

    //Constructor2
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

        this.pacman = pacman;

        // ** for ghostbehavior;
        //   this.GhostName = name;

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
        System.out.println("Draw Ghost");
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

        if (nextDirection != currDirection && nextDirection != -1) {
            //try new direction
            boolean allowsTurn = arcadeAnalyzer.allowsToGo(posInArcade, nextDirection);

            if (allowsTurn) {
                //Turn and go
                posInArcade = movedTo(mathematicalMove, nextDirection);

                currDirection = nextDirection;
                needToChangeDir = false;
                return;
           }
        }

        //Either not able to turn or not desired to turn
        boolean allowsMove = arcadeAnalyzer.allowsToGo(posInArcade, currDirection);
        if (allowsMove) {
            //move and go
            posInArcade = movedTo(mathematicalMove, currDirection);
            return;
        }

        needToChangeDir = true;
//
//        //else no move, stay there
    }

    //mathematical movement distance
    private int mathematicalMoveDistance(long fps) {
        return (int) (speed / fps);
    }

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

    //**For ghost behavior;

    //This is the GhostBehavior Function, each of the ghost has a unique behavior;
    //The red Ghost will keep tracing the pacman;
    //The pink ghost will get in front of pacman to cut the him off;
    //The blue ghost will patrol a area
    //the yellow ghost will just move randomly;
    public void GhostBehavior(long fps){
        switch (this.GhostName){
            case "Red":
                int HorizontalGap = pacman.getCurrentX()-this.posInScreen.x;
                int VerticleGap = pacman.getCurrentY()-this.posInScreen.y;
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
                int HorizontalGap1 = pacman.getCurrentX()-this.posInScreen.x;
                int VerticleGap1 = pacman.getCurrentY()-this.posInScreen.y;
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
