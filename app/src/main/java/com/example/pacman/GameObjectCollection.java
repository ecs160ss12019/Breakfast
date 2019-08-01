package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

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
    final private GameMode gameMode;
    final private TwoTuple mScreen;
    private Context context;
    private SoundEffects sound;
    private GameObjectTimer gamePrepTimer;
    private GameObjectTimer cakeTimer;

    private MovingObject pacman;
    private MovingObject redGhost;
    private MovingObject cake;
    private ArrayList<MovingObject> movingObjects;
    private ArrayList<StationaryObject> stationaryObjects;
    private ArrayList<GameObject> collisions;

    private boolean escapeMusic;
    private boolean containsPacman;
    private boolean PowerPelletEaten;
    private boolean cakeAppear;
    boolean needToPause = false;

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
            if(pacman.checkalive()) pacmanReborn();
            gamePrepTimer.setTimer(GameObjectTimer.gamePrepTime);
        }

        if (!gamePrepTimer.isTimeUp()) {
            Typeface plain = Typeface.createFromAsset(context.getAssets(), "fonts/myFont.ttf");
            Paint paint = new Paint();
            paint.setTextSize(mScreen.x/20);
            paint.setTypeface(plain);
            paint.setARGB(230, 255, 0,100);
            canvas.drawText("Ready!!!", mScreen.x / 2 - 200, mScreen.y/2 + 150, paint);
        }
    }

    public void update(int userInput, long fps, PointSystem score) {
        if (escapeMusic){
            sound.playWaze();
        } else {
            sound.playSiren();
        }
        if(cakeTimer.isTimeUp() && cakeAppear == false){
            movingObjects.add(cake);
            cakeAppear = true;
        }

        if (!gamePrepTimer.isTimeUp()) return;
        //if (cakeTimer.isTimeUp()) movingObjects.add(cake);

        for (MovingObject movingObject : movingObjects) {
            if (movingObject instanceof Pacman) {
                ((Pacman) movingObject).setInputDirection(userInput);
            }

            if (movingObject instanceof Ghost) {

                escapeMusic = ((Ghost) movingObject).getIsGhostEscape();
                MotionInfo ghostInfo = movingObject.motionInfo;
                MotionInfo pacmanInfo = pacman.motionInfo;

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
        checkIfMovingObjectRunOut();
    }

    public void checkIfMovingObjectRunOut() {
        // check if Pacman is out of bound, which means it runs into the next arcade
        // System.out.println("containsPacman: " + containsPacman);
        if (pacman == null) return;
        // System.out.println("arcade.getNumCol(): " + arcade.getNumCol());
        for(MovingObject movingObject: movingObjects) {
            if(movingObject.ranIntoNextArcade(arcade.getNumCol())) {
                movingObject.moveToNextArcade();
                System.out.println(movingObject.getClass().toString() + " in arcade: " + movingObject.motionInfo.posInArcade.second() + " " + movingObject.motionInfo.posInArcade.first());
                movingObject.motionInfo.posInScreen = arcade.mapScreen(movingObject.motionInfo.posInArcade);
                // moveToNextArcade = 1; // we only need to set a flag for change arcade, let PacmanGame handle the rest. Because we can not update all the arcades in Collection
                // System.out.println("moveToNextArcade: " + moveToNextArcade);
            } else if (movingObject.ranIntoPrevArcade()) {
                movingObject.moveToPrevArcade(arcade.getNumCol());
                movingObject.motionInfo.posInScreen = arcade.mapScreen(movingObject.motionInfo.posInArcade);
                // moveToNextArcade = -1;
            }
        }
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
                ((Ghost)movingObject).ghostBehaviour = new GhostStationaryBehaviour();
                ((Ghost)movingObject).motionInfo = InitialMotioninfo(((Ghost)movingObject).id);
                switch (((Ghost)movingObject).id){
                    case 0:
                        ((Ghost)movingObject).gameObjectTimer.setTimer(0);
                        break;
                    case 1:
                        ((Ghost)movingObject).gameObjectTimer.setTimer(1);
                        break;
                    case 2:
                        ((Ghost)movingObject).gameObjectTimer.setTimer(2);
                        break;
                    case 3:
                        ((Ghost)movingObject).gameObjectTimer.setTimer(3);
                        break;
                }
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
        if(pacman == null) return;

        //All collisions happen when the pacman is present!!!
        if (!containsPacman && Pacman.totalLives <= 0) return;

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
                        pacman.eat();
                        sound.playPacmanDeath();
                    } else {
                        score.ghostEaten();
                        sound.playEatGhost();
                    }
                }

                if (gameObject instanceof Cake) {
                    movingObjects.remove(gameObject);
                    score.cakeEaten();
                    sound.playEatPower();
                }
            }

            if (gameObject instanceof StationaryObject) {
                if(gameObject instanceof PowerPellet){
                    if(((PowerPellet) gameObject).checkReward() == false){
                        sound.playEatPower();
                        score.pwrpelletEaten();
                        ((PowerPellet) gameObject).reward();
                    }

                    PowerPelletEaten = true;
                }else {
                    if(((NormalPellet) gameObject).checkReward() == false){
                        sound.playPacmanChomp();
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
                                final Arcade arcade, final GameMode gameMode, SoundEffects sound) {
        this.arcade = arcade;
        this.arcadeAnalyzer = new ArcadeAnalyzer(arcade, true);
        this.motionController = new MotionController(arcade, gameMode);
        this.sound = sound;

        this.PowerPelletEaten = false;
        this.gameMode = gameMode;
        this.context = context;
        this.mScreen = mScreen;

        //Add moving objects to movingObjects list
        movingObjects = new ArrayList<>();
        collisions = new ArrayList<>();
        this.containsPacman = arcade.inUse;
        escapeMusic = false;

        PacmanGenerator pacmanGenerator = new PacmanGenerator(arcade, context,
                mScreen, gameMode);
        pacman = pacmanGenerator.getPacman();
        movingObjects.add(pacman);

        GhostsGenerator ghostsGenerator = new GhostsGenerator(arcade, context,
        mScreen, gameMode);

        movingObjects.addAll(new ArrayList<>(ghostsGenerator.getGhosts()));
        this.redGhost = movingObjects.get(movingObjects.size() - 4);
        CakeGenerator cake = new CakeGenerator(arcade, context, mScreen, gameMode);
        this.cake = cake.getCake();

        this.cakeTimer = new GameObjectTimer(GameObjectTimer.chaseTime);


        //Add Stationary objects to stationaryObjects list

        PelletsGenerator pelletsGenerator = new PelletsGenerator(arcade, context,
                mScreen, gameMode);
        stationaryObjects = pelletsGenerator.getPellets();
        cakeAppear = false;


        this.gamePrepTimer = new GameObjectTimer();
        this.gamePrepTimer.setTimer(GameObjectTimer.gamePrepTime);
    }
}
