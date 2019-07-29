package com.example.pacman;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MapSelectionActivity extends Activity {
    private Intent intent;
    private int modeSelected;
    public static final String EXTRA_NUMBER = "com.example.pacman.EXTRA_NUMBER2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set the game in full screen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //We don't want the title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_map_selection);
        RelativeLayout rel = findViewById(R.id.layout);
        AnimationDrawable aniD = (AnimationDrawable) rel.getBackground();
        aniD.setEnterFadeDuration(1000);
        aniD.setExitFadeDuration(2500);
        aniD.start();
        intent = getIntent();
        modeSelected = intent.getIntExtra(WelcomeActivity.EXTRA_NUMBER, -1);

        gameOn();
    }

    public void gameOn(){
        TextView gaming = findViewById(R.id.gaming);
        gaming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMap();
            }
        });
    }

    public void goToMap() {
        intent = new Intent(this, PacmanActivity.class);
        System.out.println(modeSelected + "modeSelected --------------");
        intent.putExtra(EXTRA_NUMBER, modeSelected);
        startActivity(intent);
    }
}
