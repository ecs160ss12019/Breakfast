package com.example.pacman;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class PacmanActivity extends Activity {
    private PacmanGame mPacmanGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the game in full screen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Set the game in landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //We don't want the title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        mPacmanGame = new PacmanGame(this, size.x, size.y);
        setContentView(mPacmanGame);
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
}