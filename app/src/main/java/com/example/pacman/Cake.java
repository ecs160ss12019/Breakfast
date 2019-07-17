package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Random;

/*
* This is equivalent to the original cherry in Pac-Man game:
* random generate the cake onto the sceen and double the points earned;
* power up the 4 ghosts;
* the effect should disappear in few seconds;
* the cake should move around.
* */
public class Cake implements GameObject {
    final int LEFT = 0;
    final int RIGHT = 1;
    final int UP = 2;
    final int DOWN = 3;

    private Arcade arcade;
    private Context context;
    private boolean needToChangeDir =false;
    private int mScreenX;
    private int mScreenY;
    private int bitmapWidth;
    private int bitmapHeight;
    private int xCoordiante;
    private int yCoordinate;
    private int currDirectionNextX;
    private int currDirectionNextY;
    private int currDirection;
    private int nextDirection;
    private float speed;
    private Bitmap rawCakeImg;
    private Bitmap scaledCakeImg;
    private MotionInArcade motionInArcade;

    public Cake(Context context, int sx, int sy, Arcade arcade, float speed){
        this.speed = speed;
        this.arcade = arcade;
        this.context = context;
        currDirection = 0;
        nextDirection = -1;
        mScreenX = sx;
        mScreenY = sy;
        xCoordiante = arcade.getCakeX_pix();
        yCoordinate = arcade.getCakeY_pix();
        rawCakeImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.cake);
        scaledCakeImg = Bitmap.createScaledBitmap(rawCakeImg,
                        sy / 15, sy/15, true);
        bitmapHeight = scaledCakeImg.getHeight();
        bitmapWidth = scaledCakeImg.getWidth();
        motionInArcade = new MotionInArcade(arcade);

    }

    @Override
    public void draw(Canvas canvas){
        canvas.drawBitmap(scaledCakeImg, xCoordiante - bitmapWidth/2,
                        yCoordinate - bitmapHeight/2, null);
    }

    /*
    We use this func to calculate the after move location in a direction,
    no matter the direction is valid or not.
     */
    private TwoTuple move(int direction, long fps) {
        int nextX = this.xCoordiante;
        int nextY = this.yCoordinate;

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
        return this.xCoordiante;
    }

    @Override
    public int getCenterY() {
        return this.yCoordinate;
    }

    //The starting point need to be initialized after construction
    //if collision, use this to roll back
    @Override
    public void setCenter(int centerX, int centerY) {
        this.xCoordiante = centerX;
        this.yCoordinate = centerY;
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

        //next move in current direction
        TwoTuple next = move(currDirection, fps);
        currDirectionNextX = next.first();
        currDirectionNextY = next.second();

//        System.out.println("Global update: " + xCoordiante + " "
//        + yCoordinate + " " + currDirectionNextX + " " + currDirectionNextY);

        //update motion info
        motionInArcade.updateMotionInfo(getMotionInfo());

        //check if in decision region
        if(motionInArcade.inDecisionRegion()) {
            System.out.println("in region");
            //we need to take action
            if (nextDirection != currDirection) {
                System.out.println("diff dir");
                //We need to check user's desired direction
                NextMotionInfo info1 = motionInArcade.isValidMotion(nextDirection);
                if (info1.isValid()) {
                    System.out.println("Valid Turn");
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
                System.out.println("Curr direction invalid");
                //Now we must remain at current position
                setCenter(info2.getPos().first(), info2.getPos().second());
                needToChangeDir = true;
                return;
            }
        }

        System.out.println("No disturb");
        //We do not need to disturb current motion
        needToChangeDir = false;
        setCenter(currDirectionNextX, currDirectionNextY);
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
}
