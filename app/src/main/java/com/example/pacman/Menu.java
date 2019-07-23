package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.ArrayList;

public class Menu {
    Bitmap pauseImg;
    private int buttonWidth;
    private int buttonHeight;
    private int numberHorizontalPixels;
    private int numberVerticalPixels;
    private boolean buttonPressed;

    public boolean getButtonPressed() {
        return buttonPressed;
    }

    public void setButtonPressed() {
        buttonPressed = false;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(pauseImg, numberHorizontalPixels - (buttonWidth+20)*3,
                80, null);
    }

    public int check(UserInput userInput) {
        buttonPressed = false;
        double x = userInput.getX();
        double y = userInput.getY();
        // ugly but works!
        if (x > (numberHorizontalPixels-(buttonWidth+20)*3)
                && x < (numberHorizontalPixels-(buttonWidth+20)*3)+buttonWidth
                && y > 80 && y < 80+buttonHeight){
            buttonPressed = true;
            return 0;
        }
        return -1;
    }

    public Menu(Context context, int screenWidth, int screenHeight) {

        pauseImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.pause);
        numberHorizontalPixels = screenWidth;
        numberVerticalPixels = screenHeight;
        //resize
        int optimalWidth = screenHeight / 8;
        int optimalHeight = optimalWidth;
        pauseImg = Bitmap.createScaledBitmap(pauseImg, optimalWidth, optimalHeight, true);
        buttonHeight = pauseImg.getHeight(); // 135
        buttonWidth = pauseImg.getWidth(); // 135


    }
}
