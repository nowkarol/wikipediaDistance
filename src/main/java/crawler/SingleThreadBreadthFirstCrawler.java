package crawler;

import downloader.PageDownloader;
import finder.CachedWordFinder;
import finder.UrlFinder;
import finder.SimpleWordFinder;
import finder.WordFinder;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

public class SingleThreadBreadthFirstCrawler implements Crawler{
    public static int counter = 0;
    private final PageDownloader downloader;
    private final UrlFinder urlFinder;
    private CachedWordFinder wordFinder;

    public SingleThreadBreadthFirstCrawler(PageDownloader downloader, UrlFinder urlFinder, WordFinder wordFinder) {
        this.downloader = downloader;
        this.urlFinder = urlFinder;
        this.wordFinder = new CachedWordFinder(new SimpleWordFinder());
    }

    @Override
    public List<LinkedUrl> crawl(URL rootUrl, String endingWord) {
        System.out.println("Depth:0 Crawling root page:" + rootUrl);
        LinkedUrl root = new LinkedUrl(rootUrl);
        return crawlWithDepth(root, endingWord);
    }


    private List<LinkedUrl> crawlWithDepth(LinkedUrl linkedUrl, String wordToFind) {
        List<LinkedUrl> urlsToProcess = new ArrayList<>();
        urlsToProcess.add(linkedUrl);
        List<URL> processedUrls = new ArrayList<>();

        while (!urlsToProcess.isEmpty()) {
            counter++;
            LinkedUrl currentLinkedUrl = urlsToProcess.get(0);
            URL url = currentLinkedUrl.getUrl();
            urlsToProcess.remove(0);
            if (processedUrls.contains(url)){
                continue;
            }

            String pageContent = downloader.downloadContent(url);
            if (wordFinder.wordAppears(pageContent, wordToFind)) {
                return singletonList(currentLinkedUrl);
            }

            List<URL> urlsOnPage = urlFinder.findAll(pageContent);
            List<LinkedUrl> urlsFromThisLevel = urlsOnPage.stream()
                    .unordered()
                    .map(processedUrl -> new LinkedUrl(processedUrl, currentLinkedUrl))
                    .peek(urlToProcess -> System.out.println("Crawling page: " + urlToProcess))
                    .collect(Collectors.toList());

            urlsToProcess.addAll(urlsFromThisLevel);
            processedUrls.add(url);
        }
        throw new IllegalStateException("Either you typed non existing start article " +
                "(watch case in names with multiple words) or You reached end of internet.");
    }
}
