package com.example.pacman;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.ArrayList;

public class NormalPellet extends StationaryObject implements PelletCell {
    private int reward;

    @Override
    public int reward() {
        return reward;
    }

    public NormalPellet(final StaticInfo staticInfo, final ArrayList<Bitmap> viewList) {
        super(staticInfo, viewList);
        this.currentBitmapIndex = 0;
        this.reward = 5;
    }


}
