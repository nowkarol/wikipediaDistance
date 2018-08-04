import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class Crawler {
    private static final int DEFAULT_MAX_DEPTH_LEVEL = 10;
    private final int maxDepth;

    Crawler(){
        this(DEFAULT_MAX_DEPTH_LEVEL);
    }

    Crawler(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public List<LinkedUrl> crawl(Page page) {
        return crawlWithDepth(page, 0);
    }


    private List<LinkedUrl> crawlWithDepth(Page page, int depthLevel) {
        System.out.println("Depth:"+ depthLevel+" Crawling page:"+page);
        int nextDepthLevel = depthLevel + 1;
        if (depthLevel >= maxDepth) return Collections.emptyList();

        UrlFinder finder = new UrlFinder(page.getContent());
        List<URL> urlsOnPage = finder.findAll();
        List<LinkedUrl> urls = LinkedUrl.createFromParent(page.getUrl(), urlsOnPage);
        List<LinkedUrl> urlsToAdd = urlsOnPage.stream()
                .map(page::newPageWithSameDownloader)
                .map(newPage -> crawlWithDepth(newPage, nextDepthLevel))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        urls.addAll(urlsToAdd);
        return urls;
    }
}
