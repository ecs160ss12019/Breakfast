package com.example.pacman;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class GhostTest {
    // test if ghost.x decrease after it moves left.
    // TODO: The test can not pass right now because collision exsits.
    //  Will rewrite the test after Siqi update move method without relying on collision detection.
    @Test
    public void ghost_Move_Left() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        ArcadeList arcades = new ArcadeList(appContext, 800, 600, R.raw.sample2);
        // create a new ghost move to LEFT at the beginning
        Ghost ghost = new Ghost(appContext, 800, 600, arcades.getArcadeContainingPacman(), 0);
        int xBeforeMove = ghost.x;
        ghost.updateStatus(10, arcades.getArcadeContainingPacman());
        int xAfterMove = ghost.x;
        assertTrue(xBeforeMove > xAfterMove);
    }
}
