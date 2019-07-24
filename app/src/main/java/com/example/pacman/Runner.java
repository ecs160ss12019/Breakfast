//package com.example.pacman;
//
//import android.graphics.Canvas;
//import android.util.Pair;
//
//import java.util.ArrayList;
//import java.util.Random;
//
//public class Runner implements GameObject, CollisionObserver {
//
//    /*
//    individual view height/width
//    this is crucial to centering the pacman
//    on the coordinates
//     */
//    int bitmapWidth;
//    int bitmapHeight;
//
//    // These variables are public and final
//    // They can be directly accessed by
//    // the instance (in PacmanGame)
//    // because they are part of the same
//    // package but cannot be changed
//    final int LEFT = 0;
//    final int RIGHT = 1;
//    final int UP = 2;
//    final int DOWN = 3;
//
//    //currDirection means the pacman is going up, down, left, or right
//    int currDirection;
//    /*
//    nextDirection means the user wants to head to this direction.
//    We need to verify if this direction is okay to goto.
//     */
//    int nextDirection;
//
//    // block
//    TwoTuple posInArcadeInit;
//    TwoTuple posInArcade;
//    int pixelGap;
//    TwoTuple blockPosStart; // the block which the runner start with in one frame, set the same as posInArcade at the beginning of the frame
//    TwoTuple blockPosEnd; // the block which the runner end with in one frame
//    ArrayList<TwoTuple> blockRunThrough; // the blocks which the runner run through in one frame
//
//    //coordinate
//    //pixel based, this is used for display purpose
//    TwoTuple posInScreen;
//    TwoTuple currDirectionNextPosition;
//    TwoTuple nextDirectionNextPosition;
//
//    TwoTuple mScreen;
//
//    float speed;
//    MotionInArcade motionInArcade;
//    Arcade arcade;
//
//    private CollisionSubject collision;
//    private boolean isDead;
//
//    boolean needToChangeDir =false;
//
//    public boolean isDead() {
//        return isDead;
//    }
//
//    public void setDead(boolean dead) {
//        isDead = dead;
//    }
//
//    public int getBitmapWidth() {
//        return bitmapWidth;
//    }
//
//    public int getBitmapHeight() {
//        return bitmapHeight;
//    }
//
//    //set current position
//    public void setPosition(TwoTuple position) {
//        this.posInScreen = position;
//    }
//
//
//    public int getCurrDirection() {
//        return currDirection;
//    }
//
//    public int getNextDirection() {
//        return nextDirection;
//    }
//
//    public Runner(TwoTuple screen, float speed, CollisionSubject collision) {
//        this.mScreen = screen;
//        // Configure the speed of the Game object
//        // This code means the Pacman can cover the width of the screen in X seconds
//        this.speed = speed;
//        this.collision = collision;
//        collision.registerObserver(this);
//    }
//
//    //getMotionInfo
//    /*
//    Array of 5
//    array[0] : currX
//    array[1] : currY
//    array[2] : currDirectionNextX
//    array[3] : currDirectionNextY
//    array[4] : currDirection
//
//    Note that currDirectionNextX means next x position if continue travelling
//    on currDirection (not in nextDirection), same for nextY.
//     */
//    public ArrayList<Integer> getMotionInfo() {
//        ArrayList<Integer> motion = new ArrayList<>();
//        motion.add(this.getPositionX());
//        motion.add(this.getPositionY());
//        motion.add(currDirectionNextPosition.x);
//        motion.add(currDirectionNextPosition.y);
//        motion.add(currDirection);
//        motion.add(nextDirection);
//        motion.add(nextDirectionNextPosition.x);
//        motion.add(nextDirectionNextPosition.y);
//        return motion;
//    }
//
//    /*
//    We use this func to calculate the after move location in a direction,
//    no matter the direction is valid or not.
//     */
//    TwoTuple move(int direction, long fps) {
//        int nextX = this.posInScreen.x;
//        int nextY = this.posInScreen.y;
//
//        System.out.println("direciton: " + direction + " speed: " + speed);
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
//        if (nextX + bitmapWidth / 2 > mScreen.x) {
//            nextX = mScreen.x - bitmapWidth / 2;
//        }
//        if (nextY - bitmapHeight / 2 < 0) {
//            nextY = bitmapHeight / 2;
//        }
//        if (nextY + bitmapHeight / 2 > mScreen.y) {
//            nextY = mScreen.y - bitmapHeight / 2;
//        }
//
//        return new TwoTuple(nextX, nextY);
//    }
//
//    public void updateMovementStatus(int inputDirection, long fps) {
//
//        /*
//        We want to know where is the player heading,
//        so we can update the currDirection of pacman
//         */
//
//        /*
//        int roundedX = (int) x;
//        int roundedY = (int) y;
//        int diffX = (int)this.x - roundedX;
//        int diffY = (int)this.y - roundedY;
//        moved = false;
//
//
//        We are not moving at all, we
//        do not need to change currDirection.
//
//        if(!(diffX == 0 && diffY == 0)) {
//            moved = true;
//            int absDiffX = Math.abs(diffX);
//            int absDiffY = Math.abs(diffY);
//
//
//            If change in X axis is greater than that in Y,
//            the pacman is either heading left or right.
//            Otherwise, the pacman is either heading up or down
//
//            if (absDiffX > absDiffY) {
//                //if diffX negative, moving left. Otherwise, right.
//                if (diffX > 0) {
//                    this.currDirection = LEFT; //left is the 0's bitmap in pacmanViewList
//                } else {
//                    this.currDirection = RIGHT; //right is the 1's bitmap in pacmanViewList
//                }
//            } else {
//                //if diffY negative, moving down. Otherwise, up.
//                if (diffY > 0) {
//                    this.currDirection = UP; //up is the 0's bitmap in pacmanViewList
//                } else {
//                    this.currDirection = DOWN; //down is the 1's bitmap in pacmanViewList
//                }
//            }
//        }
//        */
//
//        /*
//        We are not using buttons thus there is
//        no need to calculate the currDirection.
//
//        Later we will align Pacman currDirection with
//        button currDirection
//         */
//
//        /*
//        The user wants to change direction if
//        there is a touch on the NavigationButton
//        and input Direction != -1.
//
//        There is no need to update nextDirection if
//        input is -1.
//         */
//
//        if(this instanceof Ghost || this instanceof Cake) {
//            inputDirection = -1;
//            if(needToChangeDir == true) {
//                Random randomGenerator = new Random();
//                inputDirection = randomGenerator.nextInt(4);
//            }
//        }
//
//        switch (inputDirection) {
//            case -1:
//                //nextDirection = -1;
//                break;
//            case 0:
//                nextDirection = LEFT;
//                break;
//            case 1:
//                nextDirection = RIGHT;
//                break;
//            case 2:
//                nextDirection = UP;
//                break;
//            case 3:
//                nextDirection = DOWN;
//                break;
//        }
//
//        updatePositionByPixel(fps);
//    }
//
//    // Siqi developed two different algorithms
//
//    // first is pixel based
//    public void updatePositionByPixel(long fps) {
//
//        /*
//        1. We use a pixel based algorithm to control motion
//           This is a working solution but may not be the most
//           optimal solution. We now disable it and work on
//           method 2.
//         */
//        /*
//        CollisionDetector collisionDetector = new CollisionDetector();
//
//        int nextX = 0;
//        int nextY = 0;
//        */
//
//        /*
//        if nextDirection is not -1, try to
//        move in that direction.
//        If it works, update currDirection,
//        return.
//         */
//
//        /*
//        We first do the unit update, then check collision.
//        We do need to keep the previous location so that
//        if there is a collision, we prevent it from moving and
//        roll back.
//
//        This piece of code is working but need
//        to be rewrite. It is now too crowded and
//        ugly.
//        */
//         /*
//        if (nextDirection != -1) {
//            Pair<Integer, Integer> next = move(nextDirection, fps);
//            nextX = next.first;
//            nextY = next.second;
//
//            //Check collision
//            ArrayList<Obstacle> obstacles =arcade.getObstacleList(nextX, nextY);
//            Obstacle pacmanReference = new Obstacle(nextX, nextY,
//                    (int)(bitmapWidth * 0.8), (int)(bitmapHeight * 0.8));
//
//            boolean collision = collisionDetector.collisionExist(pacmanReference, obstacles);
//            if(!collision) {
//                //there is no collision, update and return
//                set(nextX, nextY);
//                currDirection = nextDirection;
//                return;
//            }
//        }
//        */
//
//        /*
//        Either there is no new user input direction,
//        or that direction does not work.
//        Try moving in current direction, if it do
//        not work as well, stay in current position
//         */
//        /*
//        Pair<Integer, Integer> next = move(currDirection, fps);
//        nextX = next.first;
//        nextY = next.second;
//
//        //Check collision
//        ArrayList<Obstacle> obstacles = arcade.getObstacleList(nextX, nextY);
//        Obstacle pacmanReference = new Obstacle(nextX, nextY,
//                (int)(bitmapWidth * 0.8), (int)(bitmapHeight * 0.8));
//
//        boolean collision = collisionDetector.collisionExist(pacmanReference, obstacles);
//        if(!collision) {
//            //there is no collision, update and return
//            set(nextX, nextY);
//        }
//        */
//
//        /*
//        2.  We now attach the pacman to the arcade
//            block at its position. We use arcade block
//            to determine motion, instead of pixel.
//
//            We only attempt to attach the position to arcade
//            block when the it's necessary to do so.
//            We set a condition: if next unit move will cross the
//            center of a block, we do the attach thing. Otherwise,
//            current motion should not be disturbed.
//         */
//
//        if (fps == -1 || fps == 0) {
//            return;
//        }
//
//        // next move in current direction
//        TwoTuple next = move(currDirection, fps);
//        currDirectionNextPosition = next;
//        // next move in next direction
//        nextDirectionNextPosition = move(nextDirection, fps);
//
//        System.out.println("Game Object update: " + this.posInScreen.x + " " + this.posInScreen.y + " " + currDirectionNextPosition.x + " " + currDirectionNextPosition.y);
//
//        //update motion info
//        motionInArcade.updateMotionInfo(getMotionInfo());
//
//        // we don't need to check if it's in decision region if we need to change direction
//        if (nextDirection != currDirection) {
//            //System.out.println("diff dir");
//            //We need to check user's desired direction
//            MotionInfo info1 = motionInArcade.isValidMotion(nextDirection);
//            if (info1.isValid()) {
//                //System.out.println("Valid Turn");
//                //we can change direction.
//                setPosition(info1.getPos());
//                currDirection = nextDirection;
//                this.posInArcade = arcade.mapBlock(info1.getPos(), currDirection);
//                if(this instanceof Ghost || this instanceof Cake) needToChangeDir = false;
//                return;
//            }
//        }
//        //check if in decision region
//        if (motionInArcade.inDecisionRegion()) {
//            //System.out.println("in region");
//            //we need to take action
//            /*
//            either user did not input direction
//            or user's desired input is invalid.
//            We check if we can continue on current direction
//             */
//            MotionInfo info2 = motionInArcade.isValidMotion(currDirection);
//            if (!info2.isValid()) {
//                //System.out.println("Curr direction invalid");
//                //Now we must remain at current position
//                setPosition(info2.getPos());
//                this.posInArcade = arcade.mapBlock(info2.getPos(), currDirection);
//                if(this instanceof Ghost || this instanceof Cake) needToChangeDir = true;
//                return;
//            }
//        }
//
//        /*
//        Now we can keep the original motion,
//        but we still need to know if the next move
//        is still on path.
//         */
//
//        Pair<TwoTuple, Boolean> checkNextMoveInBound = motionInArcade.mostDistantPathBlock(
//                new TwoTuple(currDirectionNextPosition), nextDirection);
//
//        if (checkNextMoveInBound.second){
//            //System.out.println("No disturb");
//            //We do not need to disturb current motion
//            setPosition(currDirectionNextPosition);
//            this.posInArcade = arcade.mapBlock(currDirectionNextPosition, currDirection);
//            if(this instanceof Ghost || this instanceof Cake) needToChangeDir = false;
//            return;
//        }
//        System.out.println("Bad Fps: " + fps + "  gap: " + speed / fps +
//                "  prev: " + this.posInScreen.x + " " + this.posInScreen.y + "  next: " + currDirectionNextPosition.x + " " + currDirectionNextPosition.y);
//        //setCenter(checkNextMoveInBound.first.first(), checkNextMoveInBound.first.second());
//        setPosition(this.posInScreen);
//        this.posInArcade = arcade.mapBlock(posInScreen, currDirection);
//        if(this instanceof Ghost || this instanceof Cake) needToChangeDir = true;
//    }
//
//    // second is Arcade based
//    public void updatePositionByBlock(long fps) {
//
//    }
//
//    @Override
//    public void draw(Canvas canvas) {
//
//    }
//
//    // route is the route went through by Pacman
//    @Override
//    public void update(ArrayList<TwoTuple> route) {
//
//    }
//}
