package Parser;

import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by Raphael on 09/10/2019.
 */

public class Sport {
    private String sport;

    public Sport(String sport) {
        this.sport = sport;
    }

    public ArrayList<String> getURLs() {
        String url = "http://www.comparateur-de-cotes.fr/comparateur/"+sport;
        Document document;
        ArrayList<String> urls = new ArrayList<>();
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
        for (Element line : document.select("a[href]")) {
            if (line.attr("href").contains(sport) && line.attr("href").contains("ed")) {
                urls.add("http://www.comparateur-de-cotes.fr/"+line.attr("href"));
            }
        }
        return urls;
    }
}
