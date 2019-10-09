package Parser;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import betClasses.Match;
import betClasses.Odds;

/**
 * Created by Raphael on 03/10/2019.
 */

public class Page {
    private String url;
    private boolean is1N2 = true;
    private boolean isBasketball = false;
    private ArrayList<Match> surebets = new ArrayList<>();
    private HashMap<String, Match> matches = new HashMap<>();
    private String competitionName = "";
    private String sport = "";

    public Page(String url) {
        this.url = url;
    }

    public HashMap<String, HashMap<String, Odds>> parse() {
        return parse(new ArrayList<String>());
    }


    public HashMap<String, HashMap<String, Odds>> parse(ArrayList<String> particularSites) {
        int n;
        if (is1N2) {
            n = 3;
        }
        else {
            n = 2;
        }
        Document document;
        try {
            document = Jsoup.connect(url).get();
        }
        catch (IOException e) {
            File input = new File(url);
            try {
                document = Jsoup.parse(input, "UTF-8");
            } catch (IOException e1) {
                throw new IllegalArgumentException();
            }
        }
        String[] splitTitle = document.title().split(" ");
        this.sport = splitTitle[splitTitle.length-1];
        this.competitionName = document.title().split("Comparer les cotes pour ")[1].split(" "+sport)[0];
        this.isBasketball = sport.equals("Basketball");
        this.is1N2 = !(sport.equals("Tennis") || sport.equals("Volleyball"));
        HashMap<String, HashMap<String, Odds>> matchOddsHash = new HashMap<>();
        int countTeams = 0;
        int countOdds = 0;
        Match match;
        String matchOpponents = "";
        boolean surebet = false;
        boolean sitesInDict;
        String dateString;
        String siteString = "";
        ArrayList<Double> oddsList = new ArrayList<>();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(" EEEE dd MMMM yyyy 'à' hh'h'mm ", Locale.FRENCH);
        ArrayList<Match> surebetMatches = new ArrayList<>();
        for (Element line : document.select("a, td, img")) {
            if (line.nodeName().equals("a") && line.hasAttr("class")) {
                if (countTeams == 0) {
                    sitesInDict = true;
                    if (!matchOpponents.isEmpty()) {
                        for (String site : particularSites) {
                            if (!matchOddsHash.get(matchOpponents).containsKey(site)) {
                                sitesInDict = false;
                                break;
                            }
                        }
                        if (matchOddsHash.get(matchOpponents).isEmpty() || !sitesInDict) {
                            matchOddsHash.remove(matchOpponents);
                        }
                    }
                    matchOpponents = "";
                }
                matchOpponents+=line.text();
                if (countTeams == 0) {
                    matchOpponents+=" - ";
                    countTeams++;
                } else {
                    match = new Match(matchOpponents, date);
                    matchOddsHash.put(matchOpponents, new HashMap<String, Odds>());
                    matches.put(matchOpponents, match);
                    countTeams = 0;
                    if (line.parent().parent().attr("class").contains("surebetbox")) {
                        surebet = true;
                        surebetMatches.add(match);
                    }
                }
            }
            else if (line.hasAttr("src")) {
                if (line.attr("src").contains("logop")) {
                    siteString = line.attr("src").split("-")[1].split("\\.")[0];
                }
            }
            else if (line.nodeName().equals("td")
                    && line.parent().parent().parent().hasAttr("class")
                    && line.parent().parent().parent().hasClass("bettable")
                    && line.text().contains("à")) {
                dateString = line.textNodes().get(3).toString();
                if (dateString.equals(" ")) {
                    dateString = line.textNodes().get(1).toString();
                }
                try {
                    date = simpleDateFormat.parse(dateString);
                } catch (ParseException e) {
                    throw new IllegalArgumentException();
                }
            }
            else if (line.hasAttr("class") && line.hasClass("bet")) {
                if (particularSites.isEmpty() || particularSites.contains(siteString)) {
                    if (!line.text().contains("-")) {
                        oddsList.add(Double.parseDouble(line.text()));
                    }
                    else {
                        oddsList.add(-1.0);
                    }
                    if (countOdds < n-1) {
                        countOdds++;
                    }
                    else {
                        if (isBasketball) {
                            if (!oddsList.contains(-1.0)) {
                                oddsList.set(0, oddsList.get(0)/1.1);
                                oddsList.set(2, oddsList.get(2)/1.1);
                            }
                            oddsList.remove(1);
                        }
                        matchOddsHash.get(matchOpponents).put(siteString, new Odds(oddsList));
                        countOdds = 0;
                        oddsList.clear();
                    }
                }
            }
        }
        sitesInDict = true;
        for (String site : particularSites) {
            if (!matchOddsHash.get(matchOpponents).containsKey(site)) {
                sitesInDict = false;
                break;
            }
        }
        if ((!matchOpponents.isEmpty() && !matchOddsHash.get(matchOpponents).isEmpty()) || !sitesInDict) {
            matchOddsHash.remove(matchOpponents);
        }
        if (surebet) {
            this.surebets = surebetMatches;
        }
        return matchOddsHash;
    }

    public HashMap<String, Match> getMatches() {
        return matches;
    }

    public ArrayList<Match> getSurebets() {
        return surebets;
    }

    public String getSport() {
        return sport;
    }

    public String getCompetitionName() {
        return competitionName;
    }
}
