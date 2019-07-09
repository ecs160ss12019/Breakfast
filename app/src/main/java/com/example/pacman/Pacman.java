package com.example.pacman;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Pacman implements GameObject{
    /*
    * create Pacman here
    */
    //coordinate
    private int x;
    private int y;

    //context of the game, used access Resource ptr
    private Context context;

    /*
    img for the pacman
    Note that this is the combination of pacman
    img in all directions
     */
    private Bitmap pacmanView;

    /*
    individual pacman view height/width
    this is crucial to centering the pacman
    on the coordinates
     */
    private int bitmapWidth;
    private int bitmapHeight;

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(pacmanView, (int)(x - (bitmapWidth/2)), (int)(y - (bitmapHeight/2)), null);
    }

    @Override
    public void updateStatus() {

    }

    public void updateStatus(int x, int y) {
        this.x = x;
        this.y = y;
    }
/*
    public Bitmap getCurrentView(){
        return new Bitmap();
    }
*/

    public Pacman(Context context) {
        this.context = context;

        //load pacman img from resource
        pacmanView = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman);

        //initialize the size to demanded
        bitmapWidth = (int)(pacmanView.getWidth() * 0.1);
        bitmapHeight = (int)(pacmanView.getHeight() * 0.1);
        //resize bitmap
        pacmanView = Bitmap.createScaledBitmap(pacmanView, bitmapWidth, bitmapHeight, true);
    }
}
