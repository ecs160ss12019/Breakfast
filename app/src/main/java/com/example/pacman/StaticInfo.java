package com.example.pacman;

//This class describes the crucial
//information of a moving object
public class StaticInfo implements Info {
    //coordinate based referencing to arcade
    //for control purpose
    public TwoTuple posInArcade;

    //pixel based referencing to screen
    //for drawing purpose
    public TwoTuple posInScreen;

    @Override
    public int posInScreen_X() {
        return this.posInScreen.x;
    }

    @Override
    public int posInScreen_Y() {
        return this.posInScreen.y;
    }

    public StaticInfo() {
    }

    public StaticInfo(final TwoTuple posInArcade, final TwoTuple posInScreen) {
        this.posInArcade = posInArcade;
        this.posInScreen = posInScreen;
    }
}
