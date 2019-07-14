package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Pair;

public class Ghost implements GameObject {

    final int LEFT = 0;
    final int RIGHT = 1;
    final int UP = 2;
    final int DOWN = 3;

    //coordinate
    private int x;
    private int y;
    private float speed;
    private int mScreenX;
    private int mScreenY;

    //context of the game, used access Resource ptr
    private Context context;

    private Bitmap ghostView;

    private int bitmapWidth;
    private int bitmapHeight;

    private int currDirection;
    private int nextDirection;

    public Ghost(Context context, int sx, int sy, Pair<Integer, Integer> optimalSize) {
        this.context = context;
        mScreenX = sx;
        mScreenY = sy;
        Bitmap unsizedGhostView = BitmapFactory.decodeResource(context.getResources(), R.drawable.ghost);
        ghostView = Bitmap.createScaledBitmap(unsizedGhostView, optimalSize.first, optimalSize.second, true);
        bitmapWidth = ghostView.getWidth();
        bitmapHeight = ghostView.getHeight();
    }

    //The starting point need to be initialized after construction
    //if collision, use this to roll back
    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(ghostView, x-bitmapWidth/2, y-bitmapHeight/2, null);
    }

    /*
    We use this func to calculate the after move location in a direction,
    no matter the direction is valid or not.
     */
    private Pair<Integer, Integer> move(int direction, long fps) {
        int nextX = this.x;
        int nextY = this.y;

        // Move the pacman based on the direction variable
        // and the speed of the previous frame
        if (direction == LEFT) {
            nextX = (int)(nextX - speed / fps);
        }
        if (direction == RIGHT) {
            nextX = (int)(nextX + speed / fps);
        }
        if (direction == UP) {
            nextY = (int)(nextY - speed / fps);
        }
        if (direction == DOWN) {
            nextY = (int)(nextY + speed / fps);
        }

        // Stop the Pacman going off the screen
        if (nextX - bitmapWidth / 2 < 0) {
            nextX = bitmapWidth / 2;
        }
        if (nextX + bitmapWidth / 2 > mScreenX) {
            nextX = mScreenX - bitmapWidth / 2;
        }
        if (nextY - bitmapHeight / 2 < 0) {
            nextY = bitmapHeight / 2;
        }
        if (nextY + bitmapHeight / 2 > mScreenY) {
            nextY = mScreenY - bitmapHeight / 2;
        }

        return new Pair<>(nextX, nextY);
    }

    @Override
    public void updateStatus(long fps) {
        /*
        We cannot update is the fps is -1,
        otherwise there will be a overflow
        in speed/fps.
         */
        if (fps == -1) {
            return;
        }

        int nextX = 0;
        int nextY = 0;

        if (nextDirection != -1) {
            Pair<Integer, Integer> next = move(nextDirection, fps);
            nextX = next.first;
            nextY = next.second;

            set(nextX, nextY);
            currDirection = nextDirection;
        }

        /*
        Either there is no new user input direction,
        or that direction does not work.
        Try moving in current direction, if it do
        not work as well, stay in current position
         */
        Pair<Integer, Integer> next = move(currDirection, fps);
        nextX = next.first;
        nextY = next.second;
        set(nextX, nextY);
    }

}
