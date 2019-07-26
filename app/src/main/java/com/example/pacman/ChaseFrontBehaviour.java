package com.example.pacman;

import java.util.ArrayList;
import java.util.Collections;

public class ChaseFrontBehaviour implements GhostBehaviour {
    final int LEFT = 0;
    final int RIGHT = 1;
    final int UP = 2;
    final int DOWN = 3;
    private MotionInfo PacmanMotion;
    private ArcadeAnalyzer arcadeAnalyzer;

    @Override
    public int performBehaviour(MotionInfo ghostMotion, MotionInfo pacmanMotion, MotionInfo reference, ArcadeAnalyzer arcadeAnalyzer) {
        this.PacmanMotion = pacmanMotion;
        this.arcadeAnalyzer = arcadeAnalyzer;
        TwoTuple ghostPosInArcade = ghostMotion.posInArcade;
        TwoTuple pacmanPosInArcade = CutoffPoint();

        //check for necessity
        if (arcadeAnalyzer.getAllowedDirections(ghostPosInArcade).size() >= 3) {

            //this is a 3 or 4 ways cross
            ArrayList<ComparableDirection> comparableDirections = new ArrayList<>();
            ArrayList<Integer> allowedDirections = arcadeAnalyzer.getAllowedDirections(ghostPosInArcade);
            for (Integer direction : allowedDirections) {
                comparableDirections.add(new ComparableDirection(direction, ghostPosInArcade, pacmanPosInArcade));
            }
            Collections.sort(comparableDirections, new CompareDistance());

            return comparableDirections.get(1).getDirection();
        } else if (!arcadeAnalyzer.allowsToGo(ghostPosInArcade, ghostMotion.nextDirection)) {


            //now this must be a turn with only 2 exits
            ArrayList<Integer> allowedDirections = arcadeAnalyzer.getAllowedDirections(ghostPosInArcade);
            switch (ghostMotion.currDirection) {
                case LEFT:
                case RIGHT:
                    return allowedDirections.contains(UP) ? UP : DOWN;
                case UP:
                case DOWN:
                    return allowedDirections.contains(LEFT) ? LEFT : RIGHT;
            }
        }

        return ghostMotion.nextDirection;
    }

    public ChaseFrontBehaviour() {

    }

    public TwoTuple CutoffPoint(){
        TwoTuple CutoffPoint = PacmanMotion.posInArcade;
        if(arcadeAnalyzer.allowsToGo(PacmanMotion.posInArcade, PacmanMotion.currDirection)){
            switch (PacmanMotion.currDirection){
                case LEFT:
                    CutoffPoint = CutoffPoint.toLeft();
                case RIGHT:
                    CutoffPoint = CutoffPoint.toRight();
                case UP:
                    CutoffPoint = CutoffPoint.toUp();
                case DOWN:
                    CutoffPoint = CutoffPoint.toDown();
            }
        }
        return CutoffPoint;
    }

}
