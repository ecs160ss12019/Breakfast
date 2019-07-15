package com.example.pacman;

import android.content.Context;
import android.graphics.Canvas;

import java.util.ArrayList;

public class GhostList {
    private ArrayList<Ghost> ghosts;
    private final int GHOSTS_NUM = 4;

    private int mScreenX;
    private int mScreenY;
    //context of the game, used access Resource ptr
    private Context context;

    public GhostList(Context context, int sx, int sy, ArcadeList arcades) {
        this.context = context;
        mScreenX = sx;
        mScreenY = sy;
        ghosts = new ArrayList<>();
        for (int i=0; i<GHOSTS_NUM; i++) {
            Ghost ghost = new Ghost(context, sx, sy, arcades.getOptimalPacmanSize(), i);
            ghost.setCenter(arcades.getArcadeContainingPacman().getGhostX_pix(),
                    arcades.getArcadeContainingPacman().getGhostY_pix());
            ghosts.add(ghost);
        }
    }

    public void updateMovementStatus(long mFPS, Arcade arcadeContainingPacman) {
        for (Ghost ghost : ghosts) {
            ghost.updateMovementStatus(mFPS,arcadeContainingPacman);
        }
    }

    public void draw(Canvas canvas) {
        for (Ghost ghost : ghosts) {
            ghost.draw(canvas);
        }
    }
}
