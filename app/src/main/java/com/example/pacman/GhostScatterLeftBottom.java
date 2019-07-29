package com.example.pacman;

public class GhostScatterLeftBottom implements GhostBehaviour {
    @Override
    public int performBehaviour(MotionInfo ghostMotion, MotionInfo pacmanMotion, MotionInfo reference, ArcadeAnalyzer arcadeAnalyzer) {
        MotionInfo motionInfo = new MotionInfo();
        motionInfo.posInArcade = new TwoTuple(28, 3);

        GhostChaseBehaviour ghostChaseBehaviour = new GhostChaseBehaviour();
        return ghostChaseBehaviour.performBehaviour(ghostMotion, motionInfo, null, arcadeAnalyzer);
    }
}
