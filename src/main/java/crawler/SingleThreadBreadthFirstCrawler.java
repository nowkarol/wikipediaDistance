package crawler;

import downloader.PageDownloader;
import finder.UrlFinder;
import finder.WordFinder;

import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

public class SingleThreadBreadthFirstCrawler implements Crawler{
    private int counter = 0;
    private final PageDownloader downloader;
    private final UrlFinder urlFinder;
    private WordFinder wordFinder;

    public SingleThreadBreadthFirstCrawler(PageDownloader downloader, UrlFinder urlFinder, WordFinder wordFinder) {
        this.downloader = downloader;
        this.urlFinder = urlFinder;
        this.wordFinder = wordFinder;
    }

    @Override
    public List<LinkedUrl> crawl(URL rootUrl, String endingWord) {
        System.out.println("Depth:0 Crawling root page:" + rootUrl);
        LinkedUrl root = new LinkedUrl(rootUrl);
        return crawlWithDepth(root, endingWord);
    }

    @Override
    public int getCounter() {
        return counter;
    }


    private List<LinkedUrl> crawlWithDepth(LinkedUrl linkedUrl, String wordToFind) {
        List<LinkedUrl> urlsToProcess = new LinkedList<>();
        urlsToProcess.add(linkedUrl);
        Set<URL> processedUrls = new HashSet<>();

        while (!urlsToProcess.isEmpty()) {
            counter++;
            LinkedUrl currentLinkedUrl = urlsToProcess.remove(0);
            URL url = currentLinkedUrl.getUrl();
            System.out.println("Crawling page: " + currentLinkedUrl);

            if (processedUrls.contains(url)){
                continue;
            }

            String pageContent = downloader.downloadContent(url);
            if (wordFinder.wordAppears(pageContent, wordToFind)) {
                return singletonList(currentLinkedUrl);
            }

            List<URL> urlsOnPage = urlFinder.findAll(pageContent);
            List<LinkedUrl> urlsFromThisLevel = urlsOnPage.stream()
                    .map(processedUrl -> new LinkedUrl(processedUrl, currentLinkedUrl))
                    .collect(Collectors.toList());

            urlsToProcess.addAll(urlsFromThisLevel);
            processedUrls.add(url);
        }
        throw new IllegalStateException("Either you typed non existing start article " +
                "(watch case in names with multiple words) or you reached end of the internet.");
    }
}
