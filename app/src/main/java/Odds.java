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
}
