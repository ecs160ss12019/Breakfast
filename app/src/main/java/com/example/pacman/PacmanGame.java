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
    private TwoTuple mScreen;

    // Here is the Thread and two control variables
    private Thread mGameThread = null;
    // This volatile variable can be accessed
    // from inside and outside the thread
    private volatile boolean mPlaying;
    private boolean mPaused = false;

    //our UserInput object
    private UserInput userInput;

    private GameMode gameMode;

    //Our pacman!
    private Pacman pacman;
    private GhostList ghosts;
    private Cake cake;
    //Our Pellets
    private PelletList pelletList;
    //Our Arcade List
    private ArcadeList arcades;

    private Collision collision;

    //Our Score system
    private PointSystem score;

    //Our Navigation Buttons!
    private NavigationButtons navigationButtons;

    //Our CollisionDestructor
    private CollisionDetector collisionDetector;

    //Our analyzer to get the vector field
    private ArcadeAnalyzer arcadeAnalyzer;

    //consoleReader for debugging use
    //ConsoleReader consoleReader;


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
            //long frameStartTime = System.currentTimeMillis();
            long frameStartTime = System.nanoTime();

            if(!mPaused) {
                /*
                We want to prevent from updating any
                motion related stuff if the fps is -1.
                Other wise there will be a moving speed
                overflow.
                 */
                updateGame();
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
                    //e.printStackTrace();
                } finally {
                    if (mCanvas != null) {
                        try {
                            mOurHolder.unlockCanvasAndPost(mCanvas);
                        } catch (Exception e) {
                            //e.printStackTrace();
                        }
                    }
                }
            }

            // How long did this frame/loop take?
            // Store the answer in timeThisFrame
            //long timeThisFrame = System.currentTimeMillis() - frameStartTime;
            long timeThisFrame = (System.nanoTime() - frameStartTime) / 1000000;


            // Make sure timeThisFrame is at least 1 millisecond
            // because accidentally dividing by zero crashes the game
            if (timeThisFrame > 0) {
                // Store the current frame rate in mFPS
                // ready to pass to the update methods of
                // mBat and mBall next frame/loop
                mFPS = MILLIS_IN_SECOND / timeThisFrame;
            }
            System.out.println("fps: " + mFPS);
        }
    }

    /*
    We do all global update events here,
    this will be continuously called while
    the thread is running
     */
    public void updateGame() {
        final int direction = navigationButtons.checkAndUpdate(userInput);

        /*
        if player touched or is continuous touching
        updated pacman position and etc.
         */

        /*
        1. If we want un-continuous movement: only move if pressed,
        we use this if condition
        if(!(direction < 0)

        2. If we want continuous movement: always moving once pressed,
        we use this if condition
        if(navigationButtons.initialInputFlag)
         */

        //FIXME
        //if(navigationButtons.initialInputFlag) {

        //System.out.println("Pacman Location: " + pacman.getCenterX() + " " + pacman.getCenterY());


        //FIXME
        /*
        concurrency may save us for now by boosting
        the speed. However, we must fix the bug and
        implement a better algorithm.
         */

        collision.recordRunnersPosition();

//        Thread pacManThread = new Thread(new Runnable(){
//            @Override
//            public void run() {
//                pacman.updateMovementStatus(direction, mFPS);
//            }
//        });
//
//        Thread ghostsThread = new Thread(new Runnable(){
//            @Override
//            public void run() {
//                ghosts.updateMovementStatus(mFPS, arcades.getArcadeContainingPacman());
//            }
//        });
//
//        Thread cakeThread = new Thread(new Runnable(){
//            @Override
//            public void run() {
//                cake.updateMovementStatus(mFPS, arcades.getArcadeContainingPacman());
//            }
//        });
//
//        pacManThread.start();
//        ghostsThread.start();
//        cakeThread.start();
//
//        try {
//            System.out.println("Waiting for threads to finish.");
//            pacManThread.join();
//            ghostsThread.join();
//            cakeThread.join();
//
//        } catch (InterruptedException e) {
//            System.out.println("Main thread Interrupted");
//        }



        pacman.updateMovementStatus(direction, mFPS);
        ghosts.updateMovementStatus(mFPS, arcades.getArcadeContainingPacman());
        cake.updateMovementStatus(mFPS, arcades.getArcadeContainingPacman());

        collision.updateRunnersPosition();
        collision.notifyObservers();
        Thread pacManThread = new Thread(new Runnable(){
            @Override
            public void run() {
                pacman.updateMovementStatus(direction, mFPS);
            }
        });

//        Thread ghostsThread = new Thread(new Runnable(){
//            @Override
//            public void run() {
//                ghosts.updateMovementStatus(mFPS, arcades.getArcadeContainingPacman());
//            }
//        });
//
//        Thread cakeThread = new Thread(new Runnable(){
//            @Override
//            public void run() {
//                cake.updateMovementStatus(mFPS, arcades.getArcadeContainingPacman());
//            }
//        });
//
        pacManThread.start();
//        ghostsThread.start();
//        cakeThread.start();

//        pacman.updateMovementStatus(direction, mFPS);
//        ghosts.updateMovementStatus(mFPS, arcades.getArcadeContainingPacman());
//        cake.updateMovementStatus(mFPS, arcades.getArcadeContainingPacman());
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
        pelletList.draw(canvas);
        pacman.draw(canvas);
        //ghosts.draw(canvas);
        //cake.draw(canvas);
        navigationButtons.draw(canvas);
    }
    /*
    implement onTouchEvent to handle user input
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            //case MotionEvent.ACTION_MOVE:
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
        mScreen = new TwoTuple(x, y);

        // Initialize the objects
        // ready for drawing with
        // getHolder is a method of SurfaceView
        mOurHolder = getHolder();
        mPaint = new Paint();

        //Init fps to -1 so that we will know if the canvas is not ready
        mFPS = -1;

        gameMode = new GameMode(0, mScreen.x);
        /* implement front page view (something like welcome to breakfast's Pac-Man game) */

        //initialize the Arcade list
        arcades = new ArcadeList(context, mScreen.x, mScreen.y,
                R.raw.sample2);

        //TODO for testing purpose, now we focus on 1 arcade
        arcadeAnalyzer = new ArcadeAnalyzer(arcades.getArcadeContainingPacman());
        arcadeAnalyzer.run();


        collision = new Collision(arcades.getArcadeContainingPacman());

        pelletList = new PelletList(context, arcades.getArcades(), new TwoTuple(mScreen.x, mScreen.y), collision);

        // Initialize the pacman and ghost
//        pacman = new Pacman(context, mScreenX, mScreenY, arcades.getOptimalPacmanSize(),
//                arcades.getArcadeContainingPacman(), gameMode.getPacmanSpeed());
        TwoTuple pacmanInitPos = new TwoTuple(arcades.getArcadeContainingPacman().pacmanPosition);
        pacman = new Pacman(context, mScreen, arcades.getArcadeContainingPacman(), pacmanInitPos,
                arcadeAnalyzer, gameMode.getPacmanSpeed(), collision);

        ghosts = new GhostList(context, mScreen.x, mScreen.y, arcades.getArcadeContainingPacman(), arcadeAnalyzer,
                gameMode.getGhostsSpeed(), collision);
//
        cake = new Cake(context, mScreen.x, mScreen.y, arcades.getArcadeContainingPacman(),
                arcadeAnalyzer, gameMode.getGhostsSpeed(), collision);
//
//        collisionDetector = new CollisionDetector();

        //userInput handler
        userInput = new UserInput();

        //init Nav Buttons
        navigationButtons = new NavigationButtons(context, mScreen.x, mScreen.y);

        //init with system env variable
        //consoleReader = new ConsoleReader(System.console());
    }
}
