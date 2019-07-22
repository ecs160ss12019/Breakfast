package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Pair;

import java.util.ArrayList;

public class Pacman extends Runner implements GameObject{
    /*
     * create Pacman here
     */

    //pixel based, this is used for display purpose
    private TwoTuple posInScreen;
    private int frameCounter = 0;
    private final int frameCounterMax = 1;

    //pixel based, block width=height
    int blockDimension;

    //coordinate
    private ArcadeAnalyzer arcadeAnalyzer;
    private Arcade arcade;
    private float speed;

    //context of the game, used access Resource ptr
    private Context context;

    /*
    img for the pacman
    Note that this is the combination of pacman
    img in all directions
     */
    private ArrayList<Bitmap> pacmanViewList;

    /*
    The img in the source folder is a collection of
    pacman views. Up, Right, Left, Down. We need to
    cut them into a list of 4 views. We take the
    original collection file as a matrix of
    numRow * numCol
     */

    private int numRow;
    private int numCol;

    private MotionInArcade motionInArcade;

    //did the pacman move?
    private boolean moved;

    @Override
    public void draw(Canvas canvas) {
        //TwoTuple screenPos = arcade.mapScreen(posInArcade);
        canvas.drawBitmap(pacmanViewList.get(currDirection),
                posInScreen.first() - (bitmapWidth/2), posInScreen.second() - (bitmapHeight/2), null);
    }

//    @Override
//    public void updateStatus(long fps){
//
//    }

    public void updateMovementStatus(int inputDirection, long fps) {

        /*
        We want to know where is the player heading,
        so we can update the currDirection of pacman
         */

        /*
        int roundedX = (int) x;
        int roundedY = (int) y;
        int diffX = (int)this.x - roundedX;
        int diffY = (int)this.y - roundedY;
        moved = false;


        We are not moving at all, we
        do not need to change currDirection.

        if(!(diffX == 0 && diffY == 0)) {
            moved = true;
            int absDiffX = Math.abs(diffX);
            int absDiffY = Math.abs(diffY);


            If change in X axis is greater than that in Y,
            the pacman is either heading left or right.
            Otherwise, the pacman is either heading up or down

            if (absDiffX > absDiffY) {
                //if diffX negative, moving left. Otherwise, right.
                if (diffX > 0) {
                    this.currDirection = LEFT; //left is the 0's bitmap in pacmanViewList
                } else {
                    this.currDirection = RIGHT; //right is the 1's bitmap in pacmanViewList
                }
            } else {
                //if diffY negative, moving down. Otherwise, up.
                if (diffY > 0) {
                    this.currDirection = UP; //up is the 0's bitmap in pacmanViewList
                } else {
                    this.currDirection = DOWN; //down is the 1's bitmap in pacmanViewList
                }
            }
        }
        */

        /*
        We are not using buttons thus there is
        no need to calculate the currDirection.

        Later we will align Pacman currDirection with
        button currDirection
         */

        /*
        The user wants to change direction if
        there is a touch on the NavigationButton
        and input Direction != -1.

        There is no need to update nextDirection if
        input is -1.
         */

        switch (inputDirection) {
            case -1:
                //nextDirection = -1;
                break;
            case 0:
                nextDirection = LEFT;
                break;
            case 1:
                nextDirection = RIGHT;
                break;
            case 2:
                nextDirection = UP;
                break;
            case 3:
                nextDirection = DOWN;
                break;
        }

        if (frameCounter == frameCounterMax) {
            updateLocation(fps);
            frameCounter = 0;
            return;
        }
        frameCounter++;
    }

