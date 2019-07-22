package com.example.pacman;

/*
* this class modifies the speed of game based on
* the player selection
* */
public class GameMode {
    private int modeSelection;

    private float ghostsSpeed;
    private float pacmanSpeed;
    private int screenX;
    private final int easy = 0;
    private final int normal = 1;
    private final int hard = 2;

    public float getGhostsSpeed() {
        return ghostsSpeed;
    }

    public float getPacmanSpeed() {
        return pacmanSpeed;
    }

    public GameMode(int inputMode, int screenX){
        // mode is normal by default
        this.screenX = screenX;
        modeSelection = 1;
        switch (inputMode) {
            case 0:
                break;
            case 1:
                modeSelection = 1;
                break;
            case 2:
                modeSelection = 2;
                break;
        }
        modeManager();

    }
    public void modeManager(){
        switch (modeSelection){
            case easy:
                easyMode();
                break;
            case normal:
                normalMode();
                break;
            case hard:
                hardMode();
                break;
        }
    }
    public void easyMode(){
        // This code means the Pacman and ghosts can cover the width
        // of the screen in 9 seconds
        pacmanSpeed = screenX/2;
        ghostsSpeed = screenX/2;
    }
    public void normalMode(){
        // This code means the Pacman and ghosts can cover the width
        // of the screen in 9 seconds
        pacmanSpeed = screenX/8;
        ghostsSpeed = screenX/8;
    }
    public void hardMode(){
        // This code means the Pacman and ghosts can cover the width
        // of the screen in 9 seconds
        pacmanSpeed = screenX/4;
        ghostsSpeed = screenX/4;
    }
}
