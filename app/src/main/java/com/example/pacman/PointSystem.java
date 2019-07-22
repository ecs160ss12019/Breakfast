package com.example.pacman;

public class PointSystem {
    private int total;
    private int pwrpoint = 5;
    private int point = 1;
    private boolean bonus;
    private int ghost = 20;

    public PointSystem(){
        this.total = 0;
        this.bonus = false;
    }
    public void resetScore(){
        this.total = 0;
        this.bonus = false;
    }
    public void pelletEaten(){
        if(bonus){
            this.total = this.total + 2*point;
        }else{
            this.total = this.total + this.point;
        }
    }
    public void updateScore(int type){
        if(type == 0){
            pwrpelletEaten();
        }else if(type == 1){
            pelletEaten();
        }
    }
    public void ghostEaten(){
        if(bonus){
            this.total = this.total + 2*ghost;
        }else{
            this.total = this.total + this.ghost;
        }
    }
    public void pwrpelletEaten(){
        if(bonus){
            this.total = this.total + 2*pwrpoint;
        }else{
            this.total = this.total + this.pwrpoint;
        }
    }
    public void cakeEaten(){
        this.bonus = true;
    }
    public int getScore(){
        return total;
    }

}
