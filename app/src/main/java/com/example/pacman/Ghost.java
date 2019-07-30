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
    private final ArrayList<Bitmap> normalViewList;
    private final ArrayList<Bitmap> escapingViewList;
    private final ArrayList<Bitmap> killedViewList;

    public GhostBehaviour ghostBehaviour;
    public GameObjectTimer gameObjectTimer;
    private static TwoTuple door = new TwoTuple(11,13);
    private static TwoTuple RebornPos = new TwoTuple(14,14);
    private ArrayList<GhostBehaviour> chaseBehaviourList;
    private ArrayList<GhostBehaviour> scatterBehaviourList;
    private boolean ghostEscape;

    public boolean getIsGhostEscape() {
        return ghostEscape;
    }

    public void updateGhostBehavior(boolean powerPelletEaten, boolean collision) {
        final GhostBehaviour currBehaviour = this.ghostBehaviour;
        final boolean isChasing = this.ghostBehaviour instanceof GhostChaseBehaviourInterface;
        final boolean isScatter = this.ghostBehaviour instanceof GhostScatterBehaviourInterface;
        final boolean timeUp = gameObjectTimer.isTimeUp();

        final TwoTuple currPos = this.motionInfo.posInArcade;
        final boolean atDoor = currPos.x == door.x && currPos.y == door.y;
        final boolean atReborn = currPos.x == RebornPos.x && currPos.y == RebornPos.y;

        if (currBehaviour instanceof GhostStationaryBehaviour && timeUp) {
            this.ghostBehaviour = new GhostExitBehaviour();
            return;
        }

        if (currBehaviour instanceof GhostExitBehaviour && atDoor) {
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
            ghostEscape = true;
            this.ghostBehaviour = new GhostEscapeBehaviour();
            System.out.println("Changed to escape");
            this.gameObjectTimer = new GameObjectTimer(GameObjectTimer.powerUp);
            return;
        }

        if (isChasing && timeUp) {
            ghostEscape = true;
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
            ghostEscape = true;
            this.ghostBehaviour = new GhostEscapeBehaviour();
            this.gameObjectTimer = new GameObjectTimer(GameObjectTimer.powerUp);
            return;
        }

        if (isScatter && timeUp) {
            ghostEscape = false;
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
            ghostEscape = false;
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

        if(currBehaviour instanceof GhostKilledBehaviour && atDoor){
            this.ghostBehaviour = new GhostEnterBehaviour();
            return;
        }

        if(currBehaviour instanceof GhostEnterBehaviour && atReborn){
            this.ghostBehaviour = new GhostExitBehaviour();
            return;
        }
    }

    public void updateViewList() {
        if (this.ghostBehaviour instanceof GhostKilledBehaviour ||
                this.ghostBehaviour instanceof GhostEnterBehaviour) {
            this.viewList = killedViewList;
            return;
        }

        if (this.ghostBehaviour instanceof GhostEscapeBehaviour) {
            this.viewList = escapingViewList;
            return;
        }

        this.viewList = normalViewList;
    }

    //Constructor
    public Ghost(final int id, final MotionInfo motionInfo,
                 final ArrayList<Bitmap> normalViewList,
                 final ArrayList<Bitmap> escapingViewList,
                 final ArrayList<Bitmap> killedViewList,
                 GhostBehaviour ghostBehaviour, long countDownTime) {
        super(motionInfo, normalViewList);
        ghostEscape = false;
        this.normalViewList = normalViewList;
        this.escapingViewList = escapingViewList;
        this.killedViewList = killedViewList;

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
