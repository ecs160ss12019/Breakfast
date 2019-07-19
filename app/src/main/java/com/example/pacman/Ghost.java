package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Random;

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

        motionInArcade = new MotionInArcade(arcade);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(ghostView, this.position.x-bitmapWidth/2, this.position.y-bitmapHeight/2, null);
    }

    /*
    We use this func to calculate the after move location in a direction,
    no matter the direction is valid or not.
     */
    private TwoTuple move(int direction, long fps) {
        int nextX = this.position.x;
        int nextY = this.position.y;

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
        if (nextX + bitmapWidth / 2 > mScreen.x) {
            nextX = mScreen.x - bitmapWidth / 2;
        }
        if (nextY - bitmapHeight / 2 < 0) {
            nextY = bitmapHeight / 2;
        }
        if (nextY + bitmapHeight / 2 > mScreen.y) {
            nextY = mScreen.x - bitmapHeight / 2;
        }

        return new TwoTuple(nextX, nextY);
    }

    @Override
    public void updateStatus(long fps){

    }

    @Override
    public int getCenterX() { return this.position.x; }

    @Override
    public int getCenterY() {
        return this.position.y;
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
        motion.add(currDirectionNextPosition.x);
        motion.add(currDirectionNextPosition.y);
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
        currDirectionNextPosition = next;

        System.out.println("Ghost update: " + this.position.x + " " + this.position.y + " " + currDirectionNextPosition.x + " " + currDirectionNextPosition.y);

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
                System.out.println("Curr direction invalid");
                //Now we must remain at current position
                setPosition(info2.getPos());
                needToChangeDir = true;
                return;
            }
        }

        System.out.println("No disturb");
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
