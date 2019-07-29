package com.example.pacman;

import android.graphics.Bitmap;

import java.util.ArrayList;

/*
id: 0 red   chase
    1 pink  chaseFront
    2 blue  chasePredict
    3 yellow    chaseSearch
 */

public class Ghost extends MovingObject {
    public int id;
    public GhostBehaviour ghostBehaviour;
    public GameObjectTimer gameObjectTimer;
    private static TwoTuple door = new TwoTuple(11,13);
    private static TwoTuple RebornPos = new TwoTuple(14,14);
    private ArrayList<GhostBehaviour> chaseBehaviourList;
    private ArrayList<GhostBehaviour> scatterBehaviourList;

    public void updateGhostBehavior(boolean powerPelletEaten, boolean collision) {
        final GhostBehaviour currBehaviour = this.ghostBehaviour;
        final boolean isChasing = this.chaseBehaviourList.contains(currBehaviour);
        final boolean isScatter = this.scatterBehaviourList.contains(currBehaviour);
        final boolean timeUp = gameObjectTimer.timeUp;
        final TwoTuple currPos = this.motionInfo.posInArcade;

        if (currBehaviour instanceof GhostStationaryBehaviour && timeUp) {

            this.ghostBehaviour = new GhostExitBehaviour();
            return;
        }

        if (currBehaviour instanceof GhostExitBehaviour && currPos == door) {

            switch (id) {
                case 0:
                    this.ghostBehaviour = new GhostChaseBehaviour();
                    break;
                case 1:
                    this.ghostBehaviour = new GhostChaseFrontBehaviour();
                    break;
                case 2:
                    this.ghostBehaviour = new GhostPredictAndChaseBehaviour();
                    break;
                case 3:
                    this.ghostBehaviour = new GhostSearchAndChaseBehaviour();
                    break;
            }

            this.gameObjectTimer = new GameObjectTimer(GameObjectTimer.chaseTime);
            return;
        }

        if (isChasing && powerPelletEaten) {
            this.ghostBehaviour = new GhostEscapeBehaviour();
            this.gameObjectTimer = new GameObjectTimer(GameObjectTimer.powerUp);
            return;
        }

        if (isChasing && timeUp) {

            switch (id) {
                case 0:
                    this.ghostBehaviour = new GhostScatterLeftTop();
                    break;
                case 1:
                    this.ghostBehaviour = new GhostScatterRightTop();
                    break;
                case 2:
                    this.ghostBehaviour = new GhostScatterLeftBottom();
                    break;
                case 3:
                    this.ghostBehaviour = new GhostScatterRightBottom();
                    break;
            }

            this.gameObjectTimer = new GameObjectTimer(GameObjectTimer.scatterTime);
            return;
        }

        if (isScatter && powerPelletEaten) {
            this.ghostBehaviour = new GhostEscapeBehaviour();
            this.gameObjectTimer = new GameObjectTimer(GameObjectTimer.powerUp);
            return;
        }

        if (isScatter && timeUp) {

            switch (id) {
                case 0:
                    this.ghostBehaviour = new GhostChaseBehaviour();
                    break;
                case 1:
                    this.ghostBehaviour = new GhostChaseFrontBehaviour();
                    break;
                case 2:
                    this.ghostBehaviour = new GhostPredictAndChaseBehaviour();
                    break;
                case 3:
                    this.ghostBehaviour = new GhostSearchAndChaseBehaviour();
                    break;
            }

            this.gameObjectTimer = new GameObjectTimer(GameObjectTimer.chaseTime);
            return;
        }

        if (currBehaviour instanceof GhostEscapeBehaviour && timeUp) {

            switch (id) {
                case 0:
                    this.ghostBehaviour = new GhostChaseBehaviour();
                    break;
                case 1:
                    this.ghostBehaviour = new GhostChaseFrontBehaviour();
                    break;
                case 2:
                    this.ghostBehaviour = new GhostPredictAndChaseBehaviour();
                    break;
                case 3:
                    this.ghostBehaviour = new GhostSearchAndChaseBehaviour();
                    break;
            }

            this.gameObjectTimer = new GameObjectTimer(GameObjectTimer.chaseTime);
            return;
        }

        if (currBehaviour instanceof GhostEscapeBehaviour && collision) {
            this.ghostBehaviour = new GhostKilledBehaviour();

            return;
        }

        if(currBehaviour instanceof GhostKilledBehaviour && currPos == door){
            this.ghostBehaviour = new GhostEnterBehaviour();

            return;
        }

        if(currBehaviour instanceof GhostEnterBehaviour && currPos == RebornPos){

            this.ghostBehaviour = new GhostExitBehaviour();

            return;
        }
    }

    //Constructor
    public Ghost(final int id, final MotionInfo motionInfo, final ArrayList<Bitmap> viewList,
                 GhostBehaviour ghostBehaviour, long countDownTime) {
        super(motionInfo, viewList);

        this.id = id;
        this.ghostBehaviour = ghostBehaviour;

        this.gameObjectTimer = new GameObjectTimer(countDownTime);

        this.chaseBehaviourList = new ArrayList<>();
        chaseBehaviourList.add(new GhostChaseBehaviour());
        chaseBehaviourList.add(new GhostChaseFrontBehaviour());
        chaseBehaviourList.add(new GhostPredictAndChaseBehaviour());
        chaseBehaviourList.add(new GhostSearchAndChaseBehaviour());

        this.scatterBehaviourList = new ArrayList<>();
        scatterBehaviourList.add(new GhostScatterLeftTop());
        scatterBehaviourList.add(new GhostScatterRightTop());
        scatterBehaviourList.add(new GhostScatterLeftBottom());
        scatterBehaviourList.add(new GhostScatterRightBottom());
    }
}
