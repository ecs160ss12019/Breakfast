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

    //pixel based, block width=height
    int blockDimension;

    //coordinate
    private ArcadeAnalyzer arcadeAnalyzer;
    // private float speed;

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

    //did the pacman move?
    private boolean moved;

    @Override
    public void draw(Canvas canvas) {
        //TwoTuple screenPos = arcade.mapScreen(posInArcade);
//        canvas.drawBitmap(pacmanViewList.get(currDirection),
//                posInScreen.first() - (bitmapWidth/2), posInScreen.second() - (bitmapHeight/2), null);
        canvas.drawBitmap(pacmanViewList.get(currDirection),
                posInScreen.first() - (bitmapWidth/2), posInScreen.second() - (bitmapHeight/2), null);
    }

    public void updateLocation(long fps) {
        /*
        We cannot update when the fps is -1,
        otherwise there will be an overflow
        in speed/fps.
         */
        if (fps == -1 || fps == 0) {
            return;
        }

        /***************************************/
//        //Method 3
//        int mathematicalMove = mathematicalMoveDistance(fps);
//
//        if (nextDirection != currDirection && nextDirection != -1) {
//            //try new direction
//            boolean allowsTurn = arcadeAnalyzer.allowsToGo(posInArcade, nextDirection);
//
//            if (allowsTurn) {
//                //Turn and go
//                //posInArcade = movedTo(mathematicalMove, nextDirection);
//                movedTo(mathematicalMove, nextDirection);
//                currDirection = nextDirection;
//                return;
//            }
//        }
//
//        //Either not able to turn or not desired to turn
//        boolean allowsMove = arcadeAnalyzer.allowsToGo(posInArcade, currDirection);
//        if (allowsMove) {
//            //move and go
//            //posInArcade = movedTo(mathematicalMove, currDirection);
//            movedTo(mathematicalMove, currDirection);
//            return;
//        }
//
//        //else no move, stay there
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
        int gap = mathematicalMove;
        posInScreen = arcade.mapScreen(posInArcade);

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
            if (!allowsMove) {
//                System.out.println("Case 0");
//                System.out.println("Arcade Pos: " + " ROW: " + posInArcade.x + " " + " COL: " + posInArcade.y);
//                System.out.println("Screen Pos: " + " X: " + posInScreen.x + " " + " Y: " + posInScreen.y);
//                System.out.println(" ");
//                System.out.println(" ");
                //meets obstacle
                return;
            }

            if (gap <= 0) {
//                System.out.println("Case 1");
//                System.out.println("Arcade Pos: " + " ROW: " + posInArcade.x + " " + " COL: " + posInArcade.y);
//                System.out.println("Screen Pos: " + " X: " + posInScreen.x + " " + " Y: " + posInScreen.y);
//                System.out.println(" ");
//                System.out.println(" ");
                return;
            }

            //gap < 1/2 dimension, no change in arcade, change in screen
            //return after done
            if(gap < blockDimension / 2) {
//                System.out.println("Case 2");
//                System.out.println("Arcade Pos: " + " ROW: " + posInArcade.x + " " + " COL: " + posInArcade.y);
//                System.out.println("Screen Pos: " + " X: " + posInScreen.x + " " + " Y: " + posInScreen.y);
//                System.out.println(" ");
//                System.out.println(" ");
                //no need to move one more
                //posInArcade = TwoTuple.moveTo(posInArcade, movingDirection);
                if (arcadeAnalyzer.allowsToGo(posInArcade, movingDirection)) {
                    posInScreen = TwoTuple.addPixelGap(posInScreen, movingDirection, gap);
                }

                return;
            }

            //gap < dimension, gap >= 1/2 dimension, change in arcade, change in screen
            //return after done
            if(gap < blockDimension) {
//                System.out.println("Case 3");
//                System.out.println("Arcade Pos: " + " ROW: " + posInArcade.x + " " + " COL: " + posInArcade.y);
//                System.out.println("Screen Pos: " + " X: " + posInScreen.x + " " + " Y: " + posInScreen.y);
//                System.out.println(" ");
//                System.out.println(" ");
                //move one more
                posInArcade = TwoTuple.moveTo(posInArcade, movingDirection);
                if (arcadeAnalyzer.allowsToGo(posInArcade, movingDirection)) {
                    posInScreen = TwoTuple.addPixelGap(posInScreen, movingDirection, gap);
                } else {
                    posInScreen = arcade.mapScreen(posInArcade);
                }
                return;
            }

            //gap > dimension, change in arcade, change in screen
            //continue after done
//            System.out.println("Case 4");
//            System.out.println("Gap: " + gap);
//            System.out.println("Arcade Pos: " + " ROW: " + posInArcade.x + " " + " COL: " + posInArcade.y);
//            System.out.println("Screen Pos: " + " X: " + posInScreen.x + " " + " Y: " + posInScreen.y);
//            System.out.println(" ");
//            System.out.println(" ");
            gap -= blockDimension;
            posInArcade = TwoTuple.moveTo(posInArcade, movingDirection);
            allowsMove = arcadeAnalyzer.allowsToGo(posInArcade, movingDirection);
            posInScreen = arcade.mapScreen(posInArcade);
        }
    }

    //Constructor2 : this is for method 3
    public Pacman(Context context, TwoTuple screenResolution, Arcade arcade, TwoTuple posInArcade,
                 ArcadeAnalyzer arcadeAnalyzer, float speed, CollisionSubject collision) {
        super(screenResolution, speed, collision);
        this.context = context;
        this.arcade = arcade;
        this.posInArcade = posInArcade;
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

        this.arcadeAnalyzer = arcadeAnalyzer;
    }

    //Constructor: this is for method 2
    public Pacman(Context context, TwoTuple screenResolution, TwoTuple posInArcade, Pair<Integer, Integer> optimalSize,
                  Arcade arcade, float speed, Collision collision) {
        super(screenResolution, speed, collision);
        this.arcade = arcade;
        this.posInArcade = posInArcade;
        setPosition(arcade.getPacmanPosition_pix());
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

