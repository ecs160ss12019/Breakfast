package com.example.pacman;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class SoundEffects {
    private SoundPool soundPool;
    private int welcomeMusic;
    private AudioAttributes audioAttributes;

    public SoundEffects(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(2)
                    .build();
        } else {
            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        }
        welcomeMusic = soundPool.load(context, R.raw.welcome_music, 1);
    }

    public void playWelcomeMusic() {
        soundPool.play(welcomeMusic, 1, 1, 0, 0, 1);
    }

}
