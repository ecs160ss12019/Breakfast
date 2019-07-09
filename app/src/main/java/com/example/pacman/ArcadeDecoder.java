package com.example.pacman;

import android.content.Context;
import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/*
We encode the arcades in a json file
The file structure looks like this:
[
    {Arcade1},
    {Arcade2},
    {Arcade3}
]
By doing this, we do not need to bother adding
tons of json files into the res folder. One file
will be sufficient. We want to decode all arcades
when onCreate() runs. We will keep all the arcade encoding
in a private ArrayList<Arcade> in the ArcadeList object, we
decide which arcade to display in game by referring to a
Boolean HashSet in the ArcadeList object.

In general, when onCreate() runs, this ArcadeDecoder object
will decode the raw json file and return a ArcadeList class.
One instance of the ArcadeList will be kept in the PacmanGame
class. We do the drawing according to the ArcadeList.
 */
public class ArcadeDecoder {
    private int[][] encodingMatrix;
    private int numRow;
    private int numCol;

    private int pacmanStartX;
    private int pacmanStartY;

    /*
    It is possible to get a IOException when
    constructing this class.
    Thus, use try catch block in the
     */
    public ArcadeDecoder(Context context, int fileIdentifier) throws IOException{
        /*
        It will make the code real harder to
        maintain if we use resource name string instead of
        resource identifier.
        We read the json file as a char stream from the
        resource folder, then parse using the json reader
         */
        InputStream in = context.getResources().openRawResource(fileIdentifier);
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

        try{

        } finally {
            reader.close();
        }
    }

}
