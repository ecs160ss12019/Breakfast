package com.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.ArrayList;

/*
this class is the arcade that the pacman
and other game characters walk on.
There may be many arcades in each game.

We use JSON file format to document a list of
Arcade. Now this is how one Arcade in that
list should look like.
    {
    "name": "Sample Arcade 1",
    "numRow": 19,
    "numCol": 19,
    "imgFileRow": 1,
    "imgFileCol": 3,
    "inUse": true,
    "pacmanX": 10,
    "pacmanY": 9,
    "matrix": [ [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
                [0,1,1,1,2,1,1,1,1,2,1,1,1,1,2,1,1,1,0],
                [0,1,2,1,2,1,2,2,1,2,1,2,2,1,2,1,2,1,0],
                [0,1,1,1,1,1,1,1,1,2,1,1,1,1,1,1,1,1,0],
                [0,1,2,2,2,2,1,2,2,2,2,2,1,2,2,2,2,1,0],
                [1,1,2,2,2,2,1,1,1,1,1,1,1,2,2,2,2,1,1],
                [0,1,1,1,1,1,1,2,2,2,2,2,1,1,1,1,1,1,0],
                [0,1,2,1,2,2,1,2,2,2,2,2,1,2,2,1,2,1,0],
                [0,1,2,1,2,1,1,1,1,1,1,1,1,1,2,1,2,1,0],
                [0,1,2,1,1,1,2,1,2,2,2,1,2,1,1,1,2,1,0],
                [0,1,2,2,2,1,2,1,1,1,1,1,2,1,2,2,2,1,0],
                [0,1,1,1,1,1,2,1,2,1,2,1,2,1,1,1,1,1,0],
                [1,1,2,2,2,1,2,1,2,1,2,1,2,1,2,2,2,1,1],
                [0,1,1,1,1,1,1,1,2,1,2,1,1,1,1,1,1,1,0],
                [0,1,2,2,2,1,2,2,2,1,2,2,2,1,2,2,2,1,0],
                [0,1,2,1,1,1,1,1,1,1,1,1,1,1,1,1,2,1,0],
                [0,1,2,1,2,2,2,1,2,2,2,1,2,2,2,1,2,1,0],
                [0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0],
                [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]	]
    }
 */
public class Arcade implements GameObject{
    private Context context;

    //the matrix of building blocks
    private ArrayList<ArrayList<ArcadeBlock>> blocks = new ArrayList<>();

    //block num
    private int numRow;
    private int numCol;

    /*
    The img in the source folder is a collection of
    block views. We need to cut them into a list of
    3 views. We take the original collection file as
    a matrix of imgFileRow * imgFileCol
     */
    private int imgFileRow;
    private int imgFileCol;

    /*
    individual block view height/width
    this is crucial to centering the block
    on the coordinates
     */
    private int blockWidth;
    private int blockHeight;

    //A list of block imgs cut from the img source file
    private ArrayList<Bitmap> blockViewList;

    //is this Arcade in use?
    boolean inUse;

    /*
    When we draw the Arcade, we want to make sure it is
    at the middle of the screen. We keep the coordinate
    , in pixels, of the the top left hand corner.
     */
    private int xReference;
    private int yReference;

    //where the pacman should start from
    private int pacmanX;
    private int pacmanY;

    //where the ghost should start from
    private int ghostX;
    private int ghostY;

    //pacman start position in pixel
    private int pacmanX_pix;
    private int pacmanY_pix;

    // ghost start position in pixel
    private int ghostX_pix;
    private int ghostY_pix;

    public int getPacmanX_pix() { return pacmanX_pix; }

    public int getPacmanY_pix() {
        return pacmanY_pix;
    }

    public int getGhostX_pix() { return ghostX_pix; }

    public int getGhostY_pix() { return ghostY_pix; }

    //getBlockSize


    public int getBlockWidth() {
        return blockWidth;
    }

    public int getBlockHeight() {
        return blockHeight;
    }

