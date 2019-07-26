package com.example.pacman;

public class Record {
    public String userName;
    public int score;


    //Constructor
    public Record() {

    }

    //Constructor2
    public Record(final String userName, final int score) {
        this.userName = userName;
        this.score = score;
    }
    public String getRecordName(){
        return userName;
    }
    public int getRecord(){
        return score;
    }
}
