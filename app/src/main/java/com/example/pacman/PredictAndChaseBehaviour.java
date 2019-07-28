package com.example.pacman;

public class PredictAndChaseBehaviour implements GhostBehaviour {
    @Override
    public int performBehaviour(final MotionInfo ghostMotion, final MotionInfo pacmanMotion, final MotionInfo reference,
                                final ArcadeAnalyzer arcadeAnalyzer) {
        //TODO new class()
        MotionInfo predictMotionInfo = new MotionInfo(pacmanMotion);
        TwoTuple predictPos = predictMotionInfo.posInArcade;
        for(int i = 0; i < 2; ++i) {
            predictPos = TwoTuple.moveTo(predictPos, pacmanMotion.currDirection);
        }

        predictPos.x = predictPos.x + (predictPos.x - reference.posInArcade.x);
        predictPos.y = predictPos.y + (predictPos.y - reference.posInArcade.y);
        predictMotionInfo.posInArcade = predictPos;
        ChaseBehaviour chaseBehaviour = new ChaseBehaviour();

        return chaseBehaviour.performBehaviour(ghostMotion, predictMotionInfo, null, arcadeAnalyzer);
    }

    public PredictAndChaseBehaviour() {
    }
}
