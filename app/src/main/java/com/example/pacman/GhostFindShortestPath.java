package com.example.pacman;

import java.lang.*;



public class GhostFindShortestPath {

    // needs the position of pacman, ghost
    // need the arcade information
    // need to test the direction is valid or not
    public GhostFindShortestPath(){
        //get the valid direction by calling "FindTheValidDirrection"
        //check each direction's Distance to the pacman by calling "FindDistancetoPacman"
        //move to the direction that has the smallest distance to pacman
    }

    public void EscapeFromPacman(){
        //it is the same as "FindDistancetoPacman"
        //but instead of move to the smallest distance to pacman
        //we move to the largest distance from the valid direction
    }


    // Find the Valid direction at the current position
    public void FindTheValidDirrection (){
        //get the current position of ghost
        //check all four direction
        // store the valid direcrion
    }

    public void FindDistancetoPacman(){
        //sqare root of (Vertical absolute sqare + Horizontal absolute square)
    }
}
