package com.example.pacman;

import java.io.FileReader;
import android.content.Context;
import android.util.JsonReader;

import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;



public class BestScores {
    private String userOne;
    private int oneScore;
    private String userTwo;
    private int twoScore;
    private String userThree;
    private int threeScore;
    private String userFour;
    private int fourScore;
    public static final String JSON_FILE="highscore.json";
    private Context context;
    private int fileIdentifier;

    public BestScores(){
        //read from a file and initialize these variables with name and score.



    }



    public void writeToFile(){

    }

    //public static final String JSON_FILE="highscore.json";

    public void jsonRead() throws IOException{
        InputStream in = context.getResources().openRawResource(fileIdentifier);
        // Create JsonReader from Json.
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        //JSONObject obj = reader.beginArray();
        //reader.close();
        try {
            reader.beginArray();
            // read integer
            while (reader.hasNext()) {
                String name = reader.nextName();
                if(name.equals("Name"))
                    System.out.println("Name: " + reader.nextName());
                else if(name.equals("Scores"))
                    System.out.println("Scores: " + reader.nextInt());
            reader.endArray();
            }
        }catch (Exception e) {
            System.out.println("Failed: " + e.getMessage());
        }
        }


}





