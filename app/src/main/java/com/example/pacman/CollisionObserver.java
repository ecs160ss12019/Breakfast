package com.example.pacman;

import java.util.ArrayList;

public interface CollisionObserver {
    public void update(ArrayList<TwoTuple> route);
}
