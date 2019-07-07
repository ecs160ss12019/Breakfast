package com.example.pacman;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class PacmanActivity extends Activity {
    private PacmanGame mPacmanGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
