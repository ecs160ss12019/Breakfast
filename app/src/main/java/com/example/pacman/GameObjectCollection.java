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

    //red: chase
    //pink: front
    //blue: predict
    //yellow: search
    final static int RedGhostId = 0;
    final static int PinkGhostId = 1;
    final static int BlueGhostId = 2;
    final static int YellowGhostId = 3;

    final private Arcade arcade;
    final private ArcadeAnalyzer arcadeAnalyzer;
    final private MotionController motionController;
    final private GameMode gameMode;

    private MovingObject pacman;
    private MovingObject redGhost;
    private ArrayList<MovingObject> movingObjects;
    private ArrayList<StationaryObject> stationaryObjects;
    private ArrayList<GameObject> collisions;

    private boolean containsPacman;
    private boolean PowerPelletEaten;

    public void draw(Canvas canvas) {
        arcade.draw(canvas);

        for (StationaryObject stationaryObject : stationaryObjects) {
            stationaryObject.draw(canvas);
        }

        for (MovingObject movingObject : movingObjects) {
            movingObject.draw(canvas);
        }

        if(!containsPacman) {
            ((Pacman)pacman).drawDied(canvas);
            pacmanReborn();
        }
    }

    public void update(int userInput, long fps, PointSystem score) {
        for (MovingObject movingObject : movingObjects) {
            if (movingObject instanceof Pacman) {
                ((Pacman) movingObject).setInputDirection(userInput);
            }

            if (movingObject instanceof Ghost) {
                MotionInfo ghostInfo = movingObject.motionInfo;
                MotionInfo pacmanInfo = pacman.motionInfo;

                //updateGhostBehaviour((Ghost)movingObject);

//                int id = ((Ghost)movingObject).id;
//                MotionInfo target = new MotionInfo();
//                if (id == RedGhostId) {
//                    target.posInArcade = new TwoTuple(2,3);
//                }
//
//                if (id == PinkGhostId) {
//                    target.posInArcade = new TwoTuple(2,24);
//                }
//
//                if (id == BlueGhostId) {
//                    target.posInArcade = new TwoTuple(28,3);
//                }
//
//                if (id == YellowGhostId) {
//                    target.posInArcade = new TwoTuple(28,24);
//                }

                System.out.println("Ghost timer: " + ((Ghost)movingObject).id + " " + ((Ghost)movingObject).gameObjectTimer.isTimeUp());

                if (arcadeAnalyzer.isCross(ghostInfo.posInArcade)) {
                    movingObject.motionInfo.nextDirection = ((Ghost) movingObject).ghostBehaviour.performBehaviour(ghostInfo,
                            pacmanInfo, redGhost.motionInfo, arcadeAnalyzer);
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
        updateGhostBehaviour();
        //pacmanReborn();
    }

    public void pacmanReborn() {
        if(pacman == null) return;
        if (!containsPacman ) {

            System.out.println("pacmanReborn");
            // reborn Pacman to the middle of current Arcade
            MotionInfo prevMotion = pacman.getMotionInfo();
            prevMotion.posInArcade = arcade.pacmanPosition;
            prevMotion.posInScreen = arcade.getPacmanPosition_pix();
            prevMotion.currDirection = TwoTuple.RIGHT;
            prevMotion.nextDirection = -1;
            pacman.setMotionInfo(prevMotion);
            System.out.println("pacman position after reborn : " + pacman.getMotionInfo().posInScreen.x +" "+ pacman.getMotionInfo().posInScreen.y);
            pacman.alive(); // reborn
            System.out.println("movingObjects.contains(pacman): " + movingObjects.contains(pacman));

            if(!movingObjects.contains(pacman)) {
                movingObjects.add(pacman);
                System.out.println("readd Pacman to current collection after reborn");
            }

            containsPacman = true;
            Pacman.totalLives -=1;

            // initial ghost when pacman died
            initialGhost();
        }
    }

    // initial ghost position and behavior
    public void initialGhost() {
        for (MovingObject movingObject : movingObjects) {
            if (movingObject instanceof Ghost) {
                ((Ghost)movingObject).ghostBehaviour = new GhostExitBehaviour();
                ((Ghost)movingObject).motionInfo = InitialMotioninfo(((Ghost)movingObject).id);
            }
        }
    }

    // initial ghost position

    public MotionInfo InitialMotioninfo(int ID){
        final TwoTuple redGhostInitPos = new TwoTuple(11,14);
        final TwoTuple pinkGhostInitPos = new TwoTuple(15,11);
        final TwoTuple blueGhostInitPos = new TwoTuple(15,13);
        final TwoTuple yellowGhostInitPos = new TwoTuple(15,15);

        MotionInfo Initialmotion = new MotionInfo();

        switch (ID){
            case 0:
                MotionInfo redInitMotion = new MotionInfo(
                        redGhostInitPos,
                        arcade.mapScreen(redGhostInitPos),
                        0, UP, -1, gameMode.getGhostsSpeed());
                Initialmotion = redInitMotion;

                break;
            case 1:
                MotionInfo pinkInitMotion = new MotionInfo(
                        pinkGhostInitPos,
                        arcade.mapScreen(pinkGhostInitPos),
                        0,RIGHT, -1, gameMode.getGhostsSpeed());
                Initialmotion = pinkInitMotion;

                break;
            case 2:
                MotionInfo blueInitMotion = new MotionInfo(
                        blueGhostInitPos,
                        arcade.mapScreen(blueGhostInitPos),
                        0, UP, -1, gameMode.getGhostsSpeed());
                Initialmotion = blueInitMotion;

                break;

            case 3:
                MotionInfo yellowInitMotion = new MotionInfo(
                        yellowGhostInitPos,
                        arcade.mapScreen(yellowGhostInitPos),
                        0, UP, -1, gameMode.getGhostsSpeed());
                Initialmotion = yellowInitMotion;

                break;
        }
        return Initialmotion;
    }


    private void updateMotion(long fps) {
        for (MovingObject movingObject : movingObjects) {
            motionController.updateMovingObject(movingObject, fps);
        }
    }

    private void updateCollision() {
        //All collisions happen when the pacman is present!!!
        if (!containsPacman) return;

        collisions = new ArrayList<>();
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
        PowerPelletEaten = false;
        for (GameObject gameObject : collisions) {
            if (gameObject instanceof MovingObject) {
                if (gameObject instanceof Ghost) {
                    if (((Ghost) gameObject).ghostBehaviour instanceof GhostChaseBehaviourInterface ||
                            ((Ghost) gameObject).ghostBehaviour instanceof GhostScatterBehaviourInterface) {
                        //being eaten by ghost
                        movingObjects.remove(pacman);
                        containsPacman = false;
                    } else {
                        score.ghostEaten();
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

                    PowerPelletEaten = true;
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

    private void updateGhostBehaviour() {
        for (MovingObject movingObject : movingObjects) {
            if (movingObject instanceof Ghost) {
                boolean collision = collisions.contains(movingObject);

                System.out.println("Ghost " + ((Ghost)movingObject).id + " : " + ((Ghost)movingObject).ghostBehaviour);
                System.out.println("Ghost pos " + movingObject.motionInfo.posInArcade.x + " " + movingObject.motionInfo.posInArcade.y);
                if (PowerPelletEaten) {
                    System.out.println("Eaten!!!");
                }
                ((Ghost)movingObject).updateGhostBehavior(PowerPelletEaten, collision);
                ((Ghost)movingObject).updateViewList();
                System.out.println("Update: " + ((Ghost)movingObject).ghostBehaviour);
            }
        }
    }

    //Constructor
    public GameObjectCollection(final Context context, final TwoTuple mScreen,
                                final Arcade arcade, final GameMode gameMode) {
        this.arcade = arcade;
        this.arcadeAnalyzer = new ArcadeAnalyzer(arcade, true);
        this.motionController = new MotionController(arcade);

        this.PowerPelletEaten = false;
        this.gameMode = gameMode;

        final BitmapDivider bitmapDivider = new BitmapDivider(context);

        //Add moving objects to movingObjects list
        movingObjects = new ArrayList<>();

        //INIT Pacman
        TwoTuple pacmanInitPos = new TwoTuple(arcade.pacmanPosition);

        ArrayList<Bitmap> pacmanViewList = BitmapDivider.splitAndResize(
                bitmapDivider.loadBitmap(R.drawable.pacmancombined),
                new TwoTuple(6,4),
                new TwoTuple(mScreen.y / 15, mScreen.y / 15));

        MotionInfo pacmanInitMotion = new MotionInfo(
                pacmanInitPos,
                arcade.mapScreen(pacmanInitPos),
                0, RIGHT, -1, gameMode.getPacmanSpeed());

        pacman = new Pacman(pacmanInitMotion, pacmanViewList);

        //Init ghosts
        final ArrayList<Bitmap> ghostsViewList = BitmapDivider.splitAndResize(
                bitmapDivider.loadBitmap(R.drawable.ghostcombined),
                new TwoTuple(6,4),
                new TwoTuple(mScreen.y / 15, mScreen.y / 15));
        final ArrayList<Bitmap> ghostKilledList = new ArrayList<>(ghostsViewList.subList(16,20));
        final ArrayList<Bitmap> ghostEscapeList = new ArrayList<>(ghostsViewList.subList(20,24));

        //INIT RedGhost
        final ArrayList<Bitmap> redGhostsViewList = new ArrayList<>(ghostsViewList.subList(0,4));

        final TwoTuple redGhostInitPos = new TwoTuple(11,14);
        final TwoTuple pinkGhostInitPos = new TwoTuple(15,11);
        final TwoTuple blueGhostInitPos = new TwoTuple(15,13);
        final TwoTuple yellowGhostInitPos = new TwoTuple(15,15);

        MotionInfo redInitMotion = new MotionInfo(
                redGhostInitPos,
                arcade.mapScreen(redGhostInitPos),
                0, UP, -1, gameMode.getGhostsSpeed());

        ArrayList<Bitmap> redViews = new ArrayList<>();
        redViews.add(ghostsViewList.get(1));
        redViews.add(ghostsViewList.get(1));
        redViews.add(ghostsViewList.get(1));
        redViews.add(ghostsViewList.get(1));

        redGhost = new Ghost(0, redInitMotion, redGhostsViewList,
                ghostEscapeList, ghostKilledList, new GhostStationaryBehaviour(), 1);

        //INIT PinkGhost
        final ArrayList<Bitmap> pinkGhostsViewList = new ArrayList<>(ghostsViewList.subList(4,8));

        MotionInfo pinkInitMotion = new MotionInfo(
                pinkGhostInitPos,
                arcade.mapScreen(pinkGhostInitPos),
                0, RIGHT, -1, gameMode.getGhostsSpeed());
        ArrayList<Bitmap> pinkViews = new ArrayList<>();
        pinkViews.add(ghostsViewList.get(3));
        pinkViews.add(ghostsViewList.get(3));
        pinkViews.add(ghostsViewList.get(3));
        pinkViews.add(ghostsViewList.get(3));

        Ghost pinkGhost = new Ghost(1, pinkInitMotion, pinkGhostsViewList,
                ghostEscapeList, ghostKilledList, new GhostStationaryBehaviour(), 2);

        //INIT BlueGhost
        final ArrayList<Bitmap> blueGhostsViewList = new ArrayList<>(ghostsViewList.subList(8,12));
        MotionInfo blueInitMotion = new MotionInfo(
                blueGhostInitPos,
                arcade.mapScreen(blueGhostInitPos),
                0, UP, -1, gameMode.getGhostsSpeed());
        ArrayList<Bitmap> blueViews = new ArrayList<>();
        blueViews.add(ghostsViewList.get(2));
        blueViews.add(ghostsViewList.get(2));
        blueViews.add(ghostsViewList.get(2));
        blueViews.add(ghostsViewList.get(2));

        Ghost blueGhost = new Ghost(2, blueInitMotion, blueGhostsViewList,
                ghostEscapeList, ghostKilledList, new GhostStationaryBehaviour(), 3);

        //INIT YellowGhost
        final ArrayList<Bitmap> yellowGhostsViewList = new ArrayList<>(ghostsViewList.subList(12,16));
        MotionInfo yellowInitMotion = new MotionInfo(
                yellowGhostInitPos,
                arcade.mapScreen(yellowGhostInitPos),
                0, UP, -1, gameMode.getGhostsSpeed());
        ArrayList<Bitmap> yellowViews = new ArrayList<>();
        yellowViews.add(ghostsViewList.get(0));
        yellowViews.add(ghostsViewList.get(0));
        yellowViews.add(ghostsViewList.get(0));
        yellowViews.add(ghostsViewList.get(0));

        Ghost yellowGhost = new Ghost(3, yellowInitMotion, yellowGhostsViewList,
                ghostEscapeList, ghostKilledList, new GhostStationaryBehaviour(), 4);

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
        movingObjects.add(this.redGhost);
        movingObjects.add(pinkGhost);
        movingObjects.add(blueGhost);
        movingObjects.add(yellowGhost);
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

        collisions = new ArrayList<>();

        this.containsPacman = arcade.inUse;
    }
}
