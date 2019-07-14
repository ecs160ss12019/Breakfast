package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Random;

public class Ghost implements GameObject {

    final int LEFT = 0;
    final int RIGHT = 1;
    final int UP = 2;
    final int DOWN = 3;
    boolean alive;

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

    private boolean collision;

    public Ghost(Context context, int sx, int sy, Pair<Integer, Integer> optimalSize, int direction) {
        this.context = context;
        mScreenX = sx;
        mScreenY = sy;
        this.currDirection = direction;
        this.nextDirection = -1;

        Bitmap unsizedGhostView = BitmapFactory.decodeResource(context.getResources(), R.drawable.ghost);
        ghostView = Bitmap.createScaledBitmap(unsizedGhostView, optimalSize.first, optimalSize.second, true);
        bitmapWidth = ghostView.getWidth();
        bitmapHeight = ghostView.getHeight();

        speed = mScreenX / 10;
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
    public void updateStatus(long fps){

    }

    public void updateStatus(long fps, Arcade arcade) {
        /*
        We cannot update is the fps is -1,
        otherwise there will be a overflow
        in speed/fps.
         */
        if (fps == -1) {
            return;
        }

        CollisionDetector collisionDetector = new CollisionDetector();

        int nextX = 0;
        int nextY = 0;

        if (nextDirection != -1) {
            Pair<Integer, Integer> next = move(nextDirection, fps);
            nextX = next.first;
            nextY = next.second;

            //Check collision
            ArrayList<Obstacle> obstacles =arcade.getObstacleList(nextX, nextY);
            Obstacle ghostReference = new Obstacle(nextX, nextY,
                    (int)(bitmapWidth*0.8), (int)(bitmapHeight*0.8));

            collision = collisionDetector.collisionExist(ghostReference, obstacles);
            if (!collision) {
                set(nextX, nextY);
                currDirection = nextDirection;
                return;
            }
        }

        /*
        Either there is no new direction,
        or that direction does not work.
        Try moving in current direction, if it do
        not work as well, stay in current position
         */
        Pair<Integer, Integer> next = move(currDirection, fps);
        nextX = next.first;
        nextY = next.second;

        //Check collision
        ArrayList<Obstacle> obstacles = arcade.getObstacleList(nextX, nextY);
        Obstacle ghostReference = new Obstacle(nextX, nextY,
                (int)(bitmapWidth * 0.8), (int)(bitmapHeight * 0.8));

        collision = collisionDetector.collisionExist(ghostReference, obstacles);
        if(!collision) {
            set(nextX, nextY);
        }
    }

    public void updateMovementStatus(long fps, Arcade arcade) {
        int inputDirection = -1;
        if(collision) {
            Random randomGenerator = new Random();
            inputDirection = randomGenerator.nextInt(3);
        }
        switch (inputDirection) {
            case -1:
                //nextDirection = -1;
                break;
            case 0:
                nextDirection = UP;
                break;
            case 1:
                nextDirection = DOWN;
                break;
            case 2:
                nextDirection = LEFT;
                break;
            case 3:
                nextDirection = RIGHT;
                break;
        }
        updateStatus(fps, arcade);
    }
}
