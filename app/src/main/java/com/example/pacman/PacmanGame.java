// Cited: https://github.com/PacktPublishing/Learning-Java-by-Building-Android-Games-Second-Edition/blob/master/Chapter10/java/PongGame.java

package com.example.pacman;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.Intent;
import android.widget.Button;

import java.util.ArrayList;

class PacmanGame extends SurfaceView implements Runnable {
    // Are we debugging?
    private final boolean DEBUGGING = true;

    // These objects are needed to do the drawing
    private SurfaceHolder mOurHolder;
    private Canvas mCanvas;
    private Paint mPaint;

    // when the pause button is pressed, the menu popup activity will popup
    private Button menuBtn;

    //create SoundEffects class object
    private SoundEffects sound;

    private Context context;
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
    private boolean mPlaying;
    private boolean mPaused;

    //our UserInput object
    private UserInput userInput;

    private GameMode gameMode;

    //Our Arcade List
    private ArcadeList arcades;

    //Our Score system
    private PointSystem score;
    private Records records;

    private int numberHorizontalPixels;
    private int numberVerticalPixels;
    private String modeSelected;

    //Our Navigation Buttons!
    private NavigationButtons navigationButtons;

    private Menu menu;

    //Our GameObjectCollections
    private ArrayList<GameObjectCollection> gameObjectCollections;

    int arrowKey = -1;

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
                    e.printStackTrace();
                } finally {
                    if (mCanvas != null) {
                        try {
                            mOurHolder.unlockCanvasAndPost(mCanvas);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {

                updateGame();
                //this might not be null in the future
                mCanvas = null;

                try {
                    //lock canvas to edit pixels
                    mCanvas = mOurHolder.lockCanvas();
                    synchronized (mOurHolder) {
                        draw(mCanvas);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (mCanvas != null) {
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
        }
    }

    /*
    We do all global update events here,
    this will be continuously called while
    the thread is running
     */
    public void updateGame() {


        if (!mPaused) {
            final int direction; // check if user pressed touch button on screen; if not, check if user entered arrow key on keyboard (for testing)
            if(navigationButtons.checkAndUpdate(userInput) != -1) direction = navigationButtons.checkAndUpdate(userInput);
            else {
                direction = arrowKey;
                arrowKey = -1;
            }

            for (GameObjectCollection gameObjectCollection : gameObjectCollections) {
                gameObjectCollection.update(direction, mFPS,score);
            }
            if(Pacman.totalLives <= 0) {
                mPlaying = false;
                //stop();
            }
        }
    }
    public void stop() {
        pause();
        // gameover, go to GameOverActivity
        Intent intent = new Intent(context, GameOverActivity.class);
        System.out.println("Start GameOverActivity");
        context.startActivity(intent);
    }

    // This method is called by PacmanActivity
    // when the player quits the game
    public void pause() {

        // Set mPlaying to false
        // Stopping the thread isn't
        // always instant

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
        //mPaused = false;
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
        System.out.println("drawing");
        // Fill the screen with a solid color
        mCanvas.drawColor(Color.argb
                (255, 255, 255, 255));

        // score system:
        Typeface plain = Typeface.createFromAsset(getContext().getAssets(), "fonts/myFont.ttf");
        Paint paint = new Paint();
        paint.setTextSize(numberHorizontalPixels/52);
        paint.setTypeface(plain);
        mCanvas.drawText("Score: "+ score.getScore(), 50, (numberHorizontalPixels/40)*3, paint);
        mCanvas.drawText("Speed: "+ modeSelected, 50, (numberHorizontalPixels/40)*4, paint);

        mCanvas.drawText("Fps: "+ mFPS, 50, (numberHorizontalPixels/40)*6, paint);

        gameObjectCollections.get(0).draw(canvas);
        navigationButtons.draw(canvas);
        if (!mPaused) {
            menu.drawPause(canvas);
        } else {
            mCanvas.drawColor(Color.argb
                    (255, 222, 184, 184));
            paint.setColor(Color.argb(255, 255, 255, 255));
            paint.setTextSize(numberHorizontalPixels/30);
            canvas.drawText("Paused!", (numberHorizontalPixels/2)-100,
                    numberVerticalPixels/2, paint);
            canvas.drawText("click on the play button to resume", (numberHorizontalPixels/20),
                    (numberVerticalPixels/2)+100, paint);
            menu.drawPlay(canvas);
        }
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
                if (menu.check(userInput) == 0) { mPaused = mPaused ? false : true; }
                break;
            case MotionEvent.ACTION_UP:
                userInput.updateUserInput(Float.MAX_VALUE, Float.MAX_VALUE);
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent keyEvent) {
        System.out.println("----------I pressed key------------");
        switch(keyCode) {
            case 37: // left arrow
                System.out.println("----------I pressed left------------");
                break;
        }
        return true;
    }

    //Constructor
    public PacmanGame(Context context, int x, int y, int inputLevel) {
        // Super... calls the parent class
        // constructor of SurfaceView
        // provided by Android
        super(context);
        //FIXME
        Pacman.totalLives = 3;
        this.context = context;

        numberHorizontalPixels = x;
        numberVerticalPixels = y;
        // initialize the sound class
        sound = new SoundEffects(getContext());
        score = new PointSystem();

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

        mPlaying = true;
        mPaused = false;

        gameMode = new GameMode(inputLevel, mScreen.x);
        switch (gameMode.getDisplayMode()){
            case 0:
                modeSelected = "Easy";
                break;
            case 1:
                modeSelected = "Normal";
                break;
            case 2:
                modeSelected = "Hard";
                break;
        }

        arcades = new ArcadeList(context, mScreen, R.raw.sample2);

        gameObjectCollections = new ArrayList<>();
        gameObjectCollections.add(new GameObjectCollection(context, mScreen,
                arcades.getArcadeContainingPacman(), gameMode, sound));

        //userInput handler
        userInput = new UserInput();

        // init the menu button
        menu = new Menu(context, mScreen.x, mScreen.y);

        //init Nav Buttons
        navigationButtons = new NavigationButtons(context, mScreen.x, mScreen.y);
        //records = new Records(context);
        //records.printRecord();
    }
}
