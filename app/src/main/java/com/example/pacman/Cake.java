package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Pair;

import java.util.Random;

/*
* This is equivalent to the original cherry in Pac-Man game:
* random generate the cake onto the sceen and double the points earned;
* power up the 4 ghosts;
* the effect should disappear in few seconds;
* the cake should move around.
* */
public class Cake extends Runner implements GameObject {

    //Added variables
    private TwoTuple posInArcade;
    private ArcadeAnalyzer arcadeAnalyzer;

    private Arcade arcade;
    private Context context;
    private boolean needToChangeDir =false;
    private Bitmap rawCakeImg;
    private Bitmap scaledCakeImg;
    private MotionInArcade motionInArcade;

//    @Override
//    public void draw(Canvas canvas){
//        canvas.drawBitmap(scaledCakeImg, xCoordiante - bitmapWidth/2,
//                        yCoordinate - bitmapHeight/2, null);
//    }
    public Cake(Context context, TwoTuple screenResolution, Arcade arcade, float speed){
        this.speed = speed;
        this.arcade = arcade;
        this.context = context;
        currDirection = 0;
        nextDirection = -1;
        mScreen = screenResolution;
        position = arcade.getCakePosition_pix();
        rawCakeImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.cake);
        scaledCakeImg = Bitmap.createScaledBitmap(rawCakeImg,
                        mScreen.y / 15, mScreen.y/15, true);
        bitmapHeight = scaledCakeImg.getHeight();
        bitmapWidth = scaledCakeImg.getWidth();
        motionInArcade = new MotionInArcade(arcade);

    }

    @Override
    public void draw(Canvas canvas){
        TwoTuple screenPos = arcade.mapScreen(posInArcade);
        canvas.drawBitmap(scaledCakeImg, screenPos.first() - bitmapWidth/2,
                        screenPos.second() - bitmapHeight/2, null);
    }

    /*
    We use this func to calculate the after move location in a direction,
    no matter the direction is valid or not.
     */
//    private TwoTuple move(int direction, long fps) {
//        int nextX = this.xCoordiante;
//        int nextY = this.yCoordinate;
//
//        // Move the pacman based on the direction variable
//        // and the speed of the previous frame
//        if (direction == LEFT) {
//            nextX = (int)(nextX - speed / fps);
//        }
//        if (direction == RIGHT) {
//            nextX = (int)(nextX + speed / fps);
//        }
//        if (direction == UP) {
//            nextY = (int)(nextY - speed / fps);
//        }
//        if (direction == DOWN) {
//            nextY = (int)(nextY + speed / fps);
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
//        canvas.drawBitmap(scaledCakeImg, position.x - bitmapWidth/2,
//                        position.y - bitmapHeight/2, null);
//    }

    @Override
    public void updateStatus(long fps){

    }

    public void updateLocation(long fps, Arcade arcade) {
        /*
        We cannot update is the fps is -1,
        otherwise there will be a overflow
        in speed/fps.
         */
        if (fps == -1 || fps == 0) {
            return;
        }

//        //next move in current direction
//        TwoTuple next = move(currDirection, fps);
//        currDirectionNextX = next.first();
//        currDirectionNextY = next.second();
//
////        System.out.println("Global update: " + xCoordiante + " "
////        + yCoordinate + " " + currDirectionNextX + " " + currDirectionNextY);
//
//        //update motion info
//        motionInArcade.updateMotionInfo(getMotionInfo());
//
//        //check if in decision region
//        if(motionInArcade.inDecisionRegion()) {
//            //System.out.println("in region");
//            //we need to take action
//            if (nextDirection != currDirection) {
//                //System.out.println("diff dir");
//                //We need to check user's desired direction
//                NextMotionInfo info1 = motionInArcade.isValidMotion(nextDirection);
//                if (info1.isValid()) {
//                    //System.out.println("Valid Turn");
//                    //we can change direction.
//                    setCenter(info1.getPos().first(), info1.getPos().second());
//                    currDirection = nextDirection;
//                    needToChangeDir = false;
//                    return;
//                }
//            }
//
//            /*
//            either user did not input direction
//            or user's desired input is invalid.
//            We check if we can continue on current direction
//             */
//            NextMotionInfo info2 = motionInArcade.isValidMotion(currDirection);
//            if (!info2.isValid()) {
//                //System.out.println("Curr direction invalid");
//                //Now we must remain at current position
//                setCenter(info2.getPos().first(), info2.getPos().second());
//                needToChangeDir = true;
//                return;
//            }
//        }
//
//        //System.out.println("No disturb");
//        //We do not need to disturb current motion
//        needToChangeDir = false;
//        /*
//        Now we can keep the original motion,
//        but we still need to know if the next move
//        is still on path.
//         */
//
//        Pair<TwoTuple, Boolean> checkNextMoveInBound = motionInArcade.mostDistantPathBlock(
//                new TwoTuple(currDirectionNextX, currDirectionNextY), nextDirection);
//
//        if (checkNextMoveInBound.second){
//            //System.out.println("No disturb");
//            //We do not need to disturb current motion
//            setCenter(currDirectionNextX, currDirectionNextY);
//            return;
//        }
//        System.out.println("Bad Fps: " + fps + "  gap: " + speed / fps +
//                "  prev: " + xCoordiante + " " + yCoordinate + "  next: " + currDirectionNextX + " " + currDirectionNextY);
//        //setCenter(checkNextMoveInBound.first.first(), checkNextMoveInBound.first.second());
//        setCenter(xCoordiante, yCoordinate);


        /***************************************/
        //New Method
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

        //else no move, stay there
    }

    //mathematical movement distance
    private int mathematicalMoveDistance(long fps) {
        return (int)(speed / fps);
    }

    //move as far as possible
    private TwoTuple movedTo(int mathematicalMove, int movingDirection) {
        int movedDistance = 0;
        TwoTuple currPos = posInArcade;

//        System.out.println("Starting to move from: " + posInArcade.first() + " " + posInArcade.second());
        boolean allowsMove = arcadeAnalyzer.allowsToGo(currPos, movingDirection);
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

    //Constructor
    public Cake(Context context, int sx, int sy, Arcade arcade, ArcadeAnalyzer arcadeAnalyzer, float speed){
        this.speed = speed;
        this.arcade = arcade;
        this.context = context;
        currDirection = 0;
        nextDirection = -1;
//        mScreenX = sx;
//        mScreenY = sy;
//        xCoordiante = arcade.getCakeX_pix();
//        yCoordinate = arcade.getCakeY_pix();
        rawCakeImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.cake);
        scaledCakeImg = Bitmap.createScaledBitmap(rawCakeImg,
                sy / 15, sy/15, true);
        bitmapHeight = scaledCakeImg.getHeight();
        bitmapWidth = scaledCakeImg.getWidth();
        motionInArcade = new MotionInArcade(arcade);


        this.posInArcade = new TwoTuple(arcade.cakePosition);
        this.arcadeAnalyzer = arcadeAnalyzer;
    }
}
