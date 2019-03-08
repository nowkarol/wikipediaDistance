package main;

import crawler.Crawler;
import crawler.LinkedUrl;
import crawler.SingleThreadBreadthFirstCrawler;
import downloader.PageDownloader;
import downloader.SimplePageDownloader;
import finder.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class WikipediaErdosNumber {
    private static final String WIKIPEDIA = "https://pl.wikipedia.org/wiki/";

    public static void main(String[] args) throws MalformedURLException {
        if (args.length != 2){
            System.out.println("Usage: \"Starting Article Title\" \"Searched Phrase\" ");
            System.out.println("For example: \"Ryszard Rynkowski\" \"Aikido\" ");
            return;
        }
        String startingArticle = handleCaseAndSpaces(args[0]);
        LocalTime startTime = LocalTime.now();

        final URL startingUrl = new URL(WIKIPEDIA + startingArticle);
        PageDownloader downloader = new SimplePageDownloader();
        UrlFinder regexUrlFinder = new RegexUrlFinder();
        WordFinder wordFinder = new CachedWordFinder(new SimpleWordFinder());


        Crawler crawler = new SingleThreadBreadthFirstCrawler(downloader, regexUrlFinder, wordFinder);
        LinkedUrl result = crawler.crawl(startingUrl, args[1]).get(0);
        LocalTime endTime = LocalTime.now();
        displayResult(startTime, endTime, result, crawler.getCounter(), downloader.getMbDownloaded());
    }

    private static void displayResult(LocalTime start, LocalTime end, LinkedUrl result, int pageCounter, int mbDownloaded) {
        long time = ChronoUnit.MILLIS.between(start, end);
        System.out.println();
        System.out.println();
        System.out.format("Found Path with depth %d in %d MS after looking at %d pages and downloading %d MB\n\n"
                , result.getDepth(), time, pageCounter, mbDownloaded);
        System.out.println("=================================================================================");
        System.out.println(result);
    }

    private static String handleCaseAndSpaces(String articleName) {
        String caseFine = articleName.substring(0, 1).toUpperCase() + articleName.substring(1);
        return caseFine.replace(' ', '_');
    }
}
