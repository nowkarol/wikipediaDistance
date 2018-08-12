import crawler.Crawler;
import crawler.LinkedUrl;
import crawler.MultiThreadFJCrawler;
import downloader.CachedPageDownloader;
import downloader.PageDownloader;
import downloader.SimplePageDownloader;
import finder.CachedUrlFinder;
import finder.RegexUrlFinder;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Main {
    public static void main(String[] args) throws MalformedURLException {
        LocalTime start = LocalTime.now();

        final URL url = new URL("https://www.wp.pl");
        SimplePageDownloader downloader = new SimplePageDownloader();
        PageDownloader cachedDownloader = new CachedPageDownloader(downloader);
        RegexUrlFinder regexUrlFinder = new RegexUrlFinder();

        Crawler crawler = new MultiThreadFJCrawler(2, cachedDownloader, regexUrlFinder);
        List<LinkedUrl> result = crawler.crawl(url);

        result.forEach(System.out::println);

        LocalTime end = LocalTime.now();
        long time = ChronoUnit.MILLIS.between(start, end);
        System.out.format("%d urls was parsed in %d MS ", result.size(), time);
    }
}
