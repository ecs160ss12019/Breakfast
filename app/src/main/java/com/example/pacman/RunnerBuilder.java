package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;

public class RunnerBuilder {
    private Context context;
    private TwoTuple screen;
    private Bitmap view;
    private float speed;
    private CollisionSubject collision;
    private String name;
    private Pacman pacman;
    private Arcade arcade;

    public RunnerBuilder(Context context, TwoTuple screen, CollisionSubject collision) {
        this.context = context;
        this.screen = screen;
        this.collision = collision;
    }

    public RunnerBuilder setSpeed(float speed) {
        this.speed = speed;
        return this;
    }

    public RunnerBuilder setArcade(Arcade arcade) {
        this.arcade = arcade;
        return this;
    }

    public RunnerBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public RunnerBuilder setPacman(Pacman pacman) {
        this.pacman = pacman;
        return this;
    }

    public Pacman createPacman() {
        return new Pacman(context, screen, speed, collision, arcade);
    }

    public Pacman createPacman(ArcadeAnalyzer analyzer) {
        return new Pacman(context, screen, speed, collision, arcade, analyzer);
    }

    public GhostList createGhosts() {
        return new GhostList(context, screen, speed, collision, pacman, arcade);
    }

    public GhostList createGhosts(ArcadeAnalyzer analyzer) {
        return new GhostList(context, screen, speed, collision, pacman,  arcade, analyzer);
    }

    public Cake createCake() {
        return new Cake(context, screen, speed, collision, arcade);
    }

    public Cake createCake(ArcadeAnalyzer analyzer) {
        return new Cake(context, screen, speed, collision, arcade, analyzer);
    }
}