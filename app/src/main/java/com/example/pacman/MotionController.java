package com.example.pacman;

import java.util.Observable;

//There is 1 motionController per arcade
public class MotionController extends Observable {
    final private ArcadeAnalyzer arcadeAnalyzerGhostHouseEnabled;
    final private ArcadeAnalyzer arcadeAnalyzerGhostHouseDisabled;
    final private Arcade arcade;
    private SoundEffects sound;

    public void updateMovingObject(MovingObject movingObject, long fps) {
        if (fps == 0 || fps == -1) {
            System.out.println("MotionController: bad fps");
            return;
        }

        MotionInfo motionInfo = new MotionInfo();
        if (movingObject instanceof Pacman) {
            motionInfo = updatePacman(movingObject.getMotionInfo(), fps);
            if (!TwoTuple.compare(motionInfo.posInScreen, movingObject.motionInfo.posInScreen)) {

//                System.out.println("Pac-Man is moving ##############################################");
//                System.out.println("+++++++++motionInfo.posInScreen "+ motionInfo.posInScreen);
//                System.out.println("+++++++++movingObject.motionInfo.posInScreen "+ movingObject.motionInfo.posInScreen);
            }
        }
        else if (movingObject instanceof Ghost) {
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
        if (ghost.ghostBehaviour instanceof GhostKilledBehaviour) {
            motionUpdater = new MotionUpdater(motionInfo, fps,
                    this.arcade, this.arcadeAnalyzerGhostHouseEnabled);
        } else {
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
    public MotionController(Arcade arcade) {
        this.arcade = arcade;
        this.arcadeAnalyzerGhostHouseEnabled = new ArcadeAnalyzer(arcade, true);
        this.arcadeAnalyzerGhostHouseDisabled = new ArcadeAnalyzer(arcade, false);
    }
}
