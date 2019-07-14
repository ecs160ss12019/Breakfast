package com.example.pacman;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class GhostListTest {
    @Test
    public void ghostList_GhostsNum_is4() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        ArcadeList arcades = new ArcadeList(appContext, 800, 600, R.raw.sample2);
        GhostList ghostList = new GhostList(appContext, 800, 600, arcades.getArcadeContainingPacman());

        assertEquals(4, ghostList.ghosts.size());
    }
}
