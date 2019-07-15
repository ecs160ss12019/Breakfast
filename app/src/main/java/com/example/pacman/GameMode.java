package com.example.pacman;

import android.util.Log;

/*
* this class modifies the speed of game based on
* the player selection
* */
public class GameMode {
    private int modeSelection;
    private final int easy = 0;
    private final int medium = 1;
    private final int hard = 2;

    public GameMode(int inputMode){
        modeSelection = 0;
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
            case medium:
                normalMode();
                break;
            case hard:
                hardMode();
                break;
        }
    }
    public void easyMode(){

    }
    public void normalMode(){

    }
    public void hardMode(){

    }
}
