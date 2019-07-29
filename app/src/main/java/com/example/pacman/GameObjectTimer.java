package com.example.pacman;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;

public class GameObjectTimer {
    final static int fraction = 1000000000;
    final static int chaseTime = 20;
    final static int scatterTime = 7;
    final static int powerUp = 10;

    private long startTime;
    private long currTime;
    private long countDownTime;

    public boolean timeUp;
    private CountDownTimer countDownTimer;

//    public void setTimer(final long countDownTime) {
//        this.timeUp = false;
//
//        if (this.countDownTimer != null) this.countDownTimer.cancel();
//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
//                countDownTimer = new CountDownTimer(countDownTime, 1000) {
//                    @Override
//                    public void onTick(long l) {
//
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        timeUp = true;
//                    }
//                }.start();
//            }
//        });
//
//    }

    public boolean isTimeUp() {
        this.currTime = System.nanoTime();
        return (currTime - startTime) / fraction > countDownTime;
    }

    public void setTimer(long countDownTime) {
        this.countDownTime = countDownTime;
        this.startTime = System.nanoTime();
    }

    public GameObjectTimer(long countDownTime) {
        setTimer(countDownTime);
    }

    public GameObjectTimer() {
        this.startTime = 0;
        this.currTime = 0;
    }
}
