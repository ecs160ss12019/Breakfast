package com.example.pacman;

import java.util.ArrayList;

public class Collision implements CollisionSubject {

    final int LEFT = 0;
    final int RIGHT = 1;
    final int UP = 2;
    final int DOWN = 3;

    private ArrayList<CollisionObserver> observers;
    private ArrayList<Runner> runners;
    private Pacman pacman;
    private PelletList pellets;
    private Arcade arcade;

    private int numRow;
    private int numCol;

    public Collision(Arcade arcade) {
        observers = new ArrayList<CollisionObserver>();
        runners = new ArrayList<Runner>();
        this.arcade = arcade;
        this.numRow = arcade.getNumRow();
        this.numCol = arcade.getNumCol();
    }

    @Override
    public void registerObserver(CollisionObserver observer) {
        observers.add(observer);
        if(observer instanceof  Runner) runners.add((Runner) observer);
        if(observer instanceof Pacman) pacman = (Pacman) observer;
        if(observer instanceof PelletList) pellets = (PelletList) observer;
    }

    @Override
    public void removeObserver(CollisionObserver observer) {

    }

    @Override
    public void notifyObservers() {
        for(CollisionObserver observer : observers) {
            if (observer instanceof PelletList) {
                // (PelletList)observer.update(pacman.blockRunThrough);
            }

        }
        pellets.udpate(pacman.blockRunThrough);
    }

    // record the runners position before each frame
    public void recordRunnersPosition() {
        // System.out.println("The number of runners: " + runners.size());
        for (Runner runner : runners) {
            // System.out.println("The type of runner: " + runner.getClass());
            runner.blockPosStart = runner.posInArcade;
        }
    }

    // update runners position at the end of each frame
    public void updateRunnersPosition() {
        System.out.println("updateRunnersPosition");
        for (Runner runner : runners) {
            runner.blockPosEnd = runner.posInArcade;
        }
        updateBlocksRunnerWhenThrough();
    }

    private void updateBlocksRunnerWhenThrough() {
        for (Runner runner : runners) {
            runner.blockRunThrough = new ArrayList<TwoTuple>();
            int i = runner.blockPosStart.x;
            int j = runner.blockPosStart.y;
            switch (runner.currDirection) {
                case LEFT:
                    for (i = runner.blockPosStart.x; i >= runner.blockPosEnd.x; i--) {
                        j = runner.blockPosStart.y;
                        runner.blockRunThrough.add(new TwoTuple(i,j));
                    }
                    break;
                case RIGHT:
                    for (i = runner.blockPosStart.x; i <= runner.blockPosEnd.x; i++) {
                        j = runner.blockPosStart.y;
                        runner.blockRunThrough.add(new TwoTuple(i,j));
                    }
                    break;
                case UP:
                    i = runner.blockPosStart.x;
                    for (j = runner.blockPosStart.y; j >= runner.blockPosEnd.y; j--) {
                        runner.blockRunThrough.add(new TwoTuple(i,j));
                    }
                    break;
                case DOWN:
                    i = runner.blockPosStart.x;
                    for (j = runner.blockPosStart.y; j <= runner.blockPosEnd.y; j++) {
                        runner.blockRunThrough.add(new TwoTuple(i,j));
                    }
                    break;
            }
        }
    }

    // if the pacman went through  a black with Pellet, mark the pellet as dead (it won't show anymore)
//    public void updatePelletsStatus() {
//
//
//
//        for(TwoTuple block : pacman.blockRunThrough) {
//            for (PelletCell pellet : pellets) {
//                if(pellet.getPositionInArcade().equals(block)) {
//                    pellet.setDead(true);
//                }
//            }
//        }
//    }


}
