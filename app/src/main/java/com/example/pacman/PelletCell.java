package com.example.pacman;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

public class PelletCell implements Pellet{
    private Bitmap pelletView;
    private ArrayList<Bitmap> pelletViewList;
    private boolean inuse;
    private int pelletX;
    private int pelletY;

    public PelletCell(){

    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public void updateStatus(long fps) {

    }
}
