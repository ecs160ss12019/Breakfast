package com.example.pacman;

import java.io.FileReader;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;

public class BestScores {
    private String userOne;
    private int oneScore;
    private String userTwo;
    private int twoScore;
    private String userThree;
    private int threeScore;
    private String userFour;
    private int fourScore;


    public BestScores(String fileName){
        String json = "0";
        //read from a file and initialize these variables with name and score.
        JSONObject obj = new JSONObject(fileName);
        this.userOne = obj.getJSONObject("Name").getString("Scores");
        this.userTwo = obj.getJSONObject("Name").getString("Scores");
        this.userThree = obj.getJSONObject("Name").getString("Scores");
        this.userFour = obj.getJSONObject("Name").getString("Scores");
    }

    public void writeToFile(){
        JSONObject update = new JSONObject();
        update.put(userOne, oneScore);
        update.put(userTwo, twoScore);
        update.put(userThree, threeScore);
        update.put(userFour, threeScore);

        try (FileWriter file = new FileWriter("highscore.json")) {
            file.write(update);
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
