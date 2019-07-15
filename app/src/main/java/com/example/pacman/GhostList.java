package com.example.pacman;

import android.content.Context;
import android.graphics.Canvas;

import java.util.ArrayList;

// One GhostList contains 4 ghosts associated with one Arcade
public class GhostList {
    ArrayList<Ghost> ghosts;
    private final int GHOSTS_NUM = 4;
    private Arcade arcade; // the arcade which the 4 ghosts are in.
    private Pacman pacman; // four ghosts are chasing the same pacman.

    private int mScreenX;
    private int mScreenY;
    //context of the game, used access Resource ptr
    private Context context;

    public GhostList(Context context, int sx, int sy, Arcade arcade, Pacman pacman) {
        this.context = context;
        mScreenX = sx;
        mScreenY = sy;
        this.arcade = arcade;
        ghosts = new ArrayList<>();
        for (int i=0; i<GHOSTS_NUM; i++) {
            Ghost ghost = new Ghost(context, sx, sy, arcade, pacman, i);
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

    public void killPacman() {
        for (Ghost ghost : ghosts) {
            ghost.killPacman();
        }
    }
}
