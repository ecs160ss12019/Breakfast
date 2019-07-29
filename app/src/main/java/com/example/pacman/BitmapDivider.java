package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

/*
This class helps to split collection of bitmap
into a list of small views. Each src file for
a game character is stored as a collection of the
character facing each direction. Thus, cutting the
img apart is necessary.
 */
public class BitmapDivider {
    private Context context;

    public static ArrayList<Bitmap> splitAndResize(Bitmap bitmap,
                                            TwoTuple dimension, TwoTuple targetDimension) {
        ArrayList<Bitmap> views = new ArrayList<>();

        int numRow = dimension.x;
        int numCol = dimension.y;
        int cuttingWidth = (int)(bitmap.getWidth()/numCol);
        int cuttingHeight = (int)(bitmap.getHeight()/numRow);

        //cutting start
        for(int i = 0; i < numRow; i++) {
            for (int j = 0; j < numCol; j++) {
                Bitmap next = Bitmap.createBitmap(bitmap, j * cuttingWidth, i * cuttingHeight,
                        cuttingWidth, cuttingHeight);

                next = Bitmap.createScaledBitmap(next, targetDimension.x, targetDimension.y, true);
                views.add(next);
            }
        }
        return views;
    }

    public Bitmap loadBitmap(final int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    public BitmapDivider(final Context context) {
        this.context = context;
    }
}
