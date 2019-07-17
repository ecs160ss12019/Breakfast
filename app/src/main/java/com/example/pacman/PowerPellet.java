package com.example.pacman;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Pair;

public class PowerPellet extends PelletCell{
    public PowerPellet(int pelletX, int pelletY,int arcadeindex) {
        super(pelletX, pelletY,1, arcadeindex );
    }
}
