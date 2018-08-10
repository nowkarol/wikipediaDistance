import downloader.SimplePageDownloader;
import finder.NaiveUrlFinder;
import finder.RegexUrlFinder;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Main {
    public static void main(String[] args) throws MalformedURLException {
        LocalTime start = LocalTime.now();
        final URL url = new URL("http://google.pl");
        Crawler crawler = new Crawler(3, new SimplePageDownloader(), new RegexUrlFinder());
        crawler.crawl(url).forEach(System.out::println);
        LocalTime end = LocalTime.now();
        long time = ChronoUnit.MILLIS.between(start, end);
        System.out.println(time);
    }
}
