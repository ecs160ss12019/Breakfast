package com.example.pacman;

import java.util.Observable;

//There is 1 motionController per arcade
public class MotionController extends Observable {
    private ArcadeAnalyzer arcadeAnalyzer;
    private Arcade arcade;

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
            motionInfo = updateGhost(movingObject.getMotionInfo(), fps);
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
                this.arcade, this.arcadeAnalyzer);
        return motionUpdater.updateMotion();
    }

    private MotionInfo updateGhost(final MotionInfo motionInfo, final long fps) {
        MotionUpdater motionUpdater = new MotionUpdater(motionInfo, fps,
                this.arcade, this.arcadeAnalyzer);
        return motionUpdater.updateMotion();
    }

    private MotionInfo updateCake(final MotionInfo motionInfo, final long fps) {
        MotionUpdater motionUpdater = new MotionUpdater(motionInfo, fps,
                this.arcade, this.arcadeAnalyzer);
        return motionUpdater.updateMotion();
    }




    //Constructor
    public MotionController(Arcade arcade) {
        this.arcade = arcade;
        this.arcadeAnalyzer = new ArcadeAnalyzer(arcade);
        this.arcadeAnalyzer.run();
    }
}
