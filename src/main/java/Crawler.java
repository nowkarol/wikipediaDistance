import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class Crawler {
    private final int maxDepth;
    private final PageDownloader downloader;
    private static final int DEFAULT_DEPTH = 10;

    Crawler(int maxDepth, PageDownloader downloader) {
        this.maxDepth = maxDepth;
        this.downloader = downloader;
    }

    public List<LinkedUrl> crawl(URL url) {
        return crawlWithDepth(url, 0);
    }


    private List<LinkedUrl> crawlWithDepth(URL url, int depthLevel) {
        int nextDepthLevel = depthLevel + 1;
        if (depthLevel >= maxDepth) return Collections.emptyList();

        String pageContent = downloader.downloadContent(url);
        UrlFinder finder = new UrlFinder(pageContent);
        List<URL> urlsOnPage = finder.findAll();
        List<LinkedUrl> urls = LinkedUrl.createFromParent(url, urlsOnPage);
        List<LinkedUrl> urlsToAdd = urlsOnPage.stream()
                .peek(urlToProcess -> System.out.println("Depth:"+ depthLevel+" Crawling page:"+urlToProcess + " reached by:"+url))
                .map(urlToProcess -> crawlWithDepth(urlToProcess, nextDepthLevel))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        urls.addAll(urlsToAdd);
        return urls;
    }
}
