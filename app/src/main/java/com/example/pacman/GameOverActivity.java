package com.example.pacman;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class GameOverActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("GameOverActivity onCreate");
        super.onCreate(savedInstanceState);
        //Set the game in full screen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gameover);
//        mainMenu();
    }
//    void mainMenu(){
//        Button mainButton = (Button) findViewById(R.id.playButton);
//        mainButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(GameOverActivity.this, MapSelectionActivity.class));
//            }
//        });
//    }
}