    /*
        We take an coordinate, and we want to know what are the
        obstacles surrounding it. The reason we cannot scan through
        the whole arcade and return all obstacles is because doing so will
        essentially take O(n^2) time to check collision in the update() method.

        We need a faster algorithm to make sure there is no delay in the main thread.
        To achieve this, we need to restrict the obstacle list to be as small as possible.

        We know that an object cannot possibly collide into an obstacle if the
        distance between there centers is longer than the sum of distances between
        each object's center and its farest point on boundary.
        d > sigma(L) = l1 + l2 = distance(center1, vertex1) + distance(center2, vertex2).

        We use this property to filter out blocks that are too far to become obstacles.
        Further, note that both GameObject and ArcadeBlock, at least for now,
        are bounded by square, we can reduce the calculation to merely evaluating
        abs(diff(center1, center2)).

        Note that we want to restrict the origin object size to be the same as the
        ArcadeBlock.

        Note that this obstacle list can be empty. Thus, do handle
        the empty case in the caller function.
         */
    public ArrayList<Obstacle> getObstacleList(int originX, int originY) {
        ArrayList<Obstacle> obstacles = new ArrayList<>();
        for (int i = 0; i < numRow; i++) {
            for (int j = 0; j < numCol; j++) {
                //if this block is a path, ignore it
                if(blocks.get(i).get(j).getType() != 2) {
                    //center of the block
                    //Note that we use width * j and height * i, not vice-versa.
                    int block_centerX_pix = xReference + blockWidth * j;
                    int block_centerY_pix = yReference + blockHeight * i;

                    //Diff center
                    int diffX = Math.abs(originX - block_centerX_pix);
                    int diffY = Math.abs(originY - block_centerY_pix);
                    if (diffX < blockWidth && diffY < blockHeight) {
                        //Add to obstacle list
                        obstacles.add(new Obstacle(block_centerX_pix, block_centerY_pix, blockWidth, blockHeight));
                    }
                }
            }
        }

        return obstacles;
    }

    @Override
    public void draw(Canvas canvas) {
        /*
        Drawing the arcade is essentially drawing
        all the blocks one by one
         */

        /*
        While we can either draw the blocks from this class
        or the ArcadeBlock class. There are pros and cons.
        pros: Drawing the blocks from here can save system resources
              we do not need to pass the canvas, the bitmap objects all
              around.
        cons: Drawing the blocks from here can make code ugly.

        For now, we can stick to this class. //TODO
         */

        /*
        The reference point is the top left hand corner coordinate
        we previously calculated.
        #----[---|---]----#
        #----[-P-|---]----#
        #----[---|---]----#
        For block P, the position in pixel is calculated by:
        X = referenceX + blockWidth * j - blockWidth / 2
        Y = referenceY + blockHeight * i - blockHeight / 2
        No need to handle margin since we are aligning edges

        Note that it is crucial to apply the correct formula as
        above when calculating the coordinates.We want to use
        width * j and height * i. This is because that j is the col
        num and i is the row num. If we mistakenly multiplied width
        to i and height to j, we will get an up side down Arcade!!
         */

        for (int i = 0; i < numRow; i++) {
            for (int j = 0; j < numCol; j++) {
                int X = xReference + blockWidth * j - blockWidth / 2;
                int Y = yReference + blockHeight * i - blockHeight / 2;

                int type = blocks.get(i).get(j).getType();
                if (type == 16 || type == 17 || type == 18) {
                    //Blocks with these type num are transparent
                    continue;
                }
                canvas.drawBitmap(blockViewList.get(type), X, Y, null);
            }
        }

    }

    @Override
    public void updateStatus(long pfs) {

    }