    private void updateLocation(long fps) {
        /*
        We cannot update when the fps is -1,
        otherwise there will be an overflow
        in speed/fps.
         */
        if (fps == -1 || fps == 0) {
            return;
        }

        /*
        1. We use a pixel based algorithm to control motion
           This is a working solution but may not be the most
           optimal solution. We now disable it and work on
           method 2.
         */
        /*
        CollisionDetector collisionDetector = new CollisionDetector();

        int nextX = 0;
        int nextY = 0;
        */

        /*
        if nextDirection is not -1, try to
        move in that direction.
        If it works, update currDirection,
        return.
         */

        /*
        We first do the unit update, then check collision.
        We do need to keep the previous location so that
        if there is a collision, we prevent it from moving and
        roll back.

        This piece of code is working but need
        to be rewrite. It is now too crowded and
        ugly.
        */
         /*
        if (nextDirection != -1) {
            Pair<Integer, Integer> next = move(nextDirection, fps);
            nextX = next.first;
            nextY = next.second;

            //Check collision
            ArrayList<Obstacle> obstacles =arcade.getObstacleList(nextX, nextY);
            Obstacle pacmanReference = new Obstacle(nextX, nextY,
                    (int)(bitmapWidth * 0.8), (int)(bitmapHeight * 0.8));

            boolean collision = collisionDetector.collisionExist(pacmanReference, obstacles);
            if(!collision) {
                //there is no collision, update and return
                set(nextX, nextY);
                currDirection = nextDirection;
                return;
            }
        }
        */

        /*
        Either there is no new user input direction,
        or that direction does not work.
        Try moving in current direction, if it do
        not work as well, stay in current position
         */
        /*
        Pair<Integer, Integer> next = move(currDirection, fps);
        nextX = next.first;
        nextY = next.second;

        //Check collision
        ArrayList<Obstacle> obstacles = arcade.getObstacleList(nextX, nextY);
        Obstacle pacmanReference = new Obstacle(nextX, nextY,
                (int)(bitmapWidth * 0.8), (int)(bitmapHeight * 0.8));

        boolean collision = collisionDetector.collisionExist(pacmanReference, obstacles);
        if(!collision) {
            //there is no collision, update and return
            set(nextX, nextY);
        }
        */

        /*
        2.  We now attach the pacman to the arcade
            block at its position. We use arcade block
            to determine motion, instead of pixel.

            We only attempt to attach the position to arcade
            block when the it's necessary to do so.
            We set a condition: if next unit move will cross the
            center of a block, we do the attach thing. Otherwise,
            current motion should not be disturbed.
         */

        //next move in current direction
//        TwoTuple next = move(currDirection, fps);
//        currDirectionNextPosition = next;
//        // next move in next direction
//        nextDirectionNextPosition = move(nextDirection, fps);
//
//        System.out.println("Pacman update: " + this.position.x + " " + this.position.y + " " + currDirectionNextPosition.x + " " + currDirectionNextPosition.y);
//
//        //update motion info
//        motionInArcade.updateMotionInfo(getMotionInfo());
//
//        if (nextDirection != currDirection) {
//            //System.out.println("diff dir");
//            //We need to check user's desired direction
//            NextMotionInfo info1 = motionInArcade.isValidMotion(nextDirection);
//            if (info1.isValid()) {
//                //System.out.println("Valid Turn");
//                //we can change direction.
//                setPosition(info1.getPos());
//                currDirection = nextDirection;
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
//            NextMotionInfo info2 = motionInArcade.isValidMotion(currDirection);
//            if (!info2.isValid()) {
//                //System.out.println("Curr direction invalid");
//                //Now we must remain at current position
//                setPosition(info2.getPos());
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
//                new TwoTuple(currDirectionNextX, currDirectionNextY), nextDirection);
//
//        if (checkNextMoveInBound.second){
//            //System.out.println("No disturb");
//            //We do not need to disturb current motion
//            setCenter(currDirectionNextX, currDirectionNextY);
//            return;
//        }
//        System.out.println("Bad Fps: " + fps + "  gap: " + speed / fps +
//                "  prev: " + x + " " + y + "  next: " + currDirectionNextX + " " + currDirectionNextY);
//        //setCenter(checkNextMoveInBound.first.first(), checkNextMoveInBound.first.second());
//        setCenter(x, y);

        /***************************************/
        //New Method
        int mathematicalMove = mathematicalMoveDistance(fps);

        if (nextDirection != currDirection && nextDirection != -1) {
            //try new direction
            boolean allowsTurn = arcadeAnalyzer.allowsToGo(posInArcade, nextDirection);

            if (allowsTurn) {
                //Turn and go
                //movedTo(mathematicalMove, nextDirection);
                posInArcade = TwoTuple.moveTo(posInArcade, nextDirection);
                posInScreen = arcade.mapScreen(posInArcade);
                pixelGap = 0;
                currDirection = nextDirection;
                return;
            }
        }

        //Either not able to turn or not desired to turn
        boolean allowsMove = arcadeAnalyzer.allowsToGo(posInArcade, currDirection);
        if (allowsMove) {
            //move and go
            //posInArcade = movedTo(mathematicalMove, currDirection);
            //movedTo(mathematicalMove, currDirection);
            posInArcade = TwoTuple.moveTo(posInArcade, currDirection);
            posInScreen = arcade.mapScreen(posInArcade);
            return;
        }

        //else no move, stay there
    }

