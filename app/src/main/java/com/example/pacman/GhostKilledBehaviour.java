package com.example.pacman;

public class GhostKilledBehaviour implements GhostBehaviour{
    @Override
    public int performBehaviour(MotionInfo ghostMotion, MotionInfo pacmanMotion, MotionInfo reference, ArcadeAnalyzer arcadeAnalyzer) {
        MotionInfo motionInfo = new MotionInfo();
        motionInfo.posInArcade = new TwoTuple(14, 14);

        GhostChaseBehaviour ghostChaseBehaviour = new GhostChaseBehaviour();
        return ghostChaseBehaviour.performBehaviour(ghostMotion, motionInfo, null, arcadeAnalyzer);
    }

    public GhostKilledBehaviour() {

    }
}
