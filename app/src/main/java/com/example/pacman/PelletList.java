package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;

public class PelletList {
    private ArrayList<PelletCell> pelletList;
    private ArrayList<PowerPellet> pwrpelletList;
    private Bitmap pelletView;
    private Context context;
    private int bitmapWidth;
    private int bitmapHeight;


    //context of the game, used access Resource ptr
//    private Context context;

    public PelletList(Context context, ArcadeList arcades){
        this.context = context;

    }

    /*public TwoTuple map2screen(int num){
        TwoTuple cor = new TwoTuple(
                num*bitmapHeight+Arcade.xReference,
                num*bitmapWidth+Arcade.yReference);
        return cor;
*/
    }


