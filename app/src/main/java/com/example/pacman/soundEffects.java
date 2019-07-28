package com.example.pacman;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class soundEffects {
    private SoundPool soundPool;
    private int welcomeMusic;

    public soundEffects(Context context) {
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        welcomeMusic = soundPool.load(context, R.raw.welcome_music, 1);
    }


}
