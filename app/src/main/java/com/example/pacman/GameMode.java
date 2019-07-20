package com.example.pacman;

import android.util.Log;

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
        this.screenX = screenX;
        // mode is normal by default
        modeSelection = normal;
        switch (inputMode) {
            case 0:
                modeSelection = easy;
            case 1:
                break;
            case 2:
                modeSelection = hard;
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
        this.pacmanSpeed = screenX/50;
        this.ghostsSpeed = screenX/50;
    }
    public void normalMode(){
        this.pacmanSpeed = screenX/8;
        this.ghostsSpeed = screenX/8;
    }
    public void hardMode(){
        this.pacmanSpeed = screenX/15;
        this.ghostsSpeed = screenX/15;
    }
}
