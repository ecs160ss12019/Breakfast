package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class GhostsGenerator {
    final Arcade arcade;
    final GameMode gameMode;

    final ArrayList<TwoTuple> ghostsInitPos;
    final ArrayList<Bitmap> NormalGhostsViewList;
    final ArrayList<Bitmap> ghostKilledList;
    final ArrayList<Bitmap> ghostEscapeList;
    final int[] directionList;
    final int[] waitTime;

    public ArrayList<Ghost> getGhosts() {
        ArrayList<Ghost> ghosts = new ArrayList<>();

        for (int i = 0; i < ghostsInitPos.size(); i++) {
            MotionInfo initMotion = new MotionInfo(
                    ghostsInitPos.get(i),
                    arcade.mapScreen(ghostsInitPos.get(i)),
                    0, directionList[i], -1, gameMode.getGhostsSpeed());

            ghosts.add(new Ghost(i, initMotion, new ArrayList<>(NormalGhostsViewList.subList(i * 4, i * 4 + 4)),
                    ghostEscapeList, ghostKilledList, new GhostStationaryBehaviour(), waitTime[i]));
        }

        return ghosts;
    }

    public GhostsGenerator(final Arcade arcade, Context context,
                           final TwoTuple mScreen, final GameMode gameMode) {
        this.arcade = arcade;
        this.ghostsInitPos = arcade.ghostsPos;
        this.gameMode = gameMode;
        directionList = new int[]{TwoTuple.UP, TwoTuple.RIGHT, TwoTuple.UP, TwoTuple.UP};
        waitTime = new int[]{0,1,2,3};

        final BitmapDivider bitmapDivider = new BitmapDivider(context);
        final ArrayList<Bitmap> ghostsViewList = BitmapDivider.splitAndResize(
                bitmapDivider.loadBitmap(R.drawable.ghostcombined),
                new TwoTuple(6,4),
                new TwoTuple(mScreen.y / 15, mScreen.y / 15));

        NormalGhostsViewList = new ArrayList<>(ghostsViewList.subList(0,16));
        ghostKilledList = new ArrayList<>(ghostsViewList.subList(16,20));
        ghostEscapeList = new ArrayList<>(ghostsViewList.subList(20,24));
    }
}