    /*
    Instead of deploying these variables from the constructor,
    we should do it here.

    This func should be called as soon as the Arcade is initialized.
    Do not use any function of the Arcade before deploying the
    parameters
     */
    public void deployParameter(int screenWidth, int screenHeight, String imgFile) {
       /*
        calculate proper block size
        Since the arcade is in square shape, we will assign
        the width to be the same as the height.
        The reason for choosing the height as the standard is
        because of the screen is always in landscape mode, where
        the horizontal length is longer.
        We fill the height, thus the optimal size is 1/numCol percent
        for each block.
         */
        this.blockWidth = this.blockHeight = screenHeight / numRow;

        /*
        now we are able to calculate the top left corner center coordinate.
        #----R--|--]----#
        #----[--|--]----#
        #----[--|--]----#
        As shown above, the #'s are the screen boundaries, the '[' and ']' are
        the outer boundaries of the Arcade. Here, the top left block refers to
        the '[' block in row 0.

        We want to know the coordinate of its center. The reason is that if we
        keep the record of this coordinate, we can easily iterate through all blocks
        and calculate there center coordinates. This helps us in drawing the arcade.

        The coordinate should be half ot horizontal length - half + margin
        of the matrix width in pixels.
         */
        double matrixWidthInPixel = numCol * blockWidth;
        double matrixHeightInPixel = numRow * blockHeight;
        xReference = (int) (screenWidth - matrixWidthInPixel) / 2 + blockWidth / 2;
        yReference = (int) (screenHeight - matrixHeightInPixel) / 2 + blockHeight / 2;

        /*
        initial pacman position referencing to top left corner.
        A visual representation:
        #----R--|--]----#
        #----[--|P-]----#
        #----[--|--]----#
         */
        pacmanX_pix = xReference + pacmanY * blockWidth;
        pacmanY_pix = yReference + pacmanX * blockHeight;

        ghostX_pix = xReference + ghostY * blockWidth;
        ghostY_pix = yReference + ghostX * blockHeight;

        /*
        Now we get the img for each types of block.
        While now we are doing it in this class, same as we did
        in Pacman, we eventually should implement a imgManager
        to help edit this imgs.
         */

        //load pacman img from resource
        //FIXME refer to id by filename?
        Bitmap blockCollectionView = BitmapFactory.decodeResource(context.getResources(), R.drawable.blocks3);

        /*
        resize the original file.
        We know that the height should be the same as blockHeight.
        Since this is the source file, we need to resize based on
        the oriFile height, width ratio
         */
        /*
        double ratio = blockCollectionView.getWidth() / blockCollectionView.getHeight();
        blockCollectionView = Bitmap.createScaledBitmap(blockCollectionView, (int)(ratio * blockHeight),
                blockHeight, true);
        */

        BitmapDivider divider = new BitmapDivider(blockCollectionView);
        blockViewList = divider.split(imgFileRow,imgFileCol);

        for (int i = 0; i < blockViewList.size(); i++) {
            Bitmap temp = Bitmap.createScaledBitmap(blockViewList.get(i), blockHeight,
                    blockHeight, true);
            blockViewList.set(i, temp);
        }
    }

    public Arcade(Context context, ArrayList<ArrayList<Integer>> matrix,
                  int numRow, int numCol,
                  int imgFileRow, int imgFileCol,
                  int pacmanX, int pacmanY, int ghostX, int ghostY,
                  boolean inUse) {
        this.context = context;

        //initialize block num
        this.numRow = numRow;
        this.numCol = numCol;

        //Now we initialize the matrix of blocks, and assign property to each
        for (int i = 0; i < numRow; i++) {
            ArrayList<ArcadeBlock> newLine = new ArrayList<>(numCol);
            for (int j = 0; j < numCol; j++) {
                //System.out.print(matrix.get(i).get(j) + " ");

                ArcadeBlock newBlock = new ArcadeBlock(i, j, matrix.get(i).get(j));
                newLine.add(newBlock);
            }
            //System.out.println();
            blocks.add(newLine);
        }

        /*
        Now we get the img for each types of block.
        While now we are doing it in this class, same as we did
        in Pacman, we eventually should implement a imgManager
        to help edit this imgs.
         */

        //currently, the png file is a 1*3 matrix
        this.imgFileRow = imgFileRow;
        this.imgFileCol = imgFileCol;

        this.pacmanX = pacmanX;
        this.pacmanY = pacmanY;
        this.ghostX = ghostX;
        this.ghostY = ghostY;

        this.inUse = inUse;
    }

}
