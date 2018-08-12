package crawler;

import downloader.PageDownloader;
import finder.UrlFinder;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MultiThreadFJCrawler implements Crawler{
    private final int maxDepth;
    private final PageDownloader downloader;
    private final UrlFinder urlFinder;

    public MultiThreadFJCrawler(int maxDepth, PageDownloader downloader, UrlFinder urlFinder) {
        this.maxDepth = maxDepth;
        this.downloader = downloader;
        this.urlFinder = urlFinder;
    }

    @Override
    public List<LinkedUrl> crawl(URL rootUrl) {
        System.out.println("Depth:0 Crawling root page:" + rootUrl);
        LinkedUrl root = new LinkedUrl(rootUrl);
        List<LinkedUrl> result = crawlWithDepth(root, 1);
        result.add(root);
        return result;
    }


    private List<LinkedUrl> crawlWithDepth(LinkedUrl linkedUrl, int currentDepth) {
        URL url = linkedUrl.getUrl();

        if (currentDepth > maxDepth) return Collections.emptyList();
        int nextDepthLevel = currentDepth + 1;

        String pageContent = downloader.downloadContent(url);
        List<URL> urlsOnPage = urlFinder.findAll(pageContent);
        List<LinkedUrl> linkedUrls = linkedUrl.createFromParent(urlsOnPage);
        List<LinkedUrl> urlsFromLowerLevels = urlsOnPage.parallelStream()
                .map(processedUrl -> new LinkedUrl(processedUrl, linkedUrl))
                .peek(urlToProcess -> System.out.println("Depth:" + currentDepth + " Crawling page:" + urlToProcess + " reached by:" + url))
                .map(urlToProcess -> crawlWithDepth(urlToProcess, nextDepthLevel))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        linkedUrls.addAll(urlsFromLowerLevels);
        return linkedUrls;
    }
}
