package com.example.pacman;

public class Runner {

    // These variables are public and final
    // They can be directly accessed by
    // the instance (in PacmanGame)
    // because they are part of the same
    // package but cannot be changed
    final int LEFT = 0;
    final int RIGHT = 1;
    final int UP = 2;
    final int DOWN = 3;

    //coordinate
    TwoTuple position;
    TwoTuple currDirectionNextPosition;
    TwoTuple mScreen;

    float speed;

    //set current position
    public void setPosition(TwoTuple position) {
        this.position = position;
    }
}
