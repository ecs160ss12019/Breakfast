package com.example.pacman;

import java.lang.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class GhostBehavior {
    private TwoTuple PacmanPos;
    private TwoTuple GhostPos;
    private int PacmanDir;
    private ArcadeAnalyzer arcadeAnalyzer;
    private int Direction;
    private boolean AllowToTurn = false;
    private TwoTuple CutoffPoints;

    final int LEFT = 0;
    final int RIGHT = 1;
    final int UP = 2;
    final int DOWN = 3;
    private int[] Direction = {LEFT, RIGHT, UP, DOWN};


    public GhostBehavior(TwoTuple PacmanPos, TwoTuple GhostPos, int PacmanDir, ArcadeAnalyzer AracadeAnalyzer) {
        this.PacmanPos = PacmanPos;
        this.GhostPos = GhostPos;
        this.PacmanDir = PacmanDir;
        this.arcadeAnalyzer = AracadeAnalyzer;
    }

    public TwoTuple FindCutoffpoint(){
        AllowToTurn = arcadeAnalyzer.allowsToGo(PacmanPos, PacmanDir);
        CutoffPoints = PacmanPos;
        if(AllowToTurn){
            switch(PacmanDir){
                case LEFT:
                    CutoffPoints.toLeft();
                    break;
                case RIGHT:
                    CutoffPoints.toRight();
                    break;
                case UP:
                    CutoffPoints.toUp();
                    break;
                case DOWN:
                    CutoffPoints.toDown();
                    break;
            }
        }
        else{
            switch(PacmanDir){
                case LEFT:
                    CutoffPoints.toRight();
                    break;
                case RIGHT:
                    CutoffPoints.toLeft();
                    break;
                case UP:
                    CutoffPoints.toDown();
                    break;
                case DOWN:
                    CutoffPoints.toUp();
                    break;
            }
        }
        return CutoffPoints;
    }

    public int chase(){
            Queue<Integer> DirectionQ = new LinkedList<>();
            int HorizontalGap = PacmanPos.y - this.GhostPos.y;
            int VerticalGap = PacmanPos.x - this.GhostPos.x;
            if (Math.abs(HorizontalGap) > Math.abs(VerticalGap)) {
                if (HorizontalGap > 0 && VerticalGap > 0) {
                    DirectionQ.add(RIGHT);
                    DirectionQ.add(UP);
                    DirectionQ.add(DOWN);
                    DirectionQ.add(LEFT);
                }
                else if(HorizontalGap > 0 && VerticalGap <= 0){
                    DirectionQ.add(RIGHT);
                    DirectionQ.add(DOWN);
                    DirectionQ.add(UP);
                    DirectionQ.add(LEFT);
                }
                else if(HorizontalGap <= 0 && VerticalGap > 0){
                    DirectionQ.add(LEFT);
                    DirectionQ.add(UP);
                    DirectionQ.add(DOWN);
                    DirectionQ.add(RIGHT);
                }
                else if (HorizontalGap <= 0 && VerticalGap <= 0){
                    DirectionQ.add(LEFT);
                    DirectionQ.add(DOWN);
                    DirectionQ.add(UP);
                    DirectionQ.add(RIGHT);
                }
            }
            else{
                if (VerticalGap > 0 && HorizontalGap > 0){
                    DirectionQ.add(UP);
                    DirectionQ.add(RIGHT);
                    DirectionQ.add(LEFT);
                    DirectionQ.add(DOWN);
                }
                else if(VerticalGap > 0 && HorizontalGap <= 0){
                    DirectionQ.add(UP);
                    DirectionQ.add(LEFT);
                    DirectionQ.add(RIGHT);
                    DirectionQ.add(DOWN);
                }
                else if (VerticalGap <= 0 && HorizontalGap > 0){
                    DirectionQ.add(DOWN);
                    DirectionQ.add(RIGHT);
                    DirectionQ.add(LEFT);
                    DirectionQ.add(UP);
                }
                else if (VerticalGap <= 0 && HorizontalGap <= 0){
                    DirectionQ.add(UP);
                    DirectionQ.add(RIGHT);
                    DirectionQ.add(LEFT);
                    DirectionQ.add(DOWN);
                }
            }
            while(!this.AllowToTurn){
                this.AllowToTurn = this.arcadeAnalyzer.allowsToGo(GhostPos, DirectionQ.peek());
                if(!this.AllowToTurn){
                    DirectionQ.remove();
                }
            }

            return DirectionQ.peek();
    }

    public int CutOff (){
        boolean AllowToTurn = false;
        Queue<Integer> DirectionQ = new LinkedList<>();
        int HorizontalGap = CutoffPoints.y - this.GhostPos.y;
        int VerticalGap = CutoffPoints.x - this.GhostPos.x;
        if (Math.abs(HorizontalGap) > Math.abs(VerticalGap)) {
            if (HorizontalGap > 0 && VerticalGap > 0) {
                DirectionQ.add(RIGHT);
                DirectionQ.add(UP);
                DirectionQ.add(DOWN);
                DirectionQ.add(LEFT);
            }
            else if(HorizontalGap > 0 && VerticalGap <= 0){
                DirectionQ.add(RIGHT);
                DirectionQ.add(DOWN);
                DirectionQ.add(UP);
                DirectionQ.add(LEFT);
            }
            else if(HorizontalGap <= 0 && VerticalGap > 0){
                DirectionQ.add(LEFT);
                DirectionQ.add(UP);
                DirectionQ.add(DOWN);
                DirectionQ.add(RIGHT);
            }
            else if (HorizontalGap <= 0 && VerticalGap <= 0){
                DirectionQ.add(LEFT);
                DirectionQ.add(DOWN);
                DirectionQ.add(UP);
                DirectionQ.add(RIGHT);
            }
        }
        else{
            if (VerticalGap > 0 && HorizontalGap > 0){
                DirectionQ.add(UP);
                DirectionQ.add(RIGHT);
                DirectionQ.add(LEFT);
                DirectionQ.add(DOWN);
            }
            else if(VerticalGap > 0 && HorizontalGap <= 0){
                DirectionQ.add(UP);
                DirectionQ.add(LEFT);
                DirectionQ.add(RIGHT);
                DirectionQ.add(DOWN);
            }
            else if (VerticalGap <= 0 && HorizontalGap > 0){
                DirectionQ.add(DOWN);
                DirectionQ.add(RIGHT);
                DirectionQ.add(LEFT);
                DirectionQ.add(UP);
            }
            else if (VerticalGap <= 0 && HorizontalGap <= 0){
                DirectionQ.add(UP);
                DirectionQ.add(RIGHT);
                DirectionQ.add(LEFT);
                DirectionQ.add(DOWN);
            }
        }
        while(!AllowToTurn){
            AllowToTurn = this.arcadeAnalyzer.allowsToGo(GhostPos, DirectionQ.peek());
            if(!AllowToTurn){
                DirectionQ.remove();
            }
        }

        return DirectionQ.peek();
    }

    public int RandomMove(){
        int inputDirection1 = -1;
        while(!AllowToTurn) {
            AllowToTurn = this.arcadeAnalyzer.allowsToGo(GhostPos, inputDirection1);
            Random randomGenerator = new Random();
            inputDirection1 = randomGenerator.nextInt(4);
        }
        switch (inputDirection1) {
            case -1:
                //nextDirection = -1;
                break;
            case 0:
                Direction = UP;
                break;
            case 1:
                Direction = DOWN;
                break;
            case 2:
                Direction = LEFT;
                break;
            case 3:
                Direction = RIGHT;
                break;
        }
        return Direction;
    }
}
