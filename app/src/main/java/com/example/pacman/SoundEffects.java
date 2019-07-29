package com.example.pacman;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;

public class SoundEffects {
    MediaPlayer chomp;

//    private SoundPool soundPool;
//    private int pacman_chomp;
//    private AudioAttributes audioAttributes;
//
    public SoundEffects(Context context) {
        chomp = MediaPlayer.create(context, R.raw.edited_chomp);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            audioAttributes = new AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_GAME)
//                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                    .build();
//            soundPool = new SoundPool.Builder()
//                    .setAudioAttributes(audioAttributes)
//                    .setMaxStreams(2)
//                    .build();
//        } else {
//            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
//        }
//        pacman_chomp = soundPool.load(context, R.raw.pacman_chomp, 1);
    }

    public void playPacmanChomp() {
        chomp.start();
//        soundPool.play(pacman_chomp, 1, 1, 0, 0, 1);
    }

}
