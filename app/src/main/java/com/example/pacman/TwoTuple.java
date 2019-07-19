package com.example.pacman;

public class TwoTuple {
    public int x;
    public int y;

    //integer case
    public int first() {
        return this.x;
    }

    public int second() {
        return this.y;
    }

    public TwoTuple(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public TwoTuple(TwoTuple position) {
        this.x = position.x;
        this.y = position.y;
    }
}
