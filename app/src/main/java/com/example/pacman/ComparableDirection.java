package com.example.pacman;

public class ComparableDirection {
    private int direction;
    private TwoTuple sourcePos;
    private TwoTuple targetPos;

    public int getDirection() {
        return direction;
    }

    public int getDistance() {
        TwoTuple newPos = TwoTuple.moveTo(sourcePos, direction);
        double xDiffSquared = (newPos.x - targetPos.x) * (newPos.x - targetPos.x);
        double yDiffSquared = (newPos.y - targetPos.y) * (newPos.y - targetPos.y);
        return (int)(Math.sqrt(xDiffSquared + yDiffSquared));
    }

    public ComparableDirection(int direction, TwoTuple sourcePos, TwoTuple targetPos) {
        this.direction = direction;
        this.sourcePos = sourcePos;
        this.targetPos = targetPos;
    }
}
