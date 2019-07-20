package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Pair;

import java.util.Random;

public class Ghost extends Runner implements GameObject {

    boolean alive;

//    //context of the game, used access Resource ptr
    private Context context;

    private Bitmap ghostView;

    private int bitmapWidth;
    private int bitmapHeight;

    private boolean needToChangeDir =false;

    private boolean collision;
    private Pacman pacman; // used for chase and kill Pacman

    private Arcade arcade;
    private MotionInArcade motionInArcade;

    private String GhostName;

    // ** for ghostbehavior;
    //public Ghost(Context context, int sx, int sy, Arcade arcade,
    //             Pacman pacman, Bitmap ghostView,  int direction,
    //             float speed, String name)
     public Ghost(Context context, TwoTuple screenResolution, Arcade arcade,
                 Pacman pacman, Bitmap ghostView,  int direction,
                    float speed) {
        this.context = context;
        mScreen = screenResolution;
        this.currDirection = direction;
        this.nextDirection = -1;

        this.ghostView = ghostView;
        bitmapWidth = ghostView.getWidth();
        bitmapHeight = ghostView.getHeight();

        this.speed = speed;

        this.position = new TwoTuple(arcade.getGhostPosition_pix());
        this.pacman = pacman;

     // ** for ghostbehavior;
     //   this.GhostName = name;

        motionInArcade = new MotionInArcade(arcade);
        this.arcade = arcade;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(ghostView, this.position.x-bitmapWidth/2, this.position.y-bitmapHeight/2, null);
    }

    @Override
    public void updateStatus(long fps){

    }

    public void updateLocation(long fps, Arcade arcade) {
        /*
        We cannot update is the fps is -1,
        otherwise there will be a overflow
        in speed/fps.
         */
        if (fps == -1) {
            return;
        }

        // next move in current direction
        TwoTuple next = move(currDirection, fps);
        currDirectionNextPosition = next;
        // next move in next direction
        nextDirectionNextPosition = move(nextDirection, fps);

        System.out.println("Ghost update: " + this.position.x + " " + this.position.y + " " + currDirectionNextPosition.x + " " + currDirectionNextPosition.y);

        //update motion info
        motionInArcade.updateMotionInfo(getMotionInfo());

        // we don't need to check if it's in decision region when direction changed,
        // because ghost will be on the middle of the block, which is contained in decision region
        if (nextDirection != currDirection) {
            //System.out.println("diff dir");
            //We need to check user's desired direction
            NextMotionInfo info1 = motionInArcade.isValidMotion(nextDirection);
            if (info1.isValid()) {
                //System.out.println("Valid Turn");
                //we can change direction.
                setPosition(info1.getPos());
                currDirection = nextDirection;
                needToChangeDir = false;
                return;
            }
        }
        /*
            either user did not input direction
            or user's desired input is invalid.
            We check if we can continue on current direction
             */
        //check if in decision region
        if(motionInArcade.inDecisionRegion()) {
            //System.out.println("in region");
            //we need to take action
            NextMotionInfo info2 = motionInArcade.isValidMotion(currDirection);
            if (!info2.isValid()) {
                //System.out.println("Curr direction invalid");
                setPosition(info2.getPos()); // Now we must remain at current position
                needToChangeDir = true;
                return;
            }
        }



        /*
        Now we can keep the original motion,
        but we still need to know if the next move
        is still on path.
         */

        Pair<TwoTuple, Boolean> checkNextMoveInBound = motionInArcade.mostDistantPathBlock(
                new TwoTuple(currDirectionNextPosition), nextDirection);

        if (checkNextMoveInBound.second){
            //System.out.println("No disturb");
            //We do not need to disturb current motion

            setPosition(currDirectionNextPosition);
            needToChangeDir = false;
            return;
        }
        System.out.println("Bad Fps: " + fps + "  gap: " + speed / fps +
                "  prev: " + this.position.x + " " + this.position.y + "  next: " + currDirectionNextPosition.x + " " + currDirectionNextPosition.y);
        this.blockPos = arcade.mapBlock(this.position, currDirection);
        System.out.println(this.blockPos.x + " " + this.blockPos.y);
        //setCenter(checkNextMoveInBound.first.first(), checkNextMoveInBound.first.second());
        setPosition(this.position);
        needToChangeDir = true;
    }

