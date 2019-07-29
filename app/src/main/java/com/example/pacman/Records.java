package com.example.pacman;

import android.content.Context;
import android.graphics.Canvas;
import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

/*
    This class keeps the highest four scores from
    the users, everytime the user starts the game,
    this class will init and read from the json file
    called "highscore.json, and assign the name and
    scores from the file to record class.

    After user finishes every single trial on game,
    it will automatically compare user's score
    to the records, if it is excees any of them,
    it will automaticallt pop the lowest the score.
    And add this new score, and sort them and display
    them in the game over page after the user have entered
    his name.
 */
public class Records{
    private Context context;
    private Stack<Record> records;
    //TODO draw to game over page
    public void draw(Canvas canvas) {

    }
    public void printRecord() {
        for (Record record : records) {
            System.out.println("Name: " + record.userName);
            System.out.println("Score: " + record.score);
        }
    }
    //compare and update the highest record.
    //pop one of the old and low scores.
    public void updateHighest(final Record newHigh) {

    }

    private void read() throws IOException {
        /*
        It will make the code real hard to
        maintain if we use resource name String instead of
        resource identifier.
        We read the json file as a char stream from the
        resource folder, then parse using the json reader
         */
        InputStream in = context.getResources().openRawResource(R.raw.highscore);
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

        /*
        pass the Json reader in
        to decode in decodeArcades.
        We don't do it here, otherwise
        the code will be real ugly.
         */
        try{
            readRecords(reader);
        } finally {
            reader.close();
        }
    }
    //reading from the json file, with throwing exceptions.
    private void readRecords(JsonReader reader) throws IOException {
        reader.beginArray();

        while (reader.hasNext()) {
            this.records.add(readRecord(reader));
        }

        reader.endArray();
    }
    //This is simplying creating the new record object by
    //creating by reading the username and score.
    private Record readRecord(JsonReader reader) throws IOException {

        String userName = "";
        int score = 0;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();

            if (name.equals("Name")) {
                userName = reader.nextString();
            } else if (name.equals("Scores")) {
                score = reader.nextInt();
            } else {
                reader.skipValue();
            }
        }

        reader.endObject();

        return new Record(userName, score);
    }

    //Constructor
    public Records(final Context context) {
        this.context = context;
        this.records = new Stack<>();

        try {
            this.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //records.sort();
    }
}
