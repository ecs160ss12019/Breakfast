package com.example.pacman;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.ArrayList;

/*
    This is a power pellet cell, which is slightly different
    than the regular pellet. We distingush them because they
    have real power, with give the pacman strength to eat ghost
    instead of getting chased by them.
 */

public class PowerPellet extends StationaryObject implements PelletCell {
    private boolean reward;

    // Reward is to check if the points were added to the scoresystem.
    @Override
    public void reward() {
        reward = true;
    }
    public boolean checkReward(){
        return reward;
    }

    public PowerPellet(final StaticInfo staticInfo, final ArrayList<Bitmap> viewList) {
        super(staticInfo, viewList);
        this.currentBitmapIndex = 0;
        this.reward = false;
    }
}
