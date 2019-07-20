package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.Random;

/*
* This is equivalent to the original cherry in Pac-Man game:
* random generate the cake onto the sceen and double the points earned;
* power up the 4 ghosts;
* the effect should disappear in few seconds;
* the cake should move around.
* */
public class Cake extends Runner implements GameObject {

    private Arcade arcade;
    private Context context;
    private boolean needToChangeDir =false;
    private Bitmap rawCakeImg;
    private Bitmap scaledCakeImg;
    private MotionInArcade motionInArcade;

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
        canvas.drawBitmap(scaledCakeImg, position.x - bitmapWidth/2,
                        position.y - bitmapHeight/2, null);
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

        //next move in current direction
        TwoTuple next = move(currDirection, fps);
        currDirectionNextPosition = next;
        // next move in next direction
        nextDirectionNextPosition = move(nextDirection, fps);

//        System.out.println("Global update: " + xCoordiante + " "
//        + yCoordinate + " " + currDirectionNextX + " " + currDirectionNextY);

        //update motion info
        motionInArcade.updateMotionInfo(getMotionInfo());

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
        //check if in decision region
        if(motionInArcade.inDecisionRegion()) {
            //System.out.println("in region");
            //we need to take action

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
}
