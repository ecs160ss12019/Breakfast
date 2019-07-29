package com.example.pacman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class GameOverActivity extends Activity {

    private Button replayBtn;
    private Button mapBtn;
    private Button homeBtn;
    private final int easy = 0;
    private final int normal = 1;
    private final int hard = 2;
    private String modeSelectedTmp;
    private int modeSelected;
    public static final String EXTRA_NUMBER = "com.example.pacman.EXTRA_NUMBER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set the game in full screen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //We don't want the title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gameover);
        replayBtn = (Button) findViewById(R.id.replayBtn);
        mapBtn = (Button) findViewById(R.id.mapBtn);
        homeBtn = (Button) findViewById(R.id.homeBtn);
        //modeSelected = -1;
        //modeSelectedTmp = "";
        playGame();
    }
    public void playGame(){
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modeSelectedTmp = replayBtn.getText().toString();
                System.out.println(modeSelectedTmp + "--------------------------------------------");
                replay();
            }
        });
        replayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modeSelectedTmp = mapBtn.getText().toString();
                System.out.println(modeSelectedTmp + "--------------------------------------------");
                mapSelect();
            }
        });
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modeSelectedTmp = homeBtn.getText().toString();
                System.out.println(modeSelectedTmp + "--------------------------------------------");
                homeActivity();
            }
        });
    }

    public void replay() {
        checkMode();
        Intent intent = new Intent(this, MapSelectionActivity.class);
        intent.putExtra(EXTRA_NUMBER, modeSelected);
        startActivity(intent);

    }

    public void mapSelect() {
        checkMode();
        Intent intent = new Intent(this, MapSelectionActivity.class);
        intent.putExtra(EXTRA_NUMBER, modeSelected);
        startActivity(intent);

    }
    public void homeActivity() {
        checkMode();
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.putExtra(EXTRA_NUMBER, modeSelected);
        startActivity(intent);
    }


    public void checkMode() {
        switch (modeSelectedTmp){
            case "Replay":
                modeSelected = easy;
                break;
            case "Home":
                modeSelected = normal;
                break;
            case "Select Different Map":
                modeSelected = hard;
                break;
        }
    }


}