    public void updateMovementStatus(long fps, Arcade arcade) {
        int inputDirection = -1;
        if(needToChangeDir == true) {
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
        updateLocation(fps, arcade);
    }

    //**For ghost behavior;

/*
    //This is the GhostBehavior Function, each of the ghost has a unique behavior;
    //The red Ghost will keep tracing the pacman;
    //The pink ghost will get in front of pacman to cut the him off;
    //The blue ghost will patrol a area
    //the yellow ghost will just move randomly;
    public void GhostBehavior(String GhostName,long fps, Arcade arcade){
        switch (GhostName){
            case "Red":
                int HorizontalGap = pacman.getCenterX()-this.x;
                int VerticleGap = pacman.getCenterY()-this.y;
                if(Math.abs(VerticleGap) > Math.abs(HorizontalGap)){
                    if(HorizontalGap < 0 ){
                        nextDirection = LEFT;
                        if(needToChangeDir){
                            if(VerticleGap < 0){
                                nextDirection = DOWN;
                            }
                            else{
                                nextDirection = UP;
                            }
                        }
                    }
                    else{
                        nextDirection = RIGHT;
                        if(needToChangeDir){
                            if(VerticleGap < 0){
                                nextDirection = DOWN;
                            }
                            else{
                                nextDirection = UP;
                            }
                        }
                    }
                }
                else{
                    if(VerticleGap < 0){
                        nextDirection = DOWN;
                        if(needToChangeDir){
                            if(HorizontalGap < 0){
                                nextDirection = LEFT;
                            }
                            else{
                                nextDirection = RIGHT;
                            }
                        }
                    }
                    else{
                        nextDirection = UP;
                        if(needToChangeDir){
                            if(HorizontalGap < 0){
                                nextDirection = LEFT;
                            }
                            else{
                                nextDirection = RIGHT;
                            }
                        }
                    }
                }
                break;
            case "Pink":
                int HorizontalGap1 = pacman.getNextposX()-this.x;
                int VerticleGap1 = pacman.getNextposY()-this.y;
                if(Math.abs(VerticleGap1) > Math.abs(HorizontalGap1)){
                    if(HorizontalGap1 < 0 ){
                        nextDirection = LEFT;
                        if(needToChangeDir){
                            if(VerticleGap1 < 0){
                                nextDirection = DOWN;
                            }
                            else{
                                nextDirection = UP;
                            }
                        }
                    }
                    else{
                        nextDirection = RIGHT;
                        if(needToChangeDir){
                            if(VerticleGap1 < 0){
                                nextDirection = DOWN;
                            }
                            else{
                                nextDirection = UP;
                            }
                        }
                    }
                }
                else{
                    if(VerticleGap1 < 0){
                        nextDirection = DOWN;
                        if(needToChangeDir){
                            if(HorizontalGap1 < 0){
                                nextDirection = LEFT;
                            }
                            else{
                                nextDirection = RIGHT;
                            }
                        }
                    }
                    else{
                        nextDirection = UP;
                        if(needToChangeDir){
                            if(HorizontalGap1 < 0){
                                nextDirection = LEFT;
                            }
                            else{
                                nextDirection = RIGHT;
                            }
                        }
                    }
                }
                break;
            case "Blue":

                break;
            case "Yellow":
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
        }
        updateLocation(fps, arcade);
    }
    */


    // for ghostbehavior
    /*
    public String getGhostName(){
        return this.GhostName;
    }
    */
}
