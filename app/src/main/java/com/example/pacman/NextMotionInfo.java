package com.example.pacman;

/*
Use this class to return updated motion
info.
If !valid then the gameObject that receive
this motion info cannot change its motion
status.

If valid, then the gameObject should advance
its center pos to pos and update currDirection
 */
public class NextMotionInfo {
    private TwoTuple pos;
    private boolean valid;

    public TwoTuple getPos() {
        return pos;
    }

    public boolean isValid() {
        return valid;
    }

    public NextMotionInfo(TwoTuple pos, boolean valid) {
        this.pos = pos;
        this.valid = valid;
    }
}
