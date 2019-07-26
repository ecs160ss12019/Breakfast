package com.example.pacman;

public class ChaseFrontBehaviour implements GhostBehaviour {
    @Override
    public int performBehaviour(final MotionInfo ghostMotion, final MotionInfo pacmanMotion,
                                final MotionInfo reference, final ArcadeAnalyzer arcadeAnalyzer) {

        //TODO new class()
        MotionInfo add4MotionInfo = new MotionInfo(pacmanMotion);
        TwoTuple add4Pos = add4MotionInfo.posInArcade;
        for(int i = 0; i < 4; ++i) {
            add4Pos = TwoTuple.moveTo(add4Pos, pacmanMotion.currDirection);
        }

        add4MotionInfo.posInArcade = add4Pos;
        ChaseBehaviour chaseBehaviour = new ChaseBehaviour();

        return chaseBehaviour.performBehaviour(ghostMotion, add4MotionInfo, null, arcadeAnalyzer);
    }

    public ChaseFrontBehaviour() {

    }


}
