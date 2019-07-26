package com.example.pacman;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class PacmanActivity extends Activity {
    private PacmanGame mPacmanGame;
    private boolean useGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //System.out.println("OnCreate Called");

        //Set the game in full screen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Set the game in landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //We don't want the title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        //FIXME
        mPacmanGame = new PacmanGame(this, size.x, size.y);
        //mPacmanGame = new PacmanGame(this, 2028, 1080);
        setContentView(mPacmanGame);

        Log.d("Debugging", "In onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // More code here later
        mPacmanGame.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // More code here later
        mPacmanGame.pause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // System.out.println("key pressed----------------" + event.getKeyCode());
        switch(event.getKeyCode()) {
            case 19: // up arrow
                // System.out.println("I pressed up------------");
                mPacmanGame.arrowKey = TwoTuple.UP;
                break;
            case 20: // down arrow
                // System.out.println("I pressed down------------");
                mPacmanGame.arrowKey = TwoTuple.DOWN;
                break;
            case 21: // left arrow
                //System.out.println("I pressed left------------");
                mPacmanGame.arrowKey = TwoTuple.LEFT;
                break;
            case 22: // right arrow
                //System.out.println("I pressed right------------");
                mPacmanGame.arrowKey = TwoTuple.RIGHT;
                break;
        }
        return true;
    }
}