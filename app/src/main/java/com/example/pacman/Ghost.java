package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Random;
import java.lang.*;

public class Ghost implements GameObject {

    final int LEFT = 0;
    final int RIGHT = 1;
    final int UP = 2;
    final int DOWN = 3;
    boolean alive;

    //coordinate
    private int x;
    private int y;
    private int currDirectionNextX;
    private int currDirectionNextY;
    private float speed;
    private int mScreenX;
    private int mScreenY;

    //context of the game, used access Resource ptr
    private Context context;

    private Bitmap ghostView;

    private int bitmapWidth;
    private int bitmapHeight;

    private int currDirection;
    private int nextDirection;
    private boolean needToChangeDir =false;

    private boolean collision;
    private Pacman pacman; // used for chase and kill Pacman

    private MotionInArcade motionInArcade;

    private String GhostName;

    // ** for ghostbehavior;
    //public Ghost(Context context, int sx, int sy, Arcade arcade,
    //             Pacman pacman, Bitmap ghostView,  int direction,
    //             float speed, String name)
    public Ghost(Context context, int sx, int sy, Arcade arcade,
                 Pacman pacman, Bitmap ghostView,  int direction,
                    float speed) {
        this.context = context;
        mScreenX = sx;
        mScreenY = sy;
        this.currDirection = direction;
        this.nextDirection = -1;

        this.ghostView = ghostView;
        bitmapWidth = ghostView.getWidth();
        bitmapHeight = ghostView.getHeight();

        this.speed = speed;

        this.x = arcade.getGhostX_pix();
        this.y = arcade.getGhostY_pix();
        this.pacman = pacman;

     // ** for ghostbehavior;
     //   this.GhostName = name;

        motionInArcade = new MotionInArcade(arcade);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(ghostView, x-bitmapWidth/2, y-bitmapHeight/2, null);
    }

    /*
    We use this func to calculate the after move location in a direction,
    no matter the direction is valid or not.
     */
    private TwoTuple move(int direction, long fps) {
        int nextX = this.x;
        int nextY = this.y;

        // Move the pacman based on the direction variable
        // and the speed of the previous frame
        if (direction == LEFT) {
            nextX = (int)(nextX - speed / fps);
        }
        if (direction == RIGHT) {
            nextX = (int)(nextX + speed / fps);
        }
        if (direction == UP) {
            nextY = (int)(nextY - speed / fps);
        }
        if (direction == DOWN) {
            nextY = (int)(nextY + speed / fps);
        }

        // Stop the Pacman going off the screen
        if (nextX - bitmapWidth / 2 < 0) {
            nextX = bitmapWidth / 2;
        }
        if (nextX + bitmapWidth / 2 > mScreenX) {
            nextX = mScreenX - bitmapWidth / 2;
        }
        if (nextY - bitmapHeight / 2 < 0) {
            nextY = bitmapHeight / 2;
        }
        if (nextY + bitmapHeight / 2 > mScreenY) {
            nextY = mScreenY - bitmapHeight / 2;
        }

        return new TwoTuple(nextX, nextY);
    }

    @Override
    public void updateStatus(long fps){

    }

    @Override
    public int getCenterX() {
        return this.x;
    }

    @Override
    public int getCenterY() {
        return this.y;
    }

    //The starting point need to be initialized after construction
    //if collision, use this to roll back
    @Override
    public void setCenter(int centerX, int centerY) {
        this.x = centerX;
        this.y = centerY;
    }

    @Override
    public int getCurrDirection() {
        return currDirection;
    }

    @Override
    public int getNextDirection() {
        return nextDirection;
    }

    @Override
    public ArrayList<Integer> getMotionInfo() {
        ArrayList<Integer> motion = new ArrayList<>();
        motion.add(this.getCenterX());
        motion.add(this.getCenterY());
        motion.add(currDirectionNextX);
        motion.add(currDirectionNextY);
        motion.add(currDirection);
        return motion;
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

        // The first Movement solution was based on Collision
        // Siqi updated movement solution based on arcade, so this part is comment out

//        CollisionDetector collisionDetector = new CollisionDetector();
//
//        int nextX = 0;
//        int nextY = 0;
//
//        if (nextDirection != -1) {
//            Pair<Integer, Integer> next = move(nextDirection, fps);
//            nextX = next.first;
//            nextY = next.second;
//
//            //Check collision
//            ArrayList<Obstacle> obstacles =arcade.getObstacleList(nextX, nextY);
//            Obstacle ghostReference = new Obstacle(nextX, nextY,
//                    (int)(bitmapWidth*0.8), (int)(bitmapHeight*0.8));
//
//            collision = collisionDetector.collisionExist(ghostReference, obstacles);
//            if (!collision) {
//                setCenter(nextX, nextY);
//                currDirection = nextDirection;
//                return;
//            }
//        }
//
//        /*
//        Either there is no new direction,
//        or that direction does not work.
//        Try moving in current direction, if it do
//        not work as well, stay in current position
//         */
//        Pair<Integer, Integer> next = move(currDirection, fps);
//        nextX = next.first;
//        nextY = next.second;
//
//        //Check collision
//        ArrayList<Obstacle> obstacles = arcade.getObstacleList(nextX, nextY);
//        Obstacle ghostReference = new Obstacle(nextX, nextY,
//                (int)(bitmapWidth * 0.8), (int)(bitmapHeight * 0.8));
//
//        collision = collisionDetector.collisionExist(ghostReference, obstacles);
//        if(!collision) {
//            setCenter(nextX, nextY);
//        }

        //next move in current direction
        TwoTuple next = move(currDirection, fps);
        currDirectionNextX = next.first();
        currDirectionNextY = next.second();

        //System.out.println("Global update: " + x + " " + y + " " + currDirectionNextX + " " + currDirectionNextY);

        //update motion info
        motionInArcade.updateMotionInfo(getMotionInfo());

        //check if in decision region
        if(motionInArcade.inDecisionRegion()) {
            //System.out.println("in region");
            //we need to take action
            if (nextDirection != currDirection) {
                //System.out.println("diff dir");
                //We need to check user's desired direction
                NextMotionInfo info1 = motionInArcade.isValidMotion(nextDirection);
                if (info1.isValid()) {
                    //System.out.println("Valid Turn");
                    //we can change direction.
                    setCenter(info1.getPos().first(), info1.getPos().second());
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
            NextMotionInfo info2 = motionInArcade.isValidMotion(currDirection);
            if (!info2.isValid()) {
                //System.out.println("Curr direction invalid");
                //Now we must remain at current position
                setCenter(info2.getPos().first(), info2.getPos().second());
                needToChangeDir = true;
                return;
            }
        }

        //System.out.println("No disturb");
        //We do not need to disturb current motion
        needToChangeDir = false;

        /*
        Now we can keep the original motion,
        but we still need to know if the next move
        is still on path.
         */

        Pair<TwoTuple, Boolean> checkNextMoveInBound = motionInArcade.mostDistantPathBlock(
                new TwoTuple(currDirectionNextX, currDirectionNextY), nextDirection);

        if (checkNextMoveInBound.second){
            //System.out.println("No disturb");
            //We do not need to disturb current motion
            setCenter(currDirectionNextX, currDirectionNextY);
            return;
        }
        System.out.println("Bad Fps: " + fps + "  gap: " + speed / fps +
                "  prev: " + x + " " + y + "  next: " + currDirectionNextX + " " + currDirectionNextY);
        //setCenter(checkNextMoveInBound.first.first(), checkNextMoveInBound.first.second());
        setCenter(x, y);
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
