import crawler.Crawler;
import crawler.LinkedUrl;
import crawler.MultiThreadFJCrawler;
import downloader.CachedDownloaderAndFinder;
import downloader.SimplePageDownloader;
import finder.RegexUrlFinder;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Main {
    public static void main(String[] args) throws MalformedURLException {
        LocalTime start = LocalTime.now();

        final URL url = new URL("https://www.google.pl");
        SimplePageDownloader downloader = new SimplePageDownloader();
        RegexUrlFinder regexUrlFinder = new RegexUrlFinder();

        CachedDownloaderAndFinder cachedDownloaderAndFinder = new CachedDownloaderAndFinder(downloader, regexUrlFinder);

        Crawler crawler = new MultiThreadFJCrawler(4, cachedDownloaderAndFinder, cachedDownloaderAndFinder);
        List<LinkedUrl> result = crawler.crawl(url);

        result.forEach(System.out::println);

        LocalTime end = LocalTime.now();
        long time = ChronoUnit.MILLIS.between(start, end);
        System.out.format("%d urls was parsed in %d MS ", result.size(), time);
    }
}
