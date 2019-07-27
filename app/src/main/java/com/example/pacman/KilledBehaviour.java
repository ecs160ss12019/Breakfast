package com.example.pacman;

public class KilledBehaviour implements GhostBehaviour{
    @Override
    public int performBehaviour(MotionInfo ghostMotion, MotionInfo pacmanMotion, MotionInfo reference, ArcadeAnalyzer arcadeAnalyzer) {
        MotionInfo motionInfo = new MotionInfo();
        motionInfo.posInArcade = new TwoTuple(17, 17);

        ChaseBehaviour chaseBehaviour = new ChaseBehaviour();
        return chaseBehaviour.performBehaviour(ghostMotion, motionInfo, null, arcadeAnalyzer);
    }

    public KilledBehaviour() {

    }
}
