package com.example.pacman;

import android.app.Activity;
import android.os.CountDownTimer;

import java.util.Timer;
import java.util.TimerTask;

public class GameObjectTimer {
    final static int chaseTime = 20000;
    final static int scatterTime = 7000;
    final static int powerUp = 10000;

    public boolean timeUp;
    private CountDownTimer countDownTimer;

    public void setTimer(long countDownTime) {
        this.timeUp = false;

        this.countDownTimer = new CountDownTimer(countDownTime, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                timeUp = true;
            }
        }.start();
    }

    public GameObjectTimer(long countDownTime) {
        setTimer(countDownTime);
    }

    public GameObjectTimer() {
    }
}
