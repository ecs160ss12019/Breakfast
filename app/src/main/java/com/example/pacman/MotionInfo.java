package com.example.pacman;

//This class describes the crucial
//information of a moving object
public class MotionInfo implements Info {
    //coordinate based referencing to arcade
    //for control purpose
    public TwoTuple posInArcade;

    //pixel based referencing to screen
    //for drawing purpose
    public TwoTuple posInScreen;

    //pixel based, gap to nearest block
    public int pixelGap;

    public int currDirection;

    public int nextDirection;

    public float speed;

    @Override
    public int posInScreen_X() {
        return this.posInScreen.x;
    }

    @Override
    public int posInScreen_Y() {
        return this.posInScreen.y;
    }

    public MotionInfo() {
    }

    public MotionInfo(MotionInfo info) {
        this.posInArcade = info.posInArcade;
        this.posInScreen = info.posInScreen;
        this.pixelGap = info.pixelGap;
        this.currDirection = info.currDirection;
        this.nextDirection = info.nextDirection;
        this.speed = info.speed;
    }

    public MotionInfo(final TwoTuple posInArcade, final TwoTuple posInScreen,
                      final int pixelGap,
                      final int currDirection, final int nextDirection,
                      final float speed) {
        this.posInArcade = posInArcade;
        this.posInScreen = posInScreen;
        this.pixelGap = pixelGap;
        this.currDirection = currDirection;
        this.nextDirection = nextDirection;
        this.speed = speed;
    }
}
