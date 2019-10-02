package betClasses;

import java.util.ArrayList;

/**
 * Created by Raphael on 03/10/2019.
 */

public class Bets {
    private ArrayList<Double> bets;
    private double gainOnFreebet;
    private double sumBets;
    private double capitalGain;

    public ArrayList<Double> getBets() {
        return bets;
    }

    public void setBets(ArrayList<Double> bets) {
        this.bets = bets;
    }

    public double getGainOnFreebet() {
        return gainOnFreebet;
    }

    public void setGainOnFreebet(double gainOnFreebet) {
        this.gainOnFreebet = gainOnFreebet;
    }

    public double getSumBets() {
        return sumBets;
    }

    public double getCapitalGain() {
        return capitalGain;
    }

    public void setCapitalGain(double capitalGain) {
        this.capitalGain = capitalGain;
    }

    public Bets(ArrayList<Double> bets) {
        this.bets = bets;
        double s = 0;
        for (double bet : bets) s += bet;
        this.sumBets = s;
    }
}
