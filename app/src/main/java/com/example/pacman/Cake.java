package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/*
* This is equivalent to the original cherry in Pac-Man game:
* random generate the cake onto the sceen and double the points earned;
* power up the 4 ghosts;
* the effect should disappear in few seconds;
* the cake should move around.
* */
public class Cake extends Runner implements GameObject {

    //Added variables
    private ArcadeAnalyzer arcadeAnalyzer;

    private Context context;
    private boolean needToChangeDir =false;
    private Bitmap rawCakeImg;
    private Bitmap scaledCakeImg;

    private boolean isDead;
    //eaten means whether points was added.
    private boolean eaten;

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

//    @Override
//    public void draw(Canvas canvas){
//        canvas.drawBitmap(scaledCakeImg, xCoordiante - bitmapWidth/2,
//                        yCoordinate - bitmapHeight/2, null);
//    }
    public Cake(Context context, TwoTuple screenResolution, Arcade arcade, float speed, CollisionSubject collision){
        super(screenResolution, speed, collision);
        this.speed = speed;
        this.arcade = arcade;
        this.context = context;
        currDirection = 0;
        nextDirection = -1;
        mScreen = screenResolution;
        posInScreen = arcade.getCakePosition_pix();
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

    public void updateLocation(long fps, Arcade arcade) {
        /*
        We cannot update is the fps is -1,
        otherwise there will be a overflow
        in speed/fps.
         */
        if (fps == -1 || fps == 0) {
            return;
        }


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

    //Constructor
    public Cake(Context context, TwoTuple screen, Arcade arcade, ArcadeAnalyzer arcadeAnalyzer, float speed, CollisionSubject collision){
        super(screen, speed, collision);
        this.arcade = arcade;
        this.context = context;
        currDirection = 0;
        nextDirection = -1;
//        xCoordiante = arcade.getCakeX_pix();
//        yCoordinate = arcade.getCakeY_pix();
        rawCakeImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.cake);
        scaledCakeImg = Bitmap.createScaledBitmap(rawCakeImg,
                screen.y / 15, screen.y/15, true);
        bitmapHeight = scaledCakeImg.getHeight();
        bitmapWidth = scaledCakeImg.getWidth();
        motionInArcade = new MotionInArcade(arcade);


        this.posInArcade = new TwoTuple(arcade.cakePosition);
        this.arcadeAnalyzer = arcadeAnalyzer;
    }
}
