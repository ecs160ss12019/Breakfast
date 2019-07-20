package com.example.pacman;

public class TwoTuple {
    private int x;
    private int y;

    static int LEFT = 0;
    static int RIGHT = 1;
    static int UP = 2;
    static int DOWN = 3;

    //integer case
    public int first() {
        return this.x;
    }

    public int second() {
        return this.y;
    }

    public TwoTuple toLeft() {
        return new TwoTuple(x, y - 1);
    }

    public TwoTuple toRight() {
        return new TwoTuple(x, y + 1);
    }

    public TwoTuple toUp() {
        return new TwoTuple(x - 1, y);
    }

    public TwoTuple toDown() {
        return new TwoTuple(x + 1, y);
    }

    public static TwoTuple moveTo(TwoTuple currPos, int direction) {
        if (direction == LEFT) {
            return currPos.toLeft();
        }

        if (direction == RIGHT) {
            return currPos.toRight();
        }

        if (direction == UP) {
            return currPos.toUp();
        }

        if (direction == DOWN) {
            return currPos.toDown();
        }

        else {
            System.out.println("Two Tuple moving direction -1");
            return new TwoTuple(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }
    }

    public TwoTuple(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
