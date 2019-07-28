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

    private MovingObject pacman;
    private MovingObject redGhost;
    private ArrayList<MovingObject> movingObjects;
    private ArrayList<StationaryObject> stationaryObjects;
    private ArrayList<GameObject> collisions;

    private boolean containsPacman;
    private boolean ghostChasing;
    private boolean ghostScattering;
    private boolean ghostEscaping;
    private boolean PowerPelletEffective;

    private GameObjectTimer powerPelletTimer;

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
//        for (MovingObject movingObject : movingObjects) {
//            if (movingObject instanceof Pacman) {
//                ((Pacman) movingObject).setInputDirection(userInput);
//            }
//
//            if (movingObject instanceof Ghost) {
//                MotionInfo ghostInfo = movingObject.motionInfo;
//                MotionInfo pacmanInfo = pacman.motionInfo;
//                if (arcadeAnalyzer.isCross(ghostInfo.posInArcade)) {
//                    movingObject.motionInfo.nextDirection = ((Ghost) movingObject).ghostBehaviour.performBehaviour(ghostInfo,
//                            pacmanInfo, redGhost.motionInfo, arcadeAnalyzer);
//                }
//            }
//
//            if (movingObject instanceof Cake) {
//                Random random = new Random();
//                MotionInfo changedDir = movingObject.getMotionInfo();
//                changedDir.nextDirection = random.nextInt(4);
//                movingObject.setMotionInfo(changedDir);
//            }
//        }

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
        //updateTimer();

        pacmanReborn();
    }

    public void pacmanReborn() {
        if(pacman == null) return;
        if ( containsPacman == false ) {
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
                System.out.println("read Pacman to current collection after reborn");
            }

            containsPacman = true;
            Pacman.totalLives -=1;
        }
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
        for (GameObject gameObject : collisions) {
            if (gameObject instanceof MovingObject) {
                if (gameObject instanceof Ghost) {
                    if (PowerPelletEffective) {
                        //eats ghost
                        movingObjects.remove(gameObject);
                        //ghost.ghostBehaviour = new GhostKilledBehaviour();
                        //ghostChasing = false;
                        //ghostScattering = false;
                        //ghostEscaping = false;

                        score.ghostEaten();
                    } else {
                        //being eaten by ghost
                        movingObjects.remove(pacman);
                        containsPacman = false;
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

                    powerPelletTimer = new GameObjectTimer(GameObjectTimer.powerUp);
                    PowerPelletEffective = true;
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

    private void updateGhostBehaviour (Ghost ghost){
        if (ghost.ghostBehaviour instanceof GhostChaseBehaviour ||
                ghost.ghostBehaviour instanceof ChaseFrontBehaviour ||
                ghost.ghostBehaviour instanceof GhostPredictAndChaseBehaviour ||
                ghost.ghostBehaviour instanceof GhostSearchAndChaseBehaviour) {
            if (ghostEscaping) {
                ghost.ghostBehaviour = new EscapeBehaviour();

                System.out.println("ghost " + ghost.id + ": from chase behaviour to escape behaviour");
                return;
            }

            if (ghostScattering) {
                //ghost.ghostBehaviour = new GhostScatterBehaviour();

                System.out.println("ghost " + ghost.id + ": from chase behaviour to scatter behaviour");
                return;
            }

            System.out.println("ghost " + ghost.id + ": from chase behaviour to chase behaviour");
            return;
        }

        //FIXME
        if (ghost.ghostBehaviour instanceof GhostScatterLeftTop) {
            if (ghostEscaping) {
                ghost.ghostBehaviour = new EscapeBehaviour();

                System.out.println("ghost " + ghost.id + ": from scatter behaviour to escape behaviour");
                return;
            }

            if (ghostChasing) {
                switch (ghost.id) {
                    case 0:
                        ghost.ghostBehaviour = new GhostChaseBehaviour();
                        break;
                    case 1:
                        ghost.ghostBehaviour = new ChaseFrontBehaviour();
                        break;
                    case 2:
                        ghost.ghostBehaviour = new GhostPredictAndChaseBehaviour();
                        break;
                    case 3:
                        ghost.ghostBehaviour = new GhostSearchAndChaseBehaviour();
                        break;
                }

                System.out.println("ghost " + ghost.id + ": from scatter behaviour to chase behaviour");
                return;
            }

            System.out.println("ghost " + ghost.id + ": from scatter behaviour to scatter behaviour");
            return;
        }

        if (ghost.ghostBehaviour instanceof EscapeBehaviour) {
            if (ghostChasing) {
                switch (ghost.id) {
                    case 0:
                        ghost.ghostBehaviour = new GhostChaseBehaviour();
                        break;
                    case 1:
                        ghost.ghostBehaviour = new ChaseFrontBehaviour();
                        break;
                    case 2:
                        ghost.ghostBehaviour = new GhostPredictAndChaseBehaviour();
                        break;
                    case 3:
                        ghost.ghostBehaviour = new GhostSearchAndChaseBehaviour();
                        break;
                }

                System.out.println("ghost " + ghost.id + ": from escape behaviour to chase behaviour");
                return;
            }

            System.out.println("ghost " + ghost.id + ": from escape behaviour to escape behaviour");
            return;
        }
    }


    //Constructor
    public GameObjectCollection(final Context context, final TwoTuple mScreen,
                                final Arcade arcade, final GameMode gameMode) {
        this.arcade = arcade;
        this.arcadeAnalyzer = new ArcadeAnalyzer(arcade, true);
        this.motionController = new MotionController(arcade);

        this.ghostChasing = true;
        this.ghostScattering = false;
        this.ghostEscaping = false;
        this.PowerPelletEffective = false;

        this.powerPelletTimer = new GameObjectTimer();

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

        //FIXME
        //final TwoTuple ghostInitPos = new TwoTuple(arcade.ghostPosition);
        //final TwoTuple ghostInitPos = new TwoTuple(5,3);
        final TwoTuple ghostInitPos = new TwoTuple(30,1);

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
        //redGhost = new Ghost(0, redInitMotion, redViews, new GhostScatterLeftTop(), 10000);
        redGhost = new Ghost(0, redInitMotion, redViews, new GhostKilledBehaviour(), 10000);

        //INIT PinkGhost
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
        //MovingObject pinkGhost = new Ghost(1, pinkInitMotion, pinkViews, new GhostScatterRightTop(), 12000);
        MovingObject pinkGhost = new Ghost(1, pinkInitMotion, pinkViews, new GhostKilledBehaviour(), 12000);

        //INIT BlueGhost
        MotionInfo blueInitMotion = new MotionInfo(
                ghostInitPos,
                arcade.mapScreen(ghostInitPos),
                0, UP, UP, gameMode.getGhostsSpeed());
        ArrayList<Bitmap> blueViews = new ArrayList<>();
        blueViews.add(ghostsViewList.get(2));
        blueViews.add(ghostsViewList.get(2));
        blueViews.add(ghostsViewList.get(2));
        blueViews.add(ghostsViewList.get(2));

        //TODO change another behavior
//        MovingObject blueGhost = new Ghost(2, blueInitMotion, blueViews, new GhostScatterLeftBottom(), 14000);
        MovingObject blueGhost = new Ghost(2, blueInitMotion, blueViews, new GhostKilledBehaviour(), 14000);

        //INIT YellowGhost
        MotionInfo yellowInitMotion = new MotionInfo(
                ghostInitPos,
                arcade.mapScreen(ghostInitPos),
                0, UP, UP, gameMode.getGhostsSpeed());
        ArrayList<Bitmap> yellowViews = new ArrayList<>();
        yellowViews.add(ghostsViewList.get(0));
        yellowViews.add(ghostsViewList.get(0));
        yellowViews.add(ghostsViewList.get(0));
        yellowViews.add(ghostsViewList.get(0));

        //TODO change another behavior
//        MovingObject yellowGhost = new Ghost(3, yellowInitMotion, yellowViews, new GhostScatterRightBottom(), 16000);
        MovingObject yellowGhost = new Ghost(3, yellowInitMotion, yellowViews, new GhostKilledBehaviour(), 16000);

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
