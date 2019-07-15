package com.example.pacman;

import android.graphics.Canvas;

/*
Each ArcadeBlock is an individual element of the arcade.
It might be one of these:
                'path',
                'outer boundary',
                'inner boundary,
                'corner'
 */
public class ArcadeBlock {
    //This is the index in block matrix, unit is not in pixel
    public int x;
    public int y;

    //this marks the type of the block
    private int type;

    public int getType() {
        return type;
    }
/*
    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public void updateLocation(long fps) {

    }
*/

    public ArcadeBlock(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }
}