    //mathematical movement distance
    private int mathematicalMoveDistance(long fps) {
        return (int)(speed / fps);
    }

//    //move as far as possible
//    private TwoTuple movedTo(int mathematicalMove, int movingDirection) {
//        int movedDistance = 0;
//        TwoTuple currPos = posInArcade;
//
////        System.out.println("Starting to move from: " + posInArcade.first() + " " + posInArcade.second());
//        boolean allowsMove = arcadeAnalyzer.allowsToGo(currPos, movingDirection);
//        while (movedDistance <= mathematicalMove && allowsMove) {
//            movedDistance += arcadeAnalyzer.blockDimension;
//            currPos = TwoTuple.moveTo(currPos, movingDirection);
//
////            System.out.println("moved distance: " + movedDistance);
////            System.out.println("Moved to: " + currPos.first() + " " + currPos.second());
//        }
//
////        System.out.println("!!!!!");
////        System.out.println("Finished moving to: " + currPos.first() + " " + currPos.second());
//        return currPos;
//    }


    //move as far as possible
    private void movedTo(int mathematicalMove, int movingDirection) {
        int gap = mathematicalMove + pixelGap;

//        System.out.println("Starting to move from: " + posInArcade.first() + " " + posInArcade.second());
        boolean allowsMove = arcadeAnalyzer.allowsToGo(posInArcade, movingDirection);
//        while (movedDistance <= mathematicalMove && allowsMove) {
//            movedDistance += arcadeAnalyzer.blockDimension;
//            currPos = TwoTuple.moveTo(currPos, movingDirection);
//
////            System.out.println("moved distance: " + movedDistance);
////            System.out.println("Moved to: " + currPos.first() + " " + currPos.second());
//        }

//        System.out.println("!!!!!");
//        System.out.println("Finished moving to: " + currPos.first() + " " + currPos.second());
        while (true) {
            if (!allowsMove || gap == 0) {
//                System.out.println("Arcade Pos: " + " ROW: " + posInArcade.x + " " + " COL: " + posInArcade.y);
//                System.out.println("Screen Pos: " + " X: " + posInScreen.x + " " + " Y: " + posInScreen.y);
//                System.out.println(" ");
//                System.out.println(" ");
                //meets obstacle
                posInScreen = arcade.mapScreen(posInArcade);
                pixelGap = 0;
                return;
            }

            //Now next block on this direction must be valid
            if (gap < 0) {
                System.out.println("Case 1");
//                System.out.println("Arcade Pos: " + " ROW: " + posInArcade.x + " " + " COL: " + posInArcade.y);
//                System.out.println("Screen Pos: " + " X: " + posInScreen.x + " " + " Y: " + posInScreen.y);
//                System.out.println(" ");
//                System.out.println(" ");

                //gap < block size
                gap = gap + blockDimension;

                posInArcade = TwoTuple.moveTo(posInArcade, currDirection);

                if (arcadeAnalyzer.allowsToGo(posInArcade, movingDirection)) {
                    pixelGap = gap - blockDimension;
                } else {
                    pixelGap = 0;
                }

                posInScreen = arcade.mapScreen(posInArcade);
                posInScreen = TwoTuple.addPixelGap(posInScreen, currDirection, pixelGap);
                return;
            }

            //gap < 1/2 dimension, no change in arcade, change in screen
//            //return after done
////            if(gap < blockDimension / 2) {
//////                System.out.println("Case 2");
//////                System.out.println("Arcade Pos: " + " ROW: " + posInArcade.x + " " + " COL: " + posInArcade.y);
//////                System.out.println("Screen Pos: " + " X: " + posInScreen.x + " " + " Y: " + posInScreen.y);
//////                System.out.println(" ");
//////                System.out.println(" ");
////                //no need to move one more
////                //posInArcade = TwoTuple.moveTo(posInArcade, movingDirection);
////                if (arcadeAnalyzer.allowsToGo(posInArcade, movingDirection)) {
////                    posInScreen = TwoTuple.addPixelGap(posInScreen, movingDirection, gap);
////                }
////
////                return;
////            }
//
//            //gap < dimension, gap >= 1/2 dimension, change in arcade, change in screen
//            //return after done
////            if(gap < blockDimension) {
//////                System.out.println("Case 3");
//////                System.out.println("Arcade Pos: " + " ROW: " + posInArcade.x + " " + " COL: " + posInArcade.y);
//////                System.out.println("Screen Pos: " + " X: " + posInScreen.x + " " + " Y: " + posInScreen.y);
//////                System.out.println(" ");
//////                System.out.println(" ");
////                //move one more
////                posInArcade = TwoTuple.moveTo(posInArcade, movingDirection);
////                if (arcadeAnalyzer.allowsToGo(posInArcade, movingDirection)) {
////                    posInScreen = TwoTuple.addPixelGap(posInScreen, movingDirection, gap);
////                } else {
////                    posInScreen = arcade.mapScreen(posInArcade);
////                }
////                return;
////            }
//
//            //gap > dimension, change in arcade, change in screen
//            //continue after done
////            System.out.println("Case 4");
////            System.out.println("Gap: " + gap);
////            System.out.println("Arcade Pos: " + " ROW: " + posInArcade.x + " " + " COL: " + posInArcade.y);
////            System.out.println("Screen Pos: " + " X: " + posInScreen.x + " " + " Y: " + posInScreen.y);
////            System.out.println(" ");
////            System.out.println(" ");
            gap -= blockDimension;
            posInArcade = TwoTuple.moveTo(posInArcade, movingDirection);
            allowsMove = arcadeAnalyzer.allowsToGo(posInArcade, movingDirection);
        }
    }


