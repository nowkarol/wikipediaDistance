package crawler;

import java.net.URL;
import java.util.List;

public interface Crawler {
    List<LinkedUrl> crawl(URL url, String endingWord);
}