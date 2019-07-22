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

    private Cake(Context context, TwoTuple screen, float speed, CollisionSubject collision) {
        super(screen, speed, collision);
        this.context = context;
        currDirection = TwoTuple.LEFT;
        nextDirection = -1;
        rawCakeImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.cake);
        scaledCakeImg = Bitmap.createScaledBitmap(rawCakeImg,mScreen.y / 15, mScreen.y/15, true);
        bitmapHeight = scaledCakeImg.getHeight();
        bitmapWidth = scaledCakeImg.getWidth();
    }

    // Constructor1
    public Cake(Context context, TwoTuple screen, float speed, CollisionSubject collision, Arcade arcade){
        this(context, screen, speed, collision);

        this.arcade = arcade;
        this.posInScreen = arcade.getCakePosition_pix();
        this.posInArcade = new TwoTuple(arcade.cakePosition);
        this.posInArcadeInit = posInArcade;

        motionInArcade = new MotionInArcade(arcade);
    }

    //Constructor2
    public Cake(Context context, TwoTuple screen, float speed, CollisionSubject collision, Arcade arcade, ArcadeAnalyzer arcadeAnalyzer){
        this(context, screen, speed, collision);

        this.arcade = arcade;
        this.posInArcade = new TwoTuple(arcade.cakePosition);
        this.posInScreen = arcade.mapScreen(posInArcade);
        this.posInArcadeInit = posInArcade;

        this.arcadeAnalyzer = arcadeAnalyzer;
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
}
