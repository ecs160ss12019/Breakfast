package com.example.pacman;

import java.lang.*;
import java.util.LinkedList;
import java.util.Queue;

public class GhostBehavior {
    private TwoTuple PacmanPos;
    private TwoTuple GhostPos;
    private int PacmanDir;
    private ArcadeAnalyzer arcadeAnalyzer;

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

    public int chase(){
            boolean AllowToTurn = false;
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
            while(!AllowToTurn){
                AllowToTurn = this.arcadeAnalyzer.allowsToGo(GhostPos, DirectionQ.peek());
                if(!AllowToTurn){
                    DirectionQ.remove();
                }
            }

            return DirectionQ.peek();

    }
}
