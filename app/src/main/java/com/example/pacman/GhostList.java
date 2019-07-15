package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    /*
    The img in the source folder is a collection of
    ghosts views. We need to cut them into a list of 4 views.
    We take the original collection file as a matrix of
    numRow * numCol
     */
    private int numRow;
    private int numCol;
    private ArrayList<Bitmap> ghostsViewList;

    public GhostList(Context context, int sx, int sy, Arcade arcade, Pacman pacman, float speed) {
        this.context = context;
        mScreenX = sx;
        mScreenY = sy;
        this.arcade = arcade;

        //currently, the collection is 2*2 with 4 views in total
        numRow = 2;
        numCol = 2;

        //load ghosts img from resource
        Bitmap ghostsCollectionView = BitmapFactory.decodeResource(context.getResources(), R.drawable.ghosts);
        BitmapDivider divider = new BitmapDivider(ghostsCollectionView);
        ArrayList<Bitmap> unsizedGhostsViewList = divider.split(numRow, numCol);
        ghostsViewList = new ArrayList<>();
        for (int i = 0; i < unsizedGhostsViewList.size(); i++) {
            Bitmap bitmap = Bitmap.createScaledBitmap(unsizedGhostsViewList.get(i),
                    sy / 15, sy/15, true);
            ghostsViewList.add(bitmap);
        }

        ghosts = new ArrayList<>();
        for (int i=0; i<GHOSTS_NUM; i++) {
            Ghost ghost = new Ghost(context, sx, sy, arcade, pacman, ghostsViewList.get(i), i, speed);
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
