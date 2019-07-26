package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Random;

public class GameObjectCollection {
    final static int LEFT = 0;
    final static int RIGHT = 1;
    final static int UP = 2;
    final static int DOWN = 3;

    final private Arcade arcade;
    final private ArcadeAnalyzer arcadeAnalyzer;
    final private MotionController motionController;

    private MovingObject pacman;
    private ArrayList<MovingObject> movingObjects;
    private ArrayList<StationaryObject> stationaryObjects;
    private ArrayList<GameObject> collisions;

    private boolean containsPacman;
    private boolean atePowerPellet;


    public void draw(Canvas canvas) {
        arcade.draw(canvas);

        for (StationaryObject stationaryObject : stationaryObjects) {
            stationaryObject.draw(canvas);
        }

        for (MovingObject movingObject : movingObjects) {
            movingObject.draw(canvas);
        }
    }

    public void update(int userInput, long fps, PointSystem score) {
        //if there is pacman, update its nextDirection to userInput
        for (MovingObject movingObject : movingObjects) {
            if (movingObject instanceof Pacman) {
                ((Pacman) movingObject).setInputDirection(userInput);
            }

            if (movingObject instanceof Ghost) {
                MotionInfo ghostInfo = movingObject.motionInfo;
                MotionInfo pacmanInfo = pacman.motionInfo;
                if (arcadeAnalyzer.isCross(ghostInfo.posInArcade)) {
                    movingObject.motionInfo.nextDirection = ((Ghost) movingObject).ghostBehaviour.performBehaviour(ghostInfo,
                            pacmanInfo, null, arcadeAnalyzer);
                }
            }

            if (movingObject instanceof Cake) {
                Random random = new Random();
                MotionInfo changedDir = movingObject.getMotionInfo();
                changedDir.nextDirection = random.nextInt(4);
                movingObject.setMotionInfo(changedDir);
            }
        }
        updateMotion(fps);
        updateCollision();
        updateStatus(score);
    }

    private void updateMotion(long fps) {
        for (MovingObject movingObject : movingObjects) {
            motionController.updateMovingObject(movingObject, fps);
        }
    }

    private void updateCollision() {
        //All collisions happen when the pacman is present!!!
        if (!containsPacman) return;

        Rect pacmanPathRect = pacman.getPathRect();
        for (MovingObject movingObject : movingObjects) {
            if (!(movingObject instanceof Pacman) &&
                    movingObject.collision(pacmanPathRect)) {
                collisions.add(movingObject);
            }
        }

        for (StationaryObject stationaryObject : stationaryObjects) {
            if (stationaryObject.collision(pacmanPathRect)) {
                collisions.add(stationaryObject);
            }
        }
    }

    //All collisions happen when the pacman is present!!!
    //we only cares about and update upon those ones !!!
    private void updateStatus(PointSystem score) {
        for (GameObject gameObject : collisions) {
            if (gameObject instanceof MovingObject) {
                if (gameObject instanceof Ghost) {
                    if (atePowerPellet) {
                        //eats ghost
                        movingObjects.remove(gameObject);
                        score.ghostEaten();
                    } else {
                        //being eaten by ghost
                        movingObjects.remove(pacman);
                        containsPacman = false;
                        atePowerPellet = false;
                    }
                }

                if (gameObject instanceof Cake) {
                    movingObjects.remove(gameObject);
                    score.cakeEaten();
                }
            }

            if (gameObject instanceof StationaryObject) {
                if(gameObject instanceof PowerPellet){
                    if(((PowerPellet) gameObject).checkReward() == false){
                        score.pwrpelletEaten();
                        ((PowerPellet) gameObject).reward();
                    }
                    atePowerPellet = false;
                }else {
                    if(((NormalPellet) gameObject).checkReward() == false){
                        score.pelletEaten();
                        ((NormalPellet) gameObject).reward();
                    }
                }
                stationaryObjects.remove(gameObject);
            }
        }
    }

