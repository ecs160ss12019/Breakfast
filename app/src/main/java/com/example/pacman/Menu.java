package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Menu {
    Bitmap pauseImg;
    private int buttonWidth;
    private int buttonHeight;
    private int numberHorizontalPixels;

    public void draw(Canvas canvas) {
        canvas.drawBitmap(pauseImg, 1550, 50, null);
    }

    public Menu(Context context, int screenWidth, int screenHeight) {

        pauseImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.pause);
        numberHorizontalPixels = screenWidth;
        //resize
        int optimalWidth = screenHeight / 8;
        int optimalHeight = optimalWidth;
        pauseImg = Bitmap.createScaledBitmap(pauseImg, optimalWidth, optimalHeight, true);
        buttonHeight = pauseImg.getHeight();
        buttonWidth = pauseImg.getWidth();
    }
}
