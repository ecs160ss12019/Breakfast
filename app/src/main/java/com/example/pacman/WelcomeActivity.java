package com.example.pacman;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class WelcomeActivity extends Activity {
    private Button normalBtn;
    private Button easyBtn;
    private Button hardBtn;
    private final int easy = 0;
    private final int normal = 1;
    private final int hard = 2;
    private String modeSelectedTmp;
    private int modeSelected;
    public static final String EXTRA_NUMBER = "com.example.pacman.EXTRA_NUMBER";
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set the game in full screen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //We don't want the title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        normalBtn = (Button) findViewById(R.id.normalBtn);
        easyBtn = (Button) findViewById(R.id.easyBtn);
        hardBtn = (Button) findViewById(R.id.hardBtn);
        modeSelected = -1;
        modeSelectedTmp = "";
        mediaPlayer = MediaPlayer.create(this, R.raw.welcome_music);
        playGame();
    }

    public void playGame(){
        mediaPlayer.start();
        easyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modeSelectedTmp = easyBtn.getText().toString();
                System.out.println(modeSelectedTmp + "--------------------------------------------");
                openActivitivy2();
            }
        });
        normalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modeSelectedTmp = normalBtn.getText().toString();
                System.out.println(modeSelectedTmp + "--------------------------------------------");
                openActivitivy2();
            }
        });
        hardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modeSelectedTmp = hardBtn.getText().toString();
                System.out.println(modeSelectedTmp + "--------------------------------------------");
                openActivitivy2();
            }
        });
    }

    public void openActivitivy2() {
//        startActivity(new Intent(WelcomeActivity.this, MapSelectionActivity.class));
        checkMode();
        Intent intent = new Intent(this, MapSelectionActivity.class);
        intent.putExtra(EXTRA_NUMBER, modeSelected);
        startActivity(intent);
    }

    public void checkMode() {
        switch (modeSelectedTmp){
            case "Easy":
                modeSelected = easy;
                break;
            case "Normal":
                modeSelected = normal;
                break;
            case "Hard":
                modeSelected = hard;
                break;
        }
    }

}
