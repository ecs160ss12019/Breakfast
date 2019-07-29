package com.example.pacman;

public class GhostPredictAndChaseBehaviour implements GhostBehaviour, GhostChaseBehaviourInterface {
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
        GhostChaseBehaviour ghostChaseBehaviour = new GhostChaseBehaviour();

        return ghostChaseBehaviour.performBehaviour(ghostMotion, predictMotionInfo, null, arcadeAnalyzer);
    }

    public GhostPredictAndChaseBehaviour() {
    }
}
