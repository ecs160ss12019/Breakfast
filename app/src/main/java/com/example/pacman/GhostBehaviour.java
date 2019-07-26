package com.example.pacman;

public interface GhostBehaviour {
    int performBehaviour(final MotionInfo ghostMotion, final MotionInfo pacmanMotion, final MotionInfo reference,
                                    final ArcadeAnalyzer arcadeAnalyzer);
    //DirectionQueue performBehaviour();
}
