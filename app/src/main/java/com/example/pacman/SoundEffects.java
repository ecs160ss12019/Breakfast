package com.example.pacman;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;

public class SoundEffects {
    MediaPlayer welcome, pacman_death, eat_ghost, siren;

    private SoundPool soundPool;
    private int pacman_chomp, eat_power;
    private AudioAttributes audioAttributes;
//
    public SoundEffects(Context context) {
        welcome = MediaPlayer.create(context, R.raw.welcome_music);
        pacman_death = MediaPlayer.create(context, R.raw.pacman_death);
        eat_ghost = MediaPlayer.create(context, R.raw.pacman_eatghost);
        siren = MediaPlayer.create(context, R.raw.edited_siren);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(1)
                    .build();
        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }
        pacman_chomp = soundPool.load(context, R.raw.edited_chomp, 1);
        eat_power = soundPool.load(context, R.raw.pacman_eatfruit, 1);
    }

    public void playPacmanChomp() {
        soundPool.play(pacman_chomp, 1, 1, 0, 0, 1);
    }

    public void playEatPower() {
        soundPool.play(eat_power, 1,1,0,0,1);
    }

    public void playSiren() {
        siren.start();
    }

    public void playEatGhost() {
        eat_ghost.start();
    }

    public void playPacmanDeath() {
        pacman_death.start();
    }

    public void playWelcome() {
        welcome.start();
    }

}
