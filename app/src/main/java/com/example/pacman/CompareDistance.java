package com.example.pacman;

import java.util.Comparator;

public class CompareDistance implements Comparator<ComparableDirection> {

    @Override
    public int compare(ComparableDirection d1, ComparableDirection d2) {
        if (d1.getDistance() < d2.getDistance()) {
            return -1;
        }

        return 1;
    }

}
