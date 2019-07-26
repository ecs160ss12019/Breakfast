package com.example.pacman;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.ArrayList;

public class PowerPellet extends StationaryObject implements PelletCell {
    private boolean reward;

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