    //Constructor2
    public Pacman(Context context, TwoTuple screenResolution, Arcade arcade, TwoTuple posInArcade,
                 ArcadeAnalyzer arcadeAnalyzer, float speed, CollisionSubject collision) {
        super(screenResolution, speed, collision);
        this.context = context;
        this.mScreen = screenResolution;
        this.arcade = arcade;
        this.posInArcade = posInArcade;
        this.pixelGap = 0;
        this.posInArcadeInit = posInArcade;
        this.posInScreen = arcade.mapScreen(posInArcade);
        this.blockDimension = arcadeAnalyzer.blockDimension;
        this.currDirection = UP;
        this.nextDirection = -1;

        //currently, the collection is 2*2 with 4 views in total
        numRow = 2;
        numCol = 2;

        //load pacman img from resource
        Bitmap pacmanCollectionView = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman);

        /*
        resize to fit screen and
        split the collection into small bitmaps (Left, Right, Up, Down)
        */
        BitmapDivider divider = new BitmapDivider(pacmanCollectionView);
        ArrayList<Bitmap> unsizedPacmanViewList = divider.split(numRow, numCol);

        pacmanViewList = new ArrayList<>();
        for (int i = 0; i < unsizedPacmanViewList.size(); i++) {

            //FIXME Pacman size?
            /*
            Bitmap bitmap = Bitmap.createScaledBitmap(unsizedPacmanViewList.get(i),
                    optimalSize.first, optimalSize.second, true);
            */
            Bitmap bitmap = Bitmap.createScaledBitmap(unsizedPacmanViewList.get(i),
                    screenResolution.y / 15, screenResolution.y/15, true);
            pacmanViewList.add(bitmap);
        }

        /*
        initialize the size of bitmap after split
         */
        bitmapWidth = pacmanViewList.get(0).getWidth();
        bitmapHeight = pacmanViewList.get(0).getHeight();

        // Configure the speed of the Pacman
        // This code means the Pacman can cover the width of the screen in 8 second
        this.speed = speed;
        this.arcadeAnalyzer = arcadeAnalyzer;
    }

    //Constructor
    public Pacman(Context context, TwoTuple screenResolution, TwoTuple posInArcade, Pair<Integer, Integer> optimalSize,
                  Arcade arcade, float speed, Collision collision) {
        super(screenResolution, speed, collision);
        this.posInArcade = posInArcade;
        //setPosition(arcade.getPacmanPosition_pix());
        this.context = context;
        mScreen = screenResolution;
        this.currDirection = RIGHT;
        this.nextDirection = -1;

        //currently, the collection is 2*2 with 4 views in total
        numRow = 2;
        numCol = 2;

        //load pacman img from resource
        Bitmap pacmanCollectionView = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman);

        /*
        resize to fit screen and
        split the collection into small bitmaps (Left, Right, Up, Down)
        */
        BitmapDivider divider = new BitmapDivider(pacmanCollectionView);
        ArrayList<Bitmap> unsizedPacmanViewList = divider.split(numRow, numCol);

        pacmanViewList = new ArrayList<>();
        for (int i = 0; i < unsizedPacmanViewList.size(); i++) {

            //FIXME Pacman size?
            /*
            Bitmap bitmap = Bitmap.createScaledBitmap(unsizedPacmanViewList.get(i),
                    optimalSize.first, optimalSize.second, true);
            */
            Bitmap bitmap = Bitmap.createScaledBitmap(unsizedPacmanViewList.get(i),
                    screenResolution.y / 15, screenResolution.y/15, true);
            pacmanViewList.add(bitmap);
        }

        /*
        initialize the size of bitmap after split
         */
        bitmapWidth = pacmanViewList.get(0).getWidth();
        bitmapHeight = pacmanViewList.get(0).getHeight();

        // Configure the speed of the Pacman
        // This code means the Pacman can cover the width of the screen in 8 second
        this.speed = speed;

        motionInArcade = new MotionInArcade(arcade);
    }

    public int getCurrentX(){return posInArcade.x;}

    public int getCurrentY(){return posInArcade.y;}



    public void reBorn() {
        this.posInArcade = this.posInArcadeInit;
        this.setDead(false);
    }
}

