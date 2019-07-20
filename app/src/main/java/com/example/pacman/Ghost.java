package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Random;
import java.lang.*;

public class Ghost extends Runner implements GameObject {

    boolean alive;

//    //context of the game, used access Resource ptr
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
        currDirectionNextPosition = next;

        System.out.println("Ghost update: " + this.position.x + " " + this.position.y + " " + currDirectionNextPosition.x + " " + currDirectionNextPosition.y);

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
            NextMotionInfo info2 = motionInArcade.isValidMotion(currDirection);
            if (!info2.isValid()) {
                //System.out.println("Curr direction invalid");
                //Now we must remain at current position
                setPosition(info2.getPos());
                needToChangeDir = true;
                return;
            }
        }

        //System.out.println("No disturb");
        //We do not need to disturb current motion
        needToChangeDir = false;
        setPosition(currDirectionNextPosition);
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
