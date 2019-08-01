package com.example.pacman;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;

public class SoundEffects {
    // use MediaPlayer for static music (the ones that have to be
    // finished playing before playing again)
    MediaPlayer welcome, pacman_death, eat_ghost, siren, waze;

    // use SoundPool for dynamic music (the ones that can restart
    // playing before the music finishes)
    private SoundPool soundPool;
    private int pacman_chomp, eat_power;
    private AudioAttributes audioAttributes;
//
    public SoundEffects(Context context) {
        welcome = MediaPlayer.create(context, R.raw.welcome_music);
        pacman_death = MediaPlayer.create(context, R.raw.pacman_death);
        eat_ghost = MediaPlayer.create(context, R.raw.pacman_eatghost);
        siren = MediaPlayer.create(context, R.raw.my_siren);
        waze = MediaPlayer.create(context, R.raw.edited_waze);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // new way of setting SoundPool
            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(1)
                    .build();
        } else {
            // this is the old way of setting SoundPool (cannot be used anymore)
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }
        pacman_chomp = soundPool.load(context, R.raw.edited_chomp, 1);
        eat_power = soundPool.load(context, R.raw.pacman_eatfruit, 1);
    }

    public void playPacmanChomp() { soundPool.play(pacman_chomp, 1, 1, 0, 0, 1); }

    public void playEatPower() {
        soundPool.play(eat_power, 1,1,0,0,1);
    }

    public void playSiren() {
        siren.start();
    }

    public void playWaze() { waze.start(); }

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
