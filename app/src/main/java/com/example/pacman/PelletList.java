package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;

public class PelletList {
    private ArrayList<ArrayList<PelletCell>> pelletList;

    private Bitmap pelletView;
    private Context context;
    private int bitmapWidth;
    private int bitmapHeight;


    public PelletList(Context context, ArcadeList arcades){
        ArrayList<ArrayList<PelletCell>> pelletList = new ArrayList<>();
        this.context = context;
        int numrows = 0; // retrieve from arcade
        int numcols = 0; // retrieve from arcade
        for(int i=0;i<numrows;i++){
            ArrayList<PelletCell> newLine = new ArrayList<>();
            for(int j=0;j<numcols;j++){
                PelletCell cell = new PelletCell(i,j,type);
                /*if(arcades.get(i).get(j).getType() == 16) {
                    ArcadeBlock newBlock = new ArcadeBlock(i, j, matrix.get(i).get(j));

                }*/
//                PelletCell cell = new PelletCell(i,j,);
            }
        }




    }

   /* public TwoTuple map2screen(int x, int y){
        TwoTuple cor = new TwoTuple(
                x*bitmapHeight+arcades.xReference,
                num*bitmapWidth+arcades.yReference);
        return cor;*/

    }


