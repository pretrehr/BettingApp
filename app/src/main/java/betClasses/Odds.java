package betClasses;

import java.util.ArrayList;

/**
 * Created by Raphael on 02/10/2019.
 */

public class Odds {
    private ArrayList<Double> oddsList;

    public Odds(ArrayList<Double> oddsList) {
        this.oddsList = oddsList;
    }

    public ArrayList<Double> getOddsList() {
        return oddsList;
    }

    public void setOddsList(ArrayList<Double> oddsList) {
        this.oddsList = oddsList;
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
        ArrayList<Double> mises = new ArrayList<Double>();
        for (double odd : oddsList) {
            mises.add(gain/odd);
        }
        Bets bets = new Bets(mises);
        bets.setCapitalGain(gain-bets.getSumBets());
        return bets;
    }

    public Bets mises_freebet(double freebet, int choix) {
        ArrayList<Double> oddsListFreebet = new ArrayList<Double>(oddsList);
        oddsListFreebet.set(choix, oddsList.get(choix)-1);
        Odds oddsFreebet = new Odds(oddsListFreebet);
        return oddsFreebet.mises2(freebet, choix);
    }
}
