package com.example.pacman;

import android.graphics.Bitmap;
import java.util.ArrayList;

/*
This class helps to split collection of bitmap
into a list of small bitmaps. Each src file for
a game character is stored as a collection of the
character facing each direction. Thus, cutting the
img apart is necessary.
 */
public class BitmapDivider {
    private ArrayList<Bitmap> bitmaps;
    private Bitmap bitmap;
    private int originalFileWidth;
    private int originalFileHeight;
    private int cuttingWidth;
    private int cuttingHeight;

    public ArrayList<Bitmap> split(int numRow, int numCol) {
        cuttingWidth = (int)(originalFileWidth/numCol);
        cuttingHeight = (int)(originalFileHeight/numRow);

        //cutting start
        for(int i = 0; i < numRow; i++) {
            for (int j = 0; j < numCol; j++) {
                Bitmap next = Bitmap.createBitmap(bitmap, j * cuttingWidth, i * cuttingHeight,
                        cuttingWidth, cuttingHeight);
                bitmaps.add(next);
            }
        }

        return bitmaps;
    }

    public BitmapDivider(Bitmap bitmap) {
        this.bitmap = bitmap;
        bitmaps = new ArrayList<>();
        originalFileWidth = bitmap.getWidth();
        originalFileHeight = bitmap.getHeight();
    }
}
