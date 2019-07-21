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
        // This code means the Pacman and ghosts can cover the width
        // of the screen in 9 seconds
        pacmanSpeed = screenX/50;
        ghostsSpeed = screenX/50;
    }
    public void normalMode(){
        // This code means the Pacman and ghosts can cover the width
        // of the screen in 9 seconds
        pacmanSpeed = screenX/100;
        ghostsSpeed = screenX/100;
    }
    public void hardMode(){
        // This code means the Pacman and ghosts can cover the width
        // of the screen in 9 seconds
        pacmanSpeed = screenX/50;
        ghostsSpeed = screenX/50;
    }
}
