package com.example.pacman;

public class TwoTuple {
    private int x;
    private int y;

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
}
