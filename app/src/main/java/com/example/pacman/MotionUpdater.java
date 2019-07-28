package com.example.pacman;

public class MotionUpdater {
    private TwoTuple posInArcade;
    private TwoTuple posInScreen;
    private int pixelGap;
    private int currDirection;
    private int nextDirection;
    private float speed;

    private Arcade arcade;
    private ArcadeAnalyzer arcadeAnalyzer;
    private int blockDimension;

    private long fps;

    //Algorithm starts here
    private void updateLocation() {
        int mathematicalMove = mathematicalMoveDistance(speed, fps);
        if (mathematicalMove == 0) return;

        if (nextDirection != currDirection && nextDirection != -1) {
            //try new direction
            boolean allowsTurn = arcadeAnalyzer.allowsToGo(posInArcade, nextDirection);

            if (allowsTurn) {
                //Turn and go
                if (essentialCheck(mathematicalMove)) {
                    posInScreen = arcade.mapScreen(posInArcade);
                    pixelGap = 0;
                    currDirection = nextDirection;
                    return;
                }
            }
        }

        movedTo(mathematicalMove, currDirection);
    }

    //mathematical movement distance
    private int mathematicalMoveDistance(final float speed, final long fps) {
        return (int)(speed/fps);
    }

    //move as far as possible
    private void movedTo(int mathematicalMove, int movingDirection) {
        int gap = mathematicalMove + pixelGap;
        boolean allowsMove = arcadeAnalyzer.allowsToGo(posInArcade, movingDirection);

        while (true) {
            if (gap == 0) {
                posInScreen = arcade.mapScreen(posInArcade);
                pixelGap = 0;
                return;
            }

            if (!allowsMove) {
                //Collide to obstacle, but still attempt to move more
                //than a block long, int this case it must stay at center
                if (gap > 0) {
                    posInScreen = arcade.mapScreen(posInArcade);
                    pixelGap = 0;
                    return;
                }

                //obstacle ahead, gap subtracted to smaller than 0
                //Just close up, it is okay
                pixelGap = gap;

                posInScreen = arcade.mapScreen(posInArcade);
                posInScreen = TwoTuple.addPixelGap(posInScreen, currDirection, pixelGap);
                return;
            }

            //Now next block on this direction must be valid
            if (gap < 0) {
                gap = gap + blockDimension;
                pixelGap = gap - blockDimension;

                posInScreen = arcade.mapScreen(posInArcade);
                posInScreen = TwoTuple.addPixelGap(posInScreen, currDirection, pixelGap);
                return;
            }

            gap -= blockDimension;
            posInArcade = TwoTuple.moveTo(posInArcade, movingDirection);
            allowsMove = arcadeAnalyzer.allowsToGo(posInArcade, movingDirection);
        }
    }

    private boolean essentialCheck(int mathGap) {
        //Will it actually go pass the
        //center of the turning block
        //if we continue the previous motion?
        //To due with this, lets try to
        //add the gap on first!

        //The turning point on screen
        TwoTuple turningPoint = arcade.mapScreen(posInArcade);

        //get sign
        int Sign_x_prev = (int)Math.signum(posInScreen.x - turningPoint.x);
        int Sign_y_prev = (int)Math.signum(posInScreen.y - turningPoint.y);

        //try to add gap
        TwoTuple posPost = TwoTuple.addPixelGap(posInScreen, currDirection, mathGap);

        //update sign
        int Sign_x_post = (int)Math.signum(posPost.x - turningPoint.x);
        int Sign_y_post = (int)Math.signum(posPost.y - turningPoint.y);

        //We must advance to center!
        //Or we are on the center
        if (Sign_x_post != Sign_x_prev || Sign_y_post != Sign_y_prev ||
                (Sign_x_prev == 0 && Sign_y_prev == 0)) {
            return true;
        }
        return false;
    }

    public MotionInfo updateMotion() {
        updateLocation();
        return new MotionInfo(posInArcade, posInScreen, pixelGap,
                currDirection, nextDirection, speed);
    }

    public MotionUpdater(final MotionInfo motionInfo, final long fps,
                         final Arcade arcade, final ArcadeAnalyzer arcadeAnalyzer) {
        this.posInArcade = motionInfo.posInArcade;
        this.posInScreen = motionInfo.posInScreen;
        this.pixelGap = motionInfo.pixelGap;
        this.currDirection = motionInfo.currDirection;
        this.nextDirection = motionInfo.nextDirection;
        this.speed = motionInfo.speed;

        this.arcade = arcade;
        this.arcadeAnalyzer = arcadeAnalyzer;
        this.blockDimension = arcadeAnalyzer.blockDimension;

        this.fps = fps;
    }
}
