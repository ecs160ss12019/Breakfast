package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.ArrayList;

public class PelletList implements CollisionObserver {
    private ArrayList<ArrayList<PelletCell>> pelletList;
    ArrayList<Arcade> arcades;
    private ArrayList<Bitmap> pelletViewList;
    private Context context;
    private int bitmapWidth;
    private int bitmapHeight;
    private int pwrBitmapWidth;
    private int pwrBitmapHeight;

    private int indexOfArcadeContainPacman = 0; // TODO: hard code to 0
    private CollisionSubject collision;

    /*
        We want to Draw the pellets based on the creation of the matrix arcade
     */
    public void draw(Canvas canvas){
        for (int index = 0; index < arcades.size(); ++index){
//            System.out.println("2d array not zero ");
            for(int i = 0; i < pelletList.get(index).size(); ++i) {
//                System.out.println("1d array not zero");
                PelletCell pellet = pelletList.get(index).get(i);
                TwoTuple posOnScreen = map2screen(pellet);
                int type = pellet.getType();
                if (!pellet.isDead()) { // draw the pellet if it hasn't been eaten
                    if (type == 1) {
                        canvas.drawBitmap(pelletViewList.get(type),
                                posOnScreen.first() - (bitmapWidth / 2),
                                posOnScreen.second() - (bitmapHeight / 2), null);
                    } else {
                        canvas.drawBitmap(pelletViewList.get(type),
                                posOnScreen.first() - (pwrBitmapWidth / 2),
                                posOnScreen.second() - (pwrBitmapHeight / 2), null);
                    }
                }
            }
        }
    }


    public PelletList(Context context, ArrayList<Arcade> arcades, TwoTuple screen, CollisionSubject collision) {
        this.arcades = arcades;
        pelletList = new ArrayList<>();
        pelletViewList = new ArrayList<>();
        this.context = context;
        Bitmap pwrpelletView = BitmapFactory.decodeResource(context.getResources(), R.drawable.powerpellet);
        Bitmap pelletView = BitmapFactory.decodeResource(context.getResources(), R.drawable.pellet);
        int screenWidth = screen.first();
        int screenHeight = screen.second();

        double pwrPelletRatio = pwrpelletView.getWidth() / pwrpelletView.getHeight();
        double pelletRatio = pelletView.getWidth()/pelletView.getHeight();
        bitmapHeight = screenHeight / 45;
        bitmapWidth = (int)(bitmapHeight * pelletRatio) ;
        pwrBitmapHeight = screenHeight / 22;
        pwrBitmapWidth = (int)(pwrBitmapHeight * pwrPelletRatio);
        pelletViewList.add(Bitmap.createScaledBitmap(pwrpelletView,
                pwrBitmapWidth , pwrBitmapHeight, true));
        pelletViewList.add(Bitmap.createScaledBitmap(pelletView,
                bitmapWidth , bitmapHeight, true));


        //Random random = new Random();
        //Construct a matrix given the arcades with 0 and 1(pellet can be added).
        for (int index = 0; index < arcades.size(); ++index) {
            int numRow =  arcades.get(index).getNumRow();
            int numCol =  arcades.get(index).getNumCol();
            ArrayList<PelletCell> newLine = new ArrayList<>();
            for (int i = 0; i < numRow; ++i) {
                for (int j = 0; j < numCol; ++j) {
                    int p = 2;
                    if(newLine.size() % 30 == 0){
                        p=1;
                    }
                    if (arcades.get(index).getBlock(new TwoTuple(i, j)).getType() == 16 && p != 0) {
                        PelletCell pell = new PelletCell(i, j, p - 1, index);
                        newLine.add(pell);
                    }
                }
            }
            pelletList.add(newLine);
        }

        this.collision = collision;
        collision.registerObserver(this);
    }

    public TwoTuple map2screen(PelletCell pell) {
        int arcadeIndex = pell.getArcadeIndex();
        int x_pixel = arcades.get(arcadeIndex).xReference + pell.getY() * arcades.get(arcadeIndex).getBlockWidth();
        int y_pixel = arcades.get(arcadeIndex).yReference + pell.getX() * arcades.get(arcadeIndex).getBlockHeight();
        return new TwoTuple(x_pixel, y_pixel);
    }

    // public TwoTuple map2Arcade()

//    public PelletCell getPelletByBlockPosition(TwoTuple blockPosition) {
//        return pelletList.get(blockPosition.x).get(blockPosition.y);
//    }

    // udpate if the pellets are eaten by pacman.
    @Override
    public void update(ArrayList<TwoTuple> route) {

        ArrayList<PelletCell> pellets = pelletList.get(indexOfArcadeContainPacman);
        for(PelletCell pellet : pellets) {
            for (TwoTuple block : route) {
                if(pellet.getPositionInArcade().equals(block)) {
                    pellet.setDead(true);
                    if(pellet.getType()==1){
//                        points.pelletEaten();
//                        System.out.println(points.getScore());
                    }else{
//                        points.pwrpelletEaten();
                    }
                }
            }
        }
    }

    public int getPelletType(ArrayList<TwoTuple> route) {

        ArrayList<PelletCell> pellets = pelletList.get(indexOfArcadeContainPacman);
        for (PelletCell pellet : pellets) {
            for (TwoTuple block : route) {
                if (pellet.getPositionInArcade().equals(block)) {
                    if (!pellet.scoreAdded()) {
                        if (pellet.getType() == 1) {
                            pellet.addedpoint();
                            return 1;
                        } else if (pellet.getType() == 0) {
                            pellet.addedpoint();
                            return 0;
                        }
                    }
                }
            }
        }
        return -1;
    }

}