    //Constructor
    public GameObjectCollection(final Context context, final TwoTuple mScreen,
                                final Arcade arcade, final GameMode gameMode) {
        this.arcade = arcade;
        this.arcadeAnalyzer = new ArcadeAnalyzer(arcade);
        this.motionController = new MotionController(arcade);

        final BitmapDivider bitmapDivider = new BitmapDivider(context);

        //Add moving objects to movingObjects list
        movingObjects = new ArrayList<>();

        //INIT Pacman
        TwoTuple pacmanInitPos = new TwoTuple(arcade.pacmanPosition);
        ArrayList<Bitmap> pacmanViewList = BitmapDivider.splitAndResize(
                bitmapDivider.loadBitmap(R.drawable.pacman),
                new TwoTuple(2,2),
                new TwoTuple(mScreen.y / 15, mScreen.y / 15));
        MotionInfo pacmanInitMotion = new MotionInfo(
                pacmanInitPos,
                arcade.mapScreen(pacmanInitPos),
                0, RIGHT, -1, gameMode.getPacmanSpeed());
        pacman = new Pacman(pacmanInitMotion, pacmanViewList);

        //Init ghosts
        final ArrayList<Bitmap> ghostsViewList = BitmapDivider.splitAndResize(
                bitmapDivider.loadBitmap(R.drawable.ghosts),
                new TwoTuple(2,2),
                new TwoTuple(mScreen.y / 15, mScreen.y / 15));
        final TwoTuple ghostInitPos = new TwoTuple(arcade.ghostPosition);

        //INIT ClayGhost
        MotionInfo clayInitMotion = new MotionInfo(
                ghostInitPos,
                arcade.mapScreen(ghostInitPos),
                0, UP, UP, gameMode.getGhostsSpeed());
        ArrayList<Bitmap> clayViews = new ArrayList<>();
        clayViews.add(ghostsViewList.get(0));
        clayViews.add(ghostsViewList.get(0));
        clayViews.add(ghostsViewList.get(0));
        clayViews.add(ghostsViewList.get(0));

        //TODO change another behavior
        MovingObject clayGhost = new Ghost(clayInitMotion, clayViews, new ChaseBehaviour());

        //INIT RedGhost
        MotionInfo redInitMotion = new MotionInfo(
                ghostInitPos,
                arcade.mapScreen(ghostInitPos),
                0, UP, UP, gameMode.getGhostsSpeed());
        ArrayList<Bitmap> redViews = new ArrayList<>();
        redViews.add(ghostsViewList.get(1));
        redViews.add(ghostsViewList.get(1));
        redViews.add(ghostsViewList.get(1));
        redViews.add(ghostsViewList.get(1));

        //TODO change another behavior
        MovingObject redGhost = new Ghost(redInitMotion, redViews, new EscapeBehaviour());

        //INIT GreenGhost
        MotionInfo greenInitMotion = new MotionInfo(
                ghostInitPos,
                arcade.mapScreen(ghostInitPos),
                0, UP, UP, gameMode.getGhostsSpeed());
        ArrayList<Bitmap> greenViews = new ArrayList<>();
        greenViews.add(ghostsViewList.get(2));
        greenViews.add(ghostsViewList.get(2));
        greenViews.add(ghostsViewList.get(2));
        greenViews.add(ghostsViewList.get(2));

        //TODO change another behavior
        MovingObject greenGhost = new Ghost(greenInitMotion, greenViews, new ChaseFrontBehaviour());

        //INIT PingGhost
        MotionInfo pinkInitMotion = new MotionInfo(
                ghostInitPos,
                arcade.mapScreen(ghostInitPos),
                0, UP, UP, gameMode.getGhostsSpeed());
        ArrayList<Bitmap> pinkViews = new ArrayList<>();
        pinkViews.add(ghostsViewList.get(3));
        pinkViews.add(ghostsViewList.get(3));
        pinkViews.add(ghostsViewList.get(3));
        pinkViews.add(ghostsViewList.get(3));

        //TODO change another behavior
        MovingObject pinkGhost = new Ghost(pinkInitMotion, pinkViews, new ChaseBehaviour());

        //INIT Cake
        TwoTuple cakeInitPos = new TwoTuple(arcade.cakePosition);
        MotionInfo cakeInitMotion = new MotionInfo(
                cakeInitPos,
                arcade.mapScreen(cakeInitPos),
                0, UP, UP, gameMode.getPacmanSpeed());
        ArrayList<Bitmap> cakeViewList = BitmapDivider.splitAndResize(
                bitmapDivider.loadBitmap(R.drawable.cake),
                new TwoTuple(1,1),
                new TwoTuple(mScreen.y / 15, mScreen.y / 15));
        ArrayList<Bitmap> cakeViews = new ArrayList<>();
        cakeViews.add(cakeViewList.get(0));
        cakeViews.add(cakeViewList.get(0));
        cakeViews.add(cakeViewList.get(0));
        cakeViews.add(cakeViewList.get(0));
        MovingObject cake = new Cake(cakeInitMotion, cakeViews);

        movingObjects.add(pacman);
        movingObjects.add(clayGhost);
        movingObjects.add(redGhost);
        movingObjects.add(greenGhost);
        movingObjects.add(pinkGhost);
        movingObjects.add(cake);


        //Add Stationary objects to stationaryObjects list
        stationaryObjects = new ArrayList<>();

        //Init Pellets
        final int numRow = arcade.getNumRow();
        final int numCol = arcade.getNumCol();

        final ArrayList<Bitmap> normalPelletViewList = BitmapDivider.splitAndResize(
                bitmapDivider.loadBitmap(R.drawable.pellet),
                new TwoTuple(1,1),
                new TwoTuple(mScreen.y / 45, mScreen.y / 45));

        final ArrayList<Bitmap> powerPelletViewList = BitmapDivider.splitAndResize(
                bitmapDivider.loadBitmap(R.drawable.powerpellet),
                new TwoTuple(1,1),
                new TwoTuple(mScreen.y / 22, mScreen.y / 22));

        ArrayList<ArrayList<Bitmap>> pelletViewLists= new ArrayList<>();
        pelletViewLists.add(normalPelletViewList);
        pelletViewLists.add(powerPelletViewList);

        Random random = new Random();
        for (int i = 0; i < numRow; i++) {
            for (int j = 0; j < numCol; j++) {
                int type = arcade.getBlock(new TwoTuple(i, j)).getType();
                //Will need to update the location of pellets with json later
//                if (type == 16) {
//                    TwoTuple posInArcade = new TwoTuple(i, j);
//                    TwoTuple posInScreen = arcade.mapScreen(posInArcade);
//                    StaticInfo pelletInfo = new StaticInfo(posInArcade, posInScreen);
//                    int pelletType = random.nextInt(2);
//
//                    StationaryObject nextPellet;
//                    if (pelletType == 0){
//                        nextPellet = new PowerPellet(pelletInfo, pelletViewLists.get(pelletType));
//                    } else {
//                        nextPellet = new NormalPellet(pelletInfo, pelletViewLists.get(pelletType));
//                    }
//                    stationaryObjects.add(nextPellet);
//                }

                TwoTuple posInArcade = new TwoTuple(i, j);
                TwoTuple posInScreen = arcade.mapScreen(posInArcade);
                StaticInfo pelletInfo = new StaticInfo(posInArcade, posInScreen);
                StationaryObject nextPellet;

                //Normal pellet
                if (type == 40) {
                    nextPellet = new NormalPellet(pelletInfo, pelletViewLists.get(0));
                    stationaryObjects.add(nextPellet);
                }

                //Power pellet!!!
                if (type == 42) {
                    nextPellet = new PowerPellet(pelletInfo, pelletViewLists.get(1));
                    stationaryObjects.add(nextPellet);
                }


            }
        }
        atePowerPellet = false;

        collisions = new ArrayList<>();

        this.containsPacman = arcade.inUse;
    }
}
