package betClasses;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Raphael on 02/10/2019.
 */

public class Match {
    private Date date;
    private String opponents;


    public Match(String opponents, Date date) {
        this.opponents = opponents;
        this.date = date;
    }

    public Match() {
        this.date = Calendar.getInstance().getTime();
        this.opponents = "";
    }

    @Override
    public String toString() {
        return "Match [date=" + date + ", opponents=" + opponents + "]";
    }
}
