package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

public class NavigationButtons {
    //button imgs
    /*
    final int LEFT = 0;
    final int RIGHT = 1;
    final int UP = 2;
    final int DOWN = 3;
     */
    private ArrayList<Bitmap> buttons;
    private ArrayList<Bitmap> pressedButtons;

    /*
    This flag is used to check if the user
    have ever input anything. Once there is
    a input, this will be set to true for the
    rest of the lifecycle
     */
    public boolean initialInputFlag;

    private boolean[] buttonPressed;

    final int LEFT = 0;
    final int RIGHT = 1;
    final int UP = 2;
    final int DOWN = 3;

    private int buttonWidth;
    private int buttonHeight;

    //location of buttons, in pixel
    private ArrayList<Pair<Double, Double>> buttonsXY;

    //range xMax, xMin, yMax, yMin
    private ArrayList<ArrayList<Double>> buttonRange;

    public int checkAndUpdate(UserInput userInput) {
        this.buttonPressed = new boolean[]{false, false, false, false};

        for(int i = 0; i < 4; i++) {
            if(check(userInput, buttonRange.get(i))) {
                /*
                once there is a touch on any button, set initialInputFlag to true.
                This variable will never be modified again.
                 */
                initialInputFlag = true;

                buttonPressed[i] = true;
                return i;
            }
        }

        //no movement
        return -1;
    }

    private boolean check(UserInput userInput, ArrayList<Double> list) {
        double x = userInput.getX();
        double y = userInput.getY();

        double xMax = list.get(0);
        double xMin = list.get(1);
        double yMax = list.get(2);
        double yMin = list.get(3);

        return x > xMin && x < xMax && y > yMin && y < yMax;
    }

    public void draw(Canvas canvas) {
        for (int i = 0; i < 4; i++) {
            Bitmap bitmap = buttonPressed[i] ? pressedButtons.get(i) : buttons.get(i);
            int x = buttonsXY.get(i).first.intValue() - buttonWidth / 2;
            int y = buttonsXY.get(i).second.intValue() - buttonHeight / 2;
            canvas.drawBitmap(bitmap, x, y, null);
        }
    }

    //Constructor
    public NavigationButtons(Context context, int screenWidth, int screenHeight) {
        initialInputFlag = false;

        //retrieve the collection view files
        Bitmap arrowsImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrows);
        Bitmap pressedArrowsImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.pressed_arrows);

        //collection view img info, currently 2 * 2
        int numImgRow = 2;
        int numImgCol = 2;

        //resize
        int optimalWidth = (int)(screenHeight / 8 * 1.7);
        int optimalHeight = optimalWidth;
        arrowsImg = Bitmap.createScaledBitmap(arrowsImg, optimalWidth, optimalHeight, true);
        pressedArrowsImg = Bitmap.createScaledBitmap(pressedArrowsImg, optimalWidth, optimalHeight, true);

        //cut
//        BitmapDivider divider = new BitmapDivider(arrowsImg);
//        this.buttons = divider.split(numImgRow, numImgCol);
//
//        divider = new BitmapDivider(pressedArrowsImg);
//        this.pressedButtons = divider.split(numImgRow, numImgCol);

        this.buttons = BitmapDivider.splitAndResize(arrowsImg, new TwoTuple(numImgRow, numImgCol), new
                TwoTuple(optimalWidth, optimalHeight));
        this.pressedButtons = BitmapDivider.splitAndResize(pressedArrowsImg, new TwoTuple(numImgRow, numImgCol), new
                TwoTuple(optimalWidth, optimalHeight));

        //get width, height
        this.buttonWidth = buttons.get(0).getWidth();
        this.buttonHeight = buttons.get(0).getHeight();

        /*
        init Arrow Location on the screen.
        Like this
        =======================
        |              UP     |
        |          LEFT  RIGHT|
        |             DOWN    |
        =======================

        centerOfButtonsX = screenWidth - buttonWidth * 1.25
        centerOfButtonsY = screenHeight / 2
         */
        double gap = 0.25;
        double centerOfButtonsX = screenWidth - buttonWidth * (gap + 1);
        double centerOfButtonsY = screenHeight / 2;

        buttonsXY = new ArrayList<>();

        //LEFT
        this.buttonsXY.add(new Pair<Double, Double>(centerOfButtonsX - (gap + 0.5) * buttonWidth,
                centerOfButtonsY));

        //RIGHT
        this.buttonsXY.add(new Pair<Double, Double>(centerOfButtonsX + (gap + 0.5) * buttonWidth,
                centerOfButtonsY));

        //UP
        this.buttonsXY.add(new Pair<Double, Double>(centerOfButtonsX,
                centerOfButtonsY - (gap + 0.5) * buttonHeight));

        //DOWN
        this.buttonsXY.add(new Pair<Double, Double>(centerOfButtonsX,
                centerOfButtonsY + (gap + 0.5) * buttonHeight));

        //None of the buttons are pressed now
        this.buttonPressed = new boolean[]{false, false, false, false};

        /*
        ButtonRange
        =======================
        |              UP     |
        |          LEFT  RIGHT|
        |             DOWN    |
        =======================
         */
        buttonRange = new ArrayList<>();

        double farest = gap + 1;
        double half = 0.5;
        //range
        //      yMin
        // xMax,    xMin,
        //      yMax
        ArrayList<Double> rangeUp = new ArrayList<>();
        ArrayList<Double> rangeDown = new ArrayList<>();
        ArrayList<Double> rangeLeft = new ArrayList<>();
        ArrayList<Double> rangeRight = new ArrayList<>();

        //LEFT
        rangeLeft.add(centerOfButtonsX - gap * buttonWidth);
        rangeLeft.add(centerOfButtonsX - farest * buttonWidth);
        rangeLeft.add(centerOfButtonsY + half * buttonHeight);
        rangeLeft.add(centerOfButtonsY - half * buttonHeight);

        //RIGHT
        rangeRight.add(centerOfButtonsX + farest * buttonWidth);
        rangeRight.add(centerOfButtonsX + gap * buttonWidth);
        rangeRight.add(centerOfButtonsY + half * buttonHeight);
        rangeRight.add(centerOfButtonsY - half * buttonHeight);

        //UP
        rangeUp.add(centerOfButtonsX + half * buttonWidth);
        rangeUp.add(centerOfButtonsX - half * buttonWidth);
        rangeUp.add(centerOfButtonsY - gap * buttonHeight);
        rangeUp.add(centerOfButtonsY - farest * buttonHeight);

        //DOWN
        rangeDown.add(centerOfButtonsX + half * buttonWidth);
        rangeDown.add(centerOfButtonsX - half * buttonWidth);
        rangeDown.add(centerOfButtonsY + farest * buttonHeight);
        rangeDown.add(centerOfButtonsY + gap * buttonHeight);

        buttonRange.add(rangeLeft);
        buttonRange.add(rangeRight);
        buttonRange.add(rangeUp);
        buttonRange.add(rangeDown);
    }
}
