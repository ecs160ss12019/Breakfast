package com.example.pacman;

import java.util.ArrayList;

/*
    project current motion onto the arcade block.
    current motion is not only decided by current
    position but also current direction.

    This projection method is implemented in Arcade.mapBlock(),
    but I think I'd better explain it here

    There are, in general, 3 cases in motion:
    1. At this instance, the object center (C) is right
       on the boundary between 2 blocks.
       ================
       |      |       |
       |  b1  C   b2  |         Assume moving right
       |      |       |         (b1 -> b2)
       ================

       In this case, we must advance the object's
       center to the center of next block in its
       current moving direction. Say, if the object is
       now moving from b1 -> b2. We should advance the
       center of c to center(b2).

    2. At this instance, the object center (C) is within
       a block's boundaries but have not crossed the center
       line (L) in its direction.
       ================
       |      |   |   |
       |  b1  | C b2  |         Assume moving right
       |      |   |   |         (b1 -> b2)
       ================
                  L
        In this case, we must advance the object's center
        to the center of the current block its within. Say,
        if the object is currently moving b1 -> b2. It's
        within b2 but have not yet crossed the center line L.
        We should advance C so that its center is equal to b2's
        center.

    3. At this instance, the object center (C) is within
       a block's boundaries and have crossed the center
       line (L) in its direction.
       ========================
       |      |   |   |   |   |
       |  b1  |  b2 C |   b3  | Assume moving right
       |      |   |   |   |   |  (b1 -> b2)
       ========================
                  L
        In this case, don't care. We've already lost
        control to the object and the only thing we can
        do is to let it travel in its current direction.

    Note that although here we say "Advance", we actually mean to
    consider the object is at the advanced position. We are not
    actually moving the object to that position. The reason that we
    do this "Advance" thing is because we cannot check if a motion
    is valid or not without "attaching" it onto a arcade block. We know
    that if an object travels over the center line of an block, we cannot
    just change the direction at that instant of time.

    Note that we need to carefully analyze this type of motion in every
    direction. The way we "Advance" differs across different directions.

    All these led to the design of decision region.
    We won't even consider direction if it
    is case 3. T
*/

public class MotionInArcade {
    static int noInput = -1;
    static int LEFT = 0;
    static int RIGHT = 1;
    static int UP = 2;
    static int DOWN = 3;

    private int currX;
    private int currY;
    private int nextX;
    private int nextY;
    private int currDirection;

    private TwoTuple currPos;
    private int blockCenterX;
    private int blockCenterY;

    private Arcade arcade;

    /*
    Check if the game object is in "decision region".
    If we really need to disturb the current motion.
     */
    public boolean inDecisionRegion() {
        /*
        currX = _currX;
        currY = _currY;
        nextX = _nextX;
        nextY = _nextY;
        currDirection = _currDirection;
        */

        /*
        now we calculate the gap between block center and
        the current position and the next position.

        If there is a sign difference, then it means that the object
        has crossed the decision region
        ========
        |       |
        |p1 C p2|   p1 -> p2 crossed decision region
        |       |
        ========
         */

        //get sign
        int currSignX = Integer.signum(currX - blockCenterX);
        int currSignY = Integer.signum(currY - blockCenterY);
        int nextSignX = Integer.signum(nextX - blockCenterX);
        int nextSignY = Integer.signum(nextY - blockCenterY);

        //System.out.println(currSignX + " " +currSignY +" " + nextSignX +" " + nextSignY);

        //if heading left or right && signX changed
        if ((currDirection == LEFT || currDirection == RIGHT) && currSignX != nextSignX) {
            return true;
        }

        //if heading up or down && signY changed
        if ((currDirection == UP || currDirection == DOWN) && currSignY != nextSignY) {
            return true;
        }

        return false;
    }

    /*
    You should only run this method if u've called inDecisionRegion()
    first and the return value is true.
     */
    public NextMotionInfo isValidMotion(int nextDirection) {
        boolean valid = false;
        int posInArcade_X = currPos.first();
        int posInArcade_Y = currPos.second();
        TwoTuple nextPos = new TwoTuple(0,0);

        //start checking
        if (nextDirection == LEFT) {
            System.out.println("Attempt to move Left");
            nextPos = new TwoTuple(posInArcade_X, posInArcade_Y - 1);
            valid = arcade.getBlock(nextPos).getType() == 16;
        }

        if (nextDirection == RIGHT) {
            System.out.println("Attempt to move Right");
            nextPos = new TwoTuple(posInArcade_X, posInArcade_Y + 1);
            valid = arcade.getBlock(nextPos).getType() == 16;
        }

        if (nextDirection == UP) {
            System.out.println("Attempt to move Up");
            nextPos = new TwoTuple(posInArcade_X - 1, posInArcade_Y);
            valid = arcade.getBlock(nextPos).getType() == 16;
        }

        if (nextDirection == DOWN) {
            System.out.println("Attempt to move Down");
            nextPos = new TwoTuple(posInArcade_X + 1, posInArcade_Y);
            valid = arcade.getBlock(nextPos).getType() == 16;
        }

        if (valid) {
            System.out.println("next motion valid");
            TwoTuple move = arcade.mapScreen(nextPos);
            return new NextMotionInfo(move, valid);
        }

        System.out.println("next motion invalid");
        TwoTuple noMove = arcade.mapScreen(currPos);
        return new NextMotionInfo(noMove, valid);
    }

    //On each iteration, first update the motion info
    public void updateMotionInfo(ArrayList<Integer> motion) {
        //motion validation
        if(motion.size() != 5) {
            System.out.println("Error: motion info");
        }

        this.currX = motion.get(0);
        this.currY = motion.get(1);
        this.nextX = motion.get(2);
        this.nextY = motion.get(3);
        this.currDirection = motion.get(4);

        /*
        First, let's find out which block is
        the game object in.
         */
        currPos = arcade.mapBlock(currX, currY, currDirection);

        System.out.println("Matched Block: " + currPos.first() + " " + currPos.second());

        //center of block
        blockCenterX = arcade.xReference + currPos.second() * arcade.getBlockWidth();
        blockCenterY = arcade.yReference + currPos.first() * arcade.getBlockHeight();

        System.out.println("Block center: " + blockCenterX + " " + blockCenterY);
    }

    //just in case we change to another arcade.
    public void setArcade(Arcade arcade) {
        this.arcade = arcade;
    }

    //Constructor
    public MotionInArcade(Arcade arcade) {
        this.arcade = arcade;
    }
}
