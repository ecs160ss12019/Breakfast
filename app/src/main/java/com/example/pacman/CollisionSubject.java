package com.example.pacman;

// This is an interface for Collision subject which will change state
public interface CollisionSubject {
    public void registerObserver(CollisionObserver observer);
    public void removeObserver(CollisionObserver observer);
    public void notifyObservers();
}
