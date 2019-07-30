package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;

public class PelletsGenerator {
    final Arcade arcade;
    final ArrayList<ArrayList<Bitmap>> pelletViewLists;

    public ArrayList<StationaryObject> getPellets() {
        ArrayList<StationaryObject> pellets = new ArrayList<>();
        for (int i = 0; i < arcade.getNumRow(); i++) {
            for (int j = 0; j < arcade.getNumCol(); j++) {
                int type = arcade.getBlock(new TwoTuple(i, j)).getType();
                TwoTuple posInArcade = new TwoTuple(i, j);
                TwoTuple posInScreen = arcade.mapScreen(posInArcade);
                StaticInfo pelletInfo = new StaticInfo(posInArcade, posInScreen);
                StationaryObject nextPellet;

                //Normal pellet
                if (type == 40) {
                    nextPellet = new NormalPellet(pelletInfo, pelletViewLists.get(0));
                    pellets.add(nextPellet);
                }

                //Power pellet!!!
                if (type == 42) {
                    nextPellet = new PowerPellet(pelletInfo, pelletViewLists.get(1));
                    pellets.add(nextPellet);
                }
            }
        }

        return pellets;
    }

    public PelletsGenerator(final Arcade arcade, Context context,
                           final TwoTuple mScreen, final GameMode gameMode) {
        this.arcade = arcade;
        final BitmapDivider bitmapDivider = new BitmapDivider(context);
        final ArrayList<Bitmap> normalPelletViewList = BitmapDivider.splitAndResize(
                bitmapDivider.loadBitmap(R.drawable.pellet),
                new TwoTuple(1,1),
                new TwoTuple(mScreen.y / 45, mScreen.y / 45));
        final ArrayList<Bitmap> powerPelletViewList = BitmapDivider.splitAndResize(
                bitmapDivider.loadBitmap(R.drawable.powerpellet),
                new TwoTuple(1,1),
                new TwoTuple(mScreen.y / 22, mScreen.y / 22));
        pelletViewLists= new ArrayList<>();
        pelletViewLists.add(normalPelletViewList);
        pelletViewLists.add(powerPelletViewList);
    }
}
