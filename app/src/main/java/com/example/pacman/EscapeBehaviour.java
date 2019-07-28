package com.example.pacman;

import java.util.ArrayList;
import java.util.Collections;

public class EscapeBehaviour implements GhostBehaviour {
    final int LEFT = 0;
    final int RIGHT = 1;
    final int UP = 2;
    final int DOWN = 3;

    @Override
    public int performBehaviour(MotionInfo ghostMotion, MotionInfo pacmanMotion, MotionInfo reference, ArcadeAnalyzer arcadeAnalyzer) {
        TwoTuple ghostPosInArcade = ghostMotion.posInArcade;
        TwoTuple pacmanPosInArcade = pacmanMotion.posInArcade;

        //check for necessity
        if (arcadeAnalyzer.getAllowedDirections(ghostPosInArcade).size() >= 3) {
            int MaxIndex = arcadeAnalyzer.getAllowedDirections(ghostPosInArcade).size() - 1;

            //this is a 3 or 4 ways cross
            ArrayList<ComparableDirection> comparableDirections = new ArrayList<>();
            ArrayList<Integer> allowedDirections = arcadeAnalyzer.getAllowedDirections(ghostPosInArcade);
            for (Integer direction : allowedDirections) {
                comparableDirections.add(new ComparableDirection(direction, ghostPosInArcade, pacmanPosInArcade));
            }
            Collections.sort(comparableDirections, new CompareDistance());

            return comparableDirections.get(MaxIndex).getDirection();
        }

        else if (!arcadeAnalyzer.allowsToGo(ghostPosInArcade, ghostMotion.nextDirection)) {


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

    public EscapeBehaviour() {

    }


}
