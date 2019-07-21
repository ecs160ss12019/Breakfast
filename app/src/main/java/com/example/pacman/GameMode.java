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
    private final int EASY = 0;
    private final int NORMAL = 1;
    private final int HARD = 2;

    public float getGhostsSpeed() {
        return ghostsSpeed;
    }

    public float getPacmanSpeed() {
        return pacmanSpeed;
    }

    public GameMode(int inputMode, int screenX){
        this.screenX = screenX;
        // mode is normal by default
        modeSelection = NORMAL;
        switch (inputMode) {
            case 0:
                modeSelection = EASY;
            case 1:
                break;
            case 2:
                modeSelection = HARD;
                break;
        }
        modeManager();
    }
    public void modeManager(){
        switch (modeSelection){
            case EASY:
                easyMode();
                break;
            case NORMAL:
                normalMode();
                break;
            case HARD:
                hardMode();
                break;
        }
    }
    public void easyMode(){
        this.pacmanSpeed = screenX/50;
        this.ghostsSpeed = screenX/50;
    }
    public void normalMode(){
        // This code means the Pacman and ghosts can cover the width
        // of the screen in 9 seconds
        this.pacmanSpeed = screenX/8;
        this.ghostsSpeed = screenX/8;
    }
    public void hardMode(){
        this.pacmanSpeed = screenX/15;
        this.ghostsSpeed = screenX/15;
    }
}
