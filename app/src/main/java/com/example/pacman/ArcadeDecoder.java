package com.example.pacman;

import android.content.Context;
import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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

In general, when the ArcadeList constructor runs, this ArcadeDecoder
object will decode the raw json file and return a ArcadeList class.
One instance of the ArcadeList will be kept in the PacmanGame
class. We do the drawing according to the ArcadeList.
 */
public class ArcadeDecoder {
    private Context context;
    private int fileIdentifier;

    /*
    It is possible to get an IOException when
    constructing this class.
    Thus, use try catch block in the
     */
    public ArrayList<Arcade> decode() throws IOException{
        /*
        It will make the code real hard to
        maintain if we use resource name String instead of
        resource identifier.
        We read the json file as a char stream from the
        resource folder, then parse using the json reader
         */
        InputStream in = context.getResources().openRawResource(fileIdentifier);
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

        /*
        pass the Json reader in
        to decode in decodeArcades.
        We don't do it here, otherwise
        the code will be real ugly.
         */
        try{
            return decodeArcades(reader);
        } finally {
            reader.close();
        }
    }

    /*
    We first parse the outer structure
    [], this will return a list of arcade.
    Note that the beginArray() need to be
    handled. This function may throw IOExp,
    thus do use try catch block.
     */
    public ArrayList<Arcade> decodeArcades(JsonReader reader) throws IOException{
        //temp holder for return
        ArrayList<Arcade> arcades = new ArrayList<>();

        //begin extraction
        reader.beginArray();

        /*
        when there is another object to read,
        read it
         */
        while (reader.hasNext()) {
            /*
            now we have the new object,
            parse it in the decodeArcade()
            func, we do not want it here,
            otherwise the code will be ugly
             */
            arcades.add(decodeArcade(reader));
        }

        //we also need to close the array
        reader.endArray();

        return arcades;
    }

    /*
    Here we parse each individual Arcade.
    We construct a new Arcade object, fill
    in the info, and return it back.
    Note that this func may throw IOExp, thus
    do use try catch block.
     */
    public Arcade decodeArcade(JsonReader reader) throws IOException {
        /*
        things we need to extract
         */
        ArrayList<ArrayList<Integer>> matrix = new ArrayList<ArrayList<Integer>>();
        int numRow = 0;
        int numCol = 0;
        int imgFileRow = 0;
        int imgFileCol = 0;
        boolean inUse = false;
        int pacmanX = 0;
        int pacmanY = 0;
        int ghostX = 0;
        int ghostY = 0;

        /*
        start extraction
         */
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            //check and assign
            if(name.equals("numRow")) {
                numRow = reader.nextInt();
            } else if(name.equals("numCol")) {
                numCol = reader.nextInt();
            } else if(name.equals("imgFileRow")) {
                imgFileRow = reader.nextInt();
            } else if(name.equals("imgFileCol")) {
                imgFileCol = reader.nextInt();
            } else if(name.equals("inUse")) {
                inUse = reader.nextBoolean();
            } else if(name.equals("pacmanX")) {
                pacmanX = reader.nextInt();
            } else if(name.equals("pacmanY")) {
                pacmanY = reader.nextInt();
            } else if(name.equals("ghostX")) {
                ghostX = reader.nextInt();
            } else if(name.equals("ghostY")) {
                ghostY = reader.nextInt();
            } else if(name.equals("matrix")) {
                matrix = readMatrix(reader);
            } else {
                reader.skipValue();
            }
        }
        //close obj
        reader.endObject();

        return new Arcade(context, matrix, numRow, numCol,
                imgFileRow, imgFileCol, pacmanX, pacmanY, ghostX, ghostY, inUse);
    }

    /*
    Here we parse the encoding matrix.

    Note that this func may throw IOExp, thus
    do use try catch block.
     */
    public ArrayList<ArrayList<Integer>> readMatrix(JsonReader reader) throws IOException {
        ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            //read each line
            matrix.add(readArray(reader));
        }

        reader.endArray();
        return matrix;
    }

    /*
    Here we parse each the encoding array.

    Note that this func may throw IOExp, thus
    do use try catch block.
     */
    public ArrayList<Integer> readArray(JsonReader reader) throws IOException {
        ArrayList<Integer> line = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            //read each line
            line.add(reader.nextInt());
        }

        reader.endArray();
        return line;
    }


    public ArcadeDecoder(Context context, int fileIdentifier) {
        this.context = context;
        this.fileIdentifier = fileIdentifier;
    }
}
