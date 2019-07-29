package com.example.pacman;

public class GhostScatterRightBottom implements GhostBehaviour, GhostScatterBehaviourInterface {
    @Override
    public int performBehaviour(MotionInfo ghostMotion, MotionInfo pacmanMotion, MotionInfo reference, ArcadeAnalyzer arcadeAnalyzer) {
        MotionInfo motionInfo = new MotionInfo();
        motionInfo.posInArcade = new TwoTuple(28, 24);

        GhostChaseBehaviour ghostChaseBehaviour = new GhostChaseBehaviour();
        return ghostChaseBehaviour.performBehaviour(ghostMotion, motionInfo, null, arcadeAnalyzer);
    }
}
