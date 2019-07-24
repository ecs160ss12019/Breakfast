package com.example.pacman;

import java.util.LinkedList;

public class DirectionQueue {
//    final static int LEFT = 0;
//    final static int RIGHT = 1;
//    final static int UP = 2;
//    final static int DOWN = 3;

    private LinkedList<Integer> nextDirectionsQueue;

    public void enqueue(int direction) {
        nextDirectionsQueue.addLast(direction);
    }

    public int dequeue() {
        return nextDirectionsQueue.getFirst();
    }

    public DirectionQueue() {
        nextDirectionsQueue = new LinkedList<>();
    }
}
