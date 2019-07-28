package com.example.pacman;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class GhostChaseBehaviour implements GhostBehaviour {
    final int LEFT = 0;
    final int RIGHT = 1;
    final int UP = 2;
    final int DOWN = 3;

    @Override
    public int performBehaviour(final MotionInfo ghostMotion, final MotionInfo pacmanMotion,
                                           final MotionInfo reference,
                                           final ArcadeAnalyzer arcadeAnalyzer) {
        TwoTuple ghostPosInArcade = ghostMotion.posInArcade;
        TwoTuple pacmanPosInArcade = pacmanMotion.posInArcade;

        //check for necessity
        if (arcadeAnalyzer.getAllowedDirections(ghostPosInArcade).size() >= 3) {

            //this is a 3 or 4 ways cross
            ArrayList<ComparableDirection> comparableDirections = new ArrayList<>();
            ArrayList<Integer> allowedDirections = new ArrayList<>(arcadeAnalyzer.getAllowedDirections(ghostPosInArcade));

            //Allowed direction cannot be a immediate reversal
            int currDirection = ghostMotion.currDirection;
            switch (currDirection) {
                case LEFT:
                    allowedDirections.removeAll(Arrays.asList(RIGHT));
                    break;
                case RIGHT:
                    allowedDirections.removeAll(Arrays.asList(LEFT));
                    break;
                case UP:
                    allowedDirections.removeAll(Arrays.asList(DOWN));
                    break;
                case DOWN:
                    allowedDirections.removeAll(Arrays.asList(UP));
                    break;
            }

            for (Integer direction : allowedDirections) {
                comparableDirections.add(new ComparableDirection(direction, ghostPosInArcade, pacmanPosInArcade));
            }
            Collections.sort(comparableDirections, new CompareDistance());

            return comparableDirections.get(0).getDirection();
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

    public GhostChaseBehaviour(){

    }


}
