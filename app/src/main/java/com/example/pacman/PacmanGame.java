package com.example.pacman;

import android.content.Context;
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

    // How many frames per second did we get?
    private long mFPS;
    // The number of milliseconds in a second
    private final int MILLIS_IN_SECOND = 1000;

    // Holds the resolution of the screen
    private int mScreenX;
    private int mScreenY;

    // Here is the Thread and two control variables
    private Thread mGameThread = null;
    // This volatile variable can be accessed
    // from inside and outside the thread
    private volatile boolean mPlaying;
    private boolean mPaused = false;

    //our UserInput object
    private UserInput userInput;

    //Our pacman!
    private Pacman pacman;

    //Our Arcade List
    private ArcadeList arcades;

    //Our Navigation Buttons!
    NavigationButtons navigationButtons;

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
            // What time is it now at the start of the loop?
            long frameStartTime = System.currentTimeMillis();

            if(!mPaused) {
                updateGame();
                // Now the Pacman and Ghosts are in their new positions
                // we can see if there have been any collisions
                detectCollisions();
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

            // How long did this frame/loop take?
            // Store the answer in timeThisFrame
            long timeThisFrame = System.currentTimeMillis() - frameStartTime;

            // Make sure timeThisFrame is at least 1 millisecond
            // because accidentally dividing by zero crashes the game
            if (timeThisFrame > 0) {
                // Store the current frame rate in mFPS
                // ready to pass to the update methods of
                // mBat and mBall next frame/loop
                mFPS = MILLIS_IN_SECOND / timeThisFrame;
            }
        }
    }

    /*
    We do all global update events here,
    this will be continuously called while
    the thread is running
     */
    public void updateGame() {
        int direction = navigationButtons.checkAndUpdate(userInput);

        /*
        if player touched or is continuous touching
        updated pacman position and etc.
         */
        if(!(direction < 0)) {
            pacman.updateMovementStatus(direction, mFPS);
        }
    }

    private void detectCollisions(){
        // Has the Pacman hit the Ghost?

        // Has the Pacman hit the edge of the screen

        // Bottom

        // Top

        // Left

        // Right

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
    implement the draw method.
    In this method, we draw game elements individually
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // Fill the screen with a solid color
        mCanvas.drawColor(Color.argb
                (255, 255, 255, 255));
        arcades.draw(canvas);
        pacman.draw(canvas);
        navigationButtons.draw(canvas);
    }

    /*
    implement onTouchEvent to handle user input
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_DOWN:
                //update UserInput only, update other game objects somewhere else
                userInput.updateUserInput(motionEvent.getX(), motionEvent.getY());
                break;
            case MotionEvent.ACTION_UP:
                userInput.updateUserInput(Float.MAX_VALUE, Float.MAX_VALUE);
                break;
        }
        return true;
    }

    //Constructor
    public PacmanGame(Context context, int x, int y) {
        // Super... calls the parent class
        // constructor of SurfaceView
        // provided by Android
        super(context);

        // Initialize these two members/fields
        // With the values passed in as parameters
        mScreenX = x;
        mScreenY = y;

        // Initialize the objects
        // ready for drawing with
        // getHolder is a method of SurfaceView
        mOurHolder = getHolder();
        mPaint = new Paint();

        //initialize the Arcade list
        arcades = new ArcadeList(context, mScreenX, mScreenY,
                R.raw.sample1);

        // Initialize the pacman and ghost
        pacman = new Pacman(context, mScreenX, mScreenY);
        pacman.initStartingPoint(arcades.getArcadeContainingPacman().getPacmanX_pix(),
                arcades.getArcadeContainingPacman().getPacmanY_pix());

        //FIXME
        userInput = new UserInput();

        //init Nav Buttons
        navigationButtons = new NavigationButtons(context, mScreenX, mScreenY);
    }
}