package com.example.pacman;

public class GhostStationaryBehaviour implements GhostBehaviour {
    @Override
    public int performBehaviour(MotionInfo ghostMotion, MotionInfo pacmanMotion, MotionInfo reference, ArcadeAnalyzer arcadeAnalyzer) {
        return ghostMotion.nextDirection = 3;
    }
}
