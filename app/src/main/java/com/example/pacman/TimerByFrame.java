package com.example.pacman;

public class TimerByFrame {
    final static int chaseTime = 200;
    final static int scatterTime = 80;
    final static int powerUp = 300;
    final static int returnTime = 300;

    public int timer;
    public boolean timeUp;
    public int countDownTime;


    public void updateTimer() {
        this.timer --;

        if (timer == 0) {
            timeUp = true;
        }
    }

    public void setTimer(int countDownTime) {
        this.timer = countDownTime;
        this.countDownTime = countDownTime;
        this.timeUp = false;
    }

    public TimerByFrame(int countDownTime) {
        setTimer(countDownTime);
    }

    public TimerByFrame() {
    }
}
