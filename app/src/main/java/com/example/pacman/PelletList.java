package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;

public class PelletList {
    private ArrayList<ArrayList<PelletCell>> pelletList;
    ArrayList<Arcade> arcades;

    private Bitmap pelletView;
    private Context context;
    private int bitmapWidth;
    private int bitmapHeight;


    public PelletList(Context context, ArrayList<Arcade> arcades) {
        this.arcades = arcades;
        ArrayList<ArrayList<PelletCell>> pelletList = new ArrayList<>();
        this.context = context;
//
        /*
        for(int i=0;i<arcades.size();i++){
            ArrayList<PelletCell> newLine = new ArrayList<>();
            for(int j=0;j<arcades.getDimensionOfArcade(i).second();j++){
                PelletCell cell = new PelletCell(i,j,type);
                if(arcades.get(i).get(j).getType() == 16) {
                    ArcadeBlock newBlock = new ArcadeBlock(i, j, matrix.get(i).get(j));

                }
//                PelletCell cell = new PelletCell(i,j,);
            }
        }
        */

        //Construct a matrix given the arcades with 0 and 1(pellet can be added).
        for (int index = 0; index < arcades.size(); ++index) {
            int numRow =  arcades.get(index).getNumRow();
            int numCol =  arcades.get(index).getNumCol();
            for (int i = 0; i < numRow; ++i) {
                ArrayList<PelletCell> newLine = new ArrayList<>();
                for (int j = 0; j < numCol; ++j) {
                    if (arcades.get(index).getBlock(new TwoTuple(i, j)).getType() == 16) {
                        PelletCell pell = new PelletCell(i, j, 1, index);
                        newLine.add(pell);
                    } else {
                        PelletCell pell = new PelletCell(i, j, 0, index);
                        newLine.add(pell);
                    }
                }
            }
        }
    }

    public TwoTuple map2screen(PelletCell pell) {
        int arcadeIndex = pell.getArcadeIndex();
        int x_pixel = arcades.get(arcadeIndex).xReference + pell.getX() * arcades.get(arcadeIndex).getBlockHeight();
        int y_pixel = arcades.get(arcadeIndex).yReference + pell.getY() * arcades.get(arcadeIndex).getBlockWidth();
        return new TwoTuple(x_pixel, y_pixel);

    }
}
