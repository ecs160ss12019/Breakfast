package com.example.pacman;

public class GhostScatterBehaviour implements GhostBehaviour {
    @Override
    public int performBehaviour(MotionInfo ghostMotion, MotionInfo pacmanMotion, MotionInfo reference, ArcadeAnalyzer arcadeAnalyzer) {
        MotionInfo motionInfo = new MotionInfo();
        motionInfo.posInArcade = new TwoTuple(28, 31);

        ChaseBehaviour chaseBehaviour = new ChaseBehaviour();
        return chaseBehaviour.performBehaviour(ghostMotion, motionInfo, null, arcadeAnalyzer);
    }
}
