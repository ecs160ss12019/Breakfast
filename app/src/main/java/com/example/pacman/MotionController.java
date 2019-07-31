package com.example.pacman;

import java.util.ArrayList;
import java.util.Observable;

//There is 1 motionController per arcade
public class MotionController extends Observable {
    final private ArcadeAnalyzer arcadeAnalyzerGhostHouseEnabled;
    final private ArcadeAnalyzer arcadeAnalyzerGhostHouseDisabled;
    final private Arcade arcade;
    final private GameMode gameMode;

    public void updateMovingObject(MovingObject movingObject, long fps) {
        if (fps == 0 || fps == -1) {
            System.out.println("MotionController: bad fps");
            return;
        }

        MotionInfo motionInfo = new MotionInfo();
        if (movingObject instanceof Pacman) {
            motionInfo = updatePacman(movingObject.getMotionInfo(), fps);
        }
        else if (movingObject instanceof Ghost) {
            if (motionInfo.nextDirection == -1) return;
            motionInfo = updateGhost((Ghost)movingObject, movingObject.getMotionInfo(), fps);
        }
        else if (movingObject instanceof Cake) {
            motionInfo = updateCake(movingObject.getMotionInfo(), fps);
        }
        else {
            System.out.println("MotionController: Error matching moving object");
        }

        movingObject.setMotionInfo(motionInfo);
    }

    private MotionInfo updatePacman(final MotionInfo motionInfo, final long fps) {
        MotionUpdater motionUpdater = new MotionUpdater(motionInfo, fps,
                    this.arcade, this.arcadeAnalyzerGhostHouseDisabled);

        return motionUpdater.updateMotion();
    }

    private MotionInfo updateGhost(final Ghost ghost, final MotionInfo motionInfo, final long fps) {

        MotionUpdater motionUpdater;

        if (ghost.ghostBehaviour instanceof GhostEnterBehaviour ||
                ghost.ghostBehaviour instanceof GhostExitBehaviour) {

            motionInfo.setSpeed(gameMode.getGhostsSpeed());
            motionUpdater = new MotionUpdater(motionInfo, fps,
                    this.arcade, this.arcadeAnalyzerGhostHouseEnabled);
        }

        else if(ghost.ghostBehaviour instanceof GhostEscapeBehaviour) {

            motionInfo.setSpeed(gameMode.getGhostsSpeed()*3/5);
            motionUpdater = new MotionUpdater(motionInfo, fps,
                    this.arcade, this.arcadeAnalyzerGhostHouseDisabled);
        }
        else {

            motionInfo.setSpeed(gameMode.getGhostsSpeed());
            motionUpdater = new MotionUpdater(motionInfo, fps,
                    this.arcade, this.arcadeAnalyzerGhostHouseDisabled);
        }

        return motionUpdater.updateMotion();
    }

    private MotionInfo updateCake(final MotionInfo motionInfo, final long fps) {
        MotionUpdater motionUpdater = new MotionUpdater(motionInfo, fps,
                this.arcade, this.arcadeAnalyzerGhostHouseDisabled);
        return motionUpdater.updateMotion();
    }




    //Constructor
    public MotionController(Arcade arcade, GameMode gameMode) {
        this.arcade = arcade;
        this.gameMode = gameMode;
        this.arcadeAnalyzerGhostHouseEnabled = new ArcadeAnalyzer(arcade, true);
        this.arcadeAnalyzerGhostHouseDisabled = new ArcadeAnalyzer(arcade, false);
    }
}
