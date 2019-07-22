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
    private int displayMode;

    public float getGhostsSpeed() {
        return ghostsSpeed;
    }

    public float getPacmanSpeed() {
        return pacmanSpeed;
    }

    public int getDisplayMode() { return displayMode; }

    public GameMode(int inputMode, int screenX){
        this.screenX = screenX;
        // mode is normal by default
        modeSelection = 1;
        switch (inputMode) {
            case 0:
                modeSelection = 0;
                displayMode = 0;
                break;
            case 1:
                displayMode = 1;
                break;
            case 2:
                modeSelection = 2;
                displayMode = 2;
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
        pacmanSpeed = screenX/14;
        ghostsSpeed = screenX/14;
    }
    public void normalMode(){
        // This code means the Pacman and ghosts can cover the width
        // of the screen in 9 seconds
        pacmanSpeed = screenX/10;
        ghostsSpeed = screenX/10;
    }
    public void hardMode(){
        // This code means the Pacman and ghosts can cover the width
        // of the screen in 9 seconds
        pacmanSpeed = screenX/5;
        ghostsSpeed = screenX/5;
    }
}
