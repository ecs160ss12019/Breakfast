package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;

public class PacmanGenerator {
    final Arcade arcade;
    final GameMode gameMode;

    final TwoTuple pacmanInitPos;
    final ArrayList<Bitmap> pacmanViewList;

    public Pacman getPacman() {
        MotionInfo pacmanInitMotion = new MotionInfo(
                pacmanInitPos,
                arcade.mapScreen(pacmanInitPos),
                0, TwoTuple.RIGHT, -1, gameMode.getPacmanSpeed());

        return new Pacman(pacmanInitMotion, pacmanViewList);
    }

    public PacmanGenerator(final Arcade arcade, Context context,
                           final TwoTuple mScreen, final GameMode gameMode) {
        this.arcade = arcade;
        this.pacmanInitPos = arcade.pacmanPosition;
        this.gameMode = gameMode;
        final BitmapDivider bitmapDivider = new BitmapDivider(context);
        pacmanViewList = BitmapDivider.splitAndResize(
                bitmapDivider.loadBitmap(R.drawable.pacmancombined),
                new TwoTuple(6,4),
                new TwoTuple(mScreen.y / 15, mScreen.y / 15));
    }
}
