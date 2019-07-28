package com.example.pacman;

import java.util.Random;

public class SearchAndChaseBehaviour implements GhostBehaviour{
    @Override
    public int performBehaviour(MotionInfo ghostMotion, MotionInfo pacmanMotion, MotionInfo reference, ArcadeAnalyzer arcadeAnalyzer) {
        //TODO new class()
        MotionInfo currMotionInfo = new MotionInfo(ghostMotion);
        MotionInfo targetMotionInfo = new MotionInfo(pacmanMotion);
        TwoTuple currPos = currMotionInfo.posInArcade;
        TwoTuple targetPos = targetMotionInfo.posInArcade;

        double xDiffSquared = (currPos.x - targetPos.x) * (currPos.x - targetPos.x);
        double yDiffSquared = (currPos.y - targetPos.y) * (currPos.y - targetPos.y);
        int Distance =  (int)(Math.sqrt(xDiffSquared + yDiffSquared));

        ChaseBehaviour chaseBehaviour = new ChaseBehaviour();

        if (Distance < 8) {
            return chaseBehaviour.performBehaviour(currMotionInfo, targetMotionInfo, null, arcadeAnalyzer);
        } else {
            Random random = new Random();
            int next = random.nextInt(5);
            while (!arcadeAnalyzer.allowsToGo(currPos, next)) {
                next = random.nextInt(5);
            }

            return next;
        }
    }

    public SearchAndChaseBehaviour() {

    }
}
