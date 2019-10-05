package betClasses;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Raphael on 02/10/2019.
 */

public class Odds {
    private ArrayList<Double> oddsList;

    public Odds(ArrayList<Double> oddsList) {
        this.oddsList = new ArrayList<>(oddsList);
    }

    public Odds(Odds odds) {
        this.oddsList = new ArrayList<>(odds.getOddsList());
    }

    public void set(int i, double odd) {
        oddsList.set(i, odd);
    }

    public double get(int i) {
        return oddsList.get(i);
    }

    public ArrayList<Double> getOddsList() {
        return oddsList;
    }


    @Override
    public String toString() {
        return "Odds [oddsList=" + oddsList + "]";
    }

    /**
     * Calcule le retour attendu en couvrant toutes les issues
     */
    public double gain() {
        double s = 0;
        for (double odd : oddsList) {
            s += 1/odd;
        }
        return 1/s;
    }

    public Bets mises2(double mise_requise, int choix) {
        double gain = mise_requise*oddsList.get(choix);
        ArrayList<Double> mises = new ArrayList<>();
        for (double odd : oddsList) {
            mises.add(gain/odd);
        }
        Bets bets = new Bets(mises);
        bets.setCapitalGain(gain-bets.getSumBets());
        return bets;
    }

    public Bets mises_freebet(double freebet, int choix) {
        ArrayList<Double> oddsListFreebet = new ArrayList<>(oddsList);
        oddsListFreebet.set(choix, oddsList.get(choix)-1);
        Odds oddsFreebet = new Odds(oddsListFreebet);
        return oddsFreebet.mises2(freebet, choix);
    }

    public double pariRembourseSiPerdant(double miseMax, int rang, boolean freebet, double tauxRemboursement) {
        int rembFreebet =  freebet ? 1 : 0;
        int notRembFreebet = freebet ? 0 : 1;
        double taux = (notRembFreebet + 0.77*rembFreebet)*tauxRemboursement;
        double coteMax;
        double gains;
        int i = 0;
        if (rang == -1) {
            coteMax = Collections.max(oddsList);
            for (double odd : oddsList) {
                if (odd == coteMax) {
                    rang = i;
                    break;
                }
                i++;
            }
        }
        gains = miseMax*oddsList.get(rang);
        ArrayList<Double> mis = new ArrayList<>();
        i = 0;
        for (double odd : oddsList) {
            if (i == rang) {
                mis.add(miseMax);
            }
            else {
                mis.add((gains-miseMax*taux)/odd);
            }
            i++;
        }
        return gains-(new Bets(mis)).getSumBets();
    }
}
