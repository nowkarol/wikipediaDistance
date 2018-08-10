import downloader.PageDownloader;
import finder.UrlFinder;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class Crawler {
    private final int maxDepth;
    private final PageDownloader downloader;
    private final UrlFinder urlFinder;

    Crawler(int maxDepth, PageDownloader downloader, UrlFinder urlFinder) {
        this.maxDepth = maxDepth;
        this.downloader = downloader;
        this.urlFinder = urlFinder;
    }

    public List<LinkedUrl> crawl(URL url) {
        return crawlWithDepth(url, 0);
    }


    private List<LinkedUrl> crawlWithDepth(URL url, int depthLevel) {
        int nextDepthLevel = depthLevel + 1;
        if (depthLevel >= maxDepth) return Collections.emptyList();

        String pageContent = downloader.downloadContent(url);
        List<URL> urlsOnPage = urlFinder.findAll(pageContent);
        List<LinkedUrl> linkedUrls = LinkedUrl.createFromParent(url, urlsOnPage);
        List<LinkedUrl> urlsFromLowerLevels = urlsOnPage.stream()
                .peek(urlToProcess -> System.out.println("Depth:"+ depthLevel+" Crawling page:"+urlToProcess + " reached by:"+url))
                .map(urlToProcess -> crawlWithDepth(urlToProcess, nextDepthLevel))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        linkedUrls.addAll(urlsFromLowerLevels);
        return linkedUrls;
    }
}
