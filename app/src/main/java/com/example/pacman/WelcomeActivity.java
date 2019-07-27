package com.example.pacman;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
    private String modeSelected;


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
        playGame();
    }

    public void playGame(){
        easyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modeSelected = hardBtn.getText().toString();
                System.out.println(modeSelected + "--------------------------------------------");
                openActivitivy2();
            }
        });
        normalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modeSelected = hardBtn.getText().toString();
                System.out.println(modeSelected + "--------------------------------------------");
                openActivitivy2();
            }
        });
        hardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modeSelected = hardBtn.getText().toString();
                System.out.println(modeSelected + "--------------------------------------------");
                openActivitivy2();
            }
        });
    }

    public void openActivitivy2(){
//        startActivity(new Intent(WelcomeActivity.this, MapSelectionActivity.class));
        Intent intent = new Intent(this, MapSelectionActivity.class);
        startActivity(intent);

    }

}
