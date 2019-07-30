package com.example.pacman;
/*
    This is a object that will keep all
    of the scores of the user and created in
    PacmanGame objects

 */
public class PointSystem {
    private int total;
    private int pwrpoint = 10;
    private int point = 5;
    private boolean bonus;
    private int ghost = 50;
    private int cake = 100;
    private GameObjectTimer cakeTimer;

    public PointSystem(){
        this.total = 0;
        this.bonus = false;
    }

    //reset the score when the user
    //start a new game.
    public void resetScore(){
        this.total = 0;
        this.bonus = false;
    }

    //Check bonus, otherwise just add points
    public void pelletEaten(){
        if(bonus){
            this.total = this.total + 2*point;
        }else{
            this.total = this.total + this.point;
        }
    }

    //Update the score by differet types
    public void updateScore(int type){
        if(type == 0){
            pwrpelletEaten();
        }else if(type == 1){
            pelletEaten();
        }
    }

    //Update the score once the ghost is eaten
    public void ghostEaten(){
        if(bonus){
            this.total = this.total + 2*ghost;
        }else{
            this.total = this.total + this.ghost;
        }
    }

    //Update the score once the pellet is eaten
    public void pwrpelletEaten(){
        if(bonus){
            this.total = this.total + 2*pwrpoint;
        }else{
            this.total = this.total + this.pwrpoint;
        }
    }

    //Update the score once the cake is eaten
    public void cakeEaten(){

        this.bonus = true;
        total = total + cake;
    }
    //Return the score for displaying
    public int getScore(){
        return total;
    }

}
