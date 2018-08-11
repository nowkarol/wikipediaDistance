import downloader.CachedPageDownloader;
import downloader.PageDownloader;
import downloader.SimplePageDownloader;
import finder.RegexUrlFinder;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Main {
    public static void main(String[] args) throws MalformedURLException {
        LocalTime start = LocalTime.now();
        final URL url = new URL("http://google.pl");
        SimplePageDownloader downloader = new SimplePageDownloader();
        PageDownloader cachedDownloader = new CachedPageDownloader(downloader);
        Crawler crawler = new Crawler(3, cachedDownloader, new RegexUrlFinder());
        crawler.crawl(url).forEach(System.out::println);
        LocalTime end = LocalTime.now();
        long time = ChronoUnit.MILLIS.between(start, end);
        System.out.println(time);
    }
}
