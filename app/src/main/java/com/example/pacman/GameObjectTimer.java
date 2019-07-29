package com.example.pacman;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;

public class GameObjectTimer {
    final static int chaseTime = 20000;
    final static int scatterTime = 7000;
    final static int powerUp = 10000;

    public boolean timeUp;
    private CountDownTimer countDownTimer;

    public void setTimer(final long countDownTime) {
        this.timeUp = false;

        if(this.countDownTimer != null) this.countDownTimer.cancel();
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                countDownTimer = new CountDownTimer(countDownTime, 1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        timeUp = true;
                    }
                }.start();
            }
        });

    }

    public void cancelTimer() {
        this.countDownTimer.cancel();
    }

    public GameObjectTimer(long countDownTime) {
        setTimer(countDownTime);
    }

    public GameObjectTimer() {
    }
}
