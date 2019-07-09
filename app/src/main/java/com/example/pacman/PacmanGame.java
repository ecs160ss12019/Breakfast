package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class PacmanGame extends SurfaceView implements Runnable {

    // Are we debugging?
    private final boolean DEBUGGING = true;

    // These objects are needed to do the drawing
    private SurfaceHolder mOurHolder;
    private Canvas mCanvas;
    private Paint mPaint;

    // Holds the resolution of the screen
    private int mScreenX;
    private int mScreenY;

    // Here is the Thread and two control variables
    private Thread mGameThread = null;
    // This volatile vaiable can be accessed
    // from inside and outside the thread
    private volatile boolean mPlaying;
    private boolean mPaused = false;

    //Our pacman!
    private Pacman pacman;

    public PacmanGame(Context context, int x, int y) {
        // Super... calls the parent class
        // constructor of SurfaceView
        // provided by Android
        super(context);

        // Initialize these two members/fields
        // With the values passesd in as parameters
        mScreenX = x;
        mScreenY = y;

        // Initialize the objects
        // ready for drawing with
        // getHolder is a method of SurfaceView
        mOurHolder = getHolder();
        mPaint = new Paint();

        // Initialize the pacman and ghost
        pacman = new Pacman(context);
    }

    // When we start the thread with:
    // mGameThread.start();
    // the run method is continuously called by Android
    // because we implemented the Runnable interface
    // Calling mGameThread.join();
    // will stop the thread
    @Override
    public void run() {
        // mPlaying gives us finer control
        // rather than just relying on the calls to run
        // mPlaying must be true AND
        // the thread running for the main loop to execute
        while (mPlaying) {
            /*
            while the game is not paused, update
             */

            if(!mPaused) {
                //this might not be null in the future
                mCanvas = null;

                /*
                since this is in the main thread, we should use
                a try catch block.
                 */
                try {
                    //lock canvas to edit pixels
                    mCanvas = mOurHolder.lockCanvas();
                    synchronized (mOurHolder) {
                        draw(mCanvas);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(mCanvas != null) {
                        try {
                            mOurHolder.unlockCanvasAndPost(mCanvas);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    // This method is called by PacmanActivity
    // when the player quits the game
    public void pause() {

        // Set mPlaying to false
        // Stopping the thread isn't
        // always instant
        mPlaying = false;
        try {
            // Stop the thread
            mGameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }

    // This method is called by PacmanActivity
    // when the player starts the game
    public void resume() {
        mPlaying = true;
        // Initialize the instance of Thread
        mGameThread = new Thread(this);

        // Start the thread
        mGameThread.start();
    }

    /*
    We do all global update events here,
    this will be continuously called while
    the thread is running
     */
    public void updateGame() {
        pacman.updateStatus();
    }

    /*
    implement the draw method.
    In this method, we draw game elements individually
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // Fill the screen with a solid color
        mCanvas.drawColor(Color.argb
                (255, 255, 255, 255));
        pacman.draw(canvas);
    }

    /*
    implement onTouchEvent to handle user input
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_DOWN:
                pacman.updateStatus((int)motionEvent.getX(), (int)motionEvent.getY());
        }
        return true;
    }
}
