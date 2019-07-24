package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.ArrayList;

public class Pacman extends Runner implements GameObject{
    /*
     * create Pacman here
     */

    private float pixelCounter = 0;

    //pixel based, block width=height
    // int blockDimension;

    //coordinate
    //private ArcadeAnalyzer arcadeAnalyzer;

    //private Arcade arcade;

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
        canvas.drawBitmap(pacmanViewList.get(currDirection),
                posInScreen.x - (bitmapWidth/2), posInScreen.y - (bitmapHeight/2), null);
    }

//    @Override
//    public void updateStatus(long fps){
//
//    }

    public void reBorn() {
        this.posInArcade = this.posInArcadeInit;
        this.setDead(false);
    }

    public void resetPacman(boolean toTheRight) {
        if(toTheRight) this.posInArcade.y = arcade.getNumCol()-1;
        else this.posInArcade.y = 0;
        this.posInScreen = arcade.mapScreen(this.posInArcade);
    }

//    public void changeArcade() {
//        if(this.posInArcade.y < 0 || this.posInArcade.y >= this.arcade.getNumCol()) {
//            // Pacman run out of the current arcade, we need to move to the new arcade
//
//        }
//    }

    private Pacman(Context context, TwoTuple screen, float speed, CollisionSubject collision) {
        super(screen, speed, collision);

        this.context = context;

        this.posInArcadeInit = posInArcade;
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
                    screen.y / 15, screen.y/15, true);
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
    }

    //Constructor1: set up pacman based on pixel
    public Pacman(Context context, TwoTuple screen, float speed, CollisionSubject collision, Arcade arcade) {
        this(context, screen, speed, collision);

        this.arcade = arcade;
        this.posInScreen = arcade.getPacmanPosition_pix();
        this.posInArcade = arcade.pacmanPosition;
        this.posInArcadeInit = posInArcade;

        motionInArcade = new MotionInArcade(arcade);
    }

    //Constructor2: set up pacman based on block
    public Pacman(Context context, TwoTuple screen, float speed, CollisionSubject collision, Arcade arcade, ArcadeAnalyzer arcadeAnalyzer) {
        this(context, screen, speed, collision);

        this.arcade = arcade;
        this.posInArcade = arcade.pacmanPosition;
        this.posInScreen = arcade.mapScreen(posInArcade); // calculate posInScreen based on posInArcade;
        this.posInArcadeInit = posInArcade;

        this.pixelGap = 0;
        this.arcadeAnalyzer = arcadeAnalyzer;
        this.blockDimension = arcadeAnalyzer.blockDimension;
    }
}

