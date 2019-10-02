package com.example.android.bettingapp;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Created by Raphael on 02/10/2019.
 */

public class Match {

    private Odds odds;
    private LocalDateTime date;
    private ArrayList<String> opponents;


    public Match(ArrayList<String> opponents, Odds odds, LocalDateTime date) {
        this.opponents = opponents;
        this.odds = odds;
        this.date = date;
    }
}
