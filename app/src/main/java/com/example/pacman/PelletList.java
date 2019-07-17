package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import java.util.ArrayList;
import java.util.Random;

public class PelletList {
    private ArrayList<ArrayList<PelletCell>> pelletList;
    ArrayList<Arcade> arcades;

    private ArrayList<Bitmap> pelletViewList;
    private Context context;
    private int bitmapWidth;
    private int bitmapHeight;
    private int pwrBitmapWidth;
    private int pwrBitmapHeight;

    public void draw(Canvas canvas){
        for (int index = 0; index < arcades.size();++index){
            ArrayList<PelletCell> pelletCells = pelletList.get(index);
            for(int i = 0; i<pelletCells.size(); ++i) {
                TwoTuple posOnScreen = map2screen(pelletCells.get(i));
                int type = pelletCells.get(i).getType();
                if (type == 1) {
                    canvas.drawBitmap(pelletViewList.get(type),
                            posOnScreen.first() - (bitmapWidth / 2), posOnScreen.second() - (bitmapHeight / 2), null);
                } else {
                    canvas.drawBitmap(pelletViewList.get(type),
                            posOnScreen.first() - (pwrBitmapWidth / 2), posOnScreen.second() - (pwrBitmapHeight / 2), null);
                }
            }
        }
    }
    public PelletList(Context context, ArrayList<Arcade> arcades, TwoTuple screen) {
        this.arcades = arcades;
        ArrayList<ArrayList<PelletCell>> pelletList = new ArrayList<>();
        pelletViewList = new ArrayList<>();
        this.context = context;
        Bitmap pwrpelletView = BitmapFactory.decodeResource(context.getResources(), R.drawable.powerpellet);
        Bitmap pelletView = BitmapFactory.decodeResource(context.getResources(), R.drawable.pellet);
        int screenWidth = screen.first();
        int screenHeight = screen.second();

        double pwrPelletRatio = pwrpelletView.getWidth() / pwrpelletView.getHeight();
        double pelletRatio = pelletView.getWidth()/pelletView.getHeight();
        bitmapHeight = screenHeight / 45;
        bitmapWidth = (int)(pwrBitmapHeight * pelletRatio) ;
        pwrBitmapHeight = screenHeight / 30;
        pwrBitmapWidth = (int)(pwrBitmapHeight * pwrPelletRatio);
        pelletViewList.add(Bitmap.createScaledBitmap(pwrpelletView,
                pwrBitmapWidth , pwrBitmapHeight, true));
        pelletViewList.add(Bitmap.createScaledBitmap(pelletView,
                bitmapWidth , bitmapHeight, true));


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

        Random random = new Random();
        //Construct a matrix given the arcades with 0 and 1(pellet can be added).
        for (int index = 0; index < arcades.size(); ++index) {
            int numRow =  arcades.get(index).getNumRow();
            int numCol =  arcades.get(index).getNumCol();
            for (int i = 0; i < numRow; ++i) {
                ArrayList<PelletCell> newLine = new ArrayList<>();
                for (int j = 0; j < numCol; ++j) {
                    int p = random.nextInt(3);
                    if (arcades.get(index).getBlock(new TwoTuple(i, j)).getType() == 16 && p != 0) {

                        PelletCell pell = new PelletCell(i, j, p - 1, index);
                        newLine.add(pell);
                    }
                }
            }
        }
    }

    public TwoTuple map2screen(PelletCell pell) {
        int arcadeIndex = pell.getArcadeIndex();
        int x_pixel = arcades.get(arcadeIndex).xReference + pell.getY() * arcades.get(arcadeIndex).getBlockWidth();
        int y_pixel = arcades.get(arcadeIndex).yReference + pell.getX() * arcades.get(arcadeIndex).getBlockHeight();

        return new TwoTuple(x_pixel, y_pixel);
    }
}
