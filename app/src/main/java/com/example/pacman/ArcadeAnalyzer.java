package com.example.pacman;

import java.util.ArrayList;

public class ArcadeAnalyzer{
    private Arcade arcade;
    private final int numRow;
    private final int numCol;
    public final int blockDimension;

    //Allowed direction of an Arcade Block
    private ArrayList<ArrayList<Integer>> analyzedArcade;
    private ArrayList<Boolean> cross;

    final int LEFT = 0;
    final int RIGHT = 1;
    final int UP = 2;
    final int DOWN = 3;

    private int mapArrayList(TwoTuple block) {
        return block.first() * numCol + block.second();
    }

    public boolean isCross(TwoTuple block) {
        return cross.get(mapArrayList(block));
    }

    public ArrayList<Integer> getAllowedDirections(TwoTuple block) {
        return analyzedArcade.get(mapArrayList(block));
    }

    public boolean allowsToGo(TwoTuple block, int direction) {

//        for (int i = 0; i < numRow; i++) {
//            for (int j = 0; j < numCol; j++) {
//                System.out.println("Block: " + block.first() + " " + block.second() +
//                        " Allows: " + analyzedArcade.get(mapArrayList(new TwoTuple(i,j))));
//            }
//        }

        return !analyzedArcade.get(mapArrayList(block)).isEmpty() &&
                analyzedArcade.get(mapArrayList(block)).contains(direction);
    }

    private void run() {
        for (int i = 0; i < numRow; i++) {
            for (int j = 0; j < numCol; j++) {
                ArrayList<Integer> allowedDirections = new ArrayList<>(4);
                TwoTuple thisBlock = new TwoTuple(i, j);
                //Analyze Left
                if (arcade.pathValid(thisBlock.toLeft())) {
                    allowedDirections.add(LEFT);
                }

                //Analyze Right
                if (arcade.pathValid(thisBlock.toRight())) {
                    allowedDirections.add(RIGHT);
                }

                //Analyze Up
                if (arcade.pathValid(thisBlock.toUp())) {
                    allowedDirections.add(UP);
                }

                //Analyze Down
                if (arcade.pathValid(thisBlock.toDown())) {
                    allowedDirections.add(DOWN);
                }

                //Is this block a cross?
                if ((allowedDirections.size() <= 2) &&
                        ((allowedDirections.contains(LEFT) && allowedDirections.contains(RIGHT)) ||
                                (allowedDirections.contains(UP) && allowedDirections.contains(DOWN)))) {
                    cross.add(false);
                } else {
                    cross.add(true);
                }

                //Add to hashTable
                analyzedArcade.add(allowedDirections);
            }
        }
    }

    public ArcadeAnalyzer(Arcade arcade) {
        this.arcade = arcade;
        this.numRow = arcade.getNumRow();
        this.numCol = arcade.getNumCol();
        this.blockDimension = arcade.getBlockHeight();
        this.analyzedArcade = new ArrayList<>();
        this.cross = new ArrayList<>();
        this.run();
    }
}
