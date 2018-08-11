package crawler;

import downloader.PageDownloader;
import finder.UrlFinder;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;


public class ForkJoinCrawler extends RecursiveTask<List<LinkedUrl>> implements Crawler {
    private static final int DEFAULT_THRESHOLD = 5;
    private final int threshold;
    private final int maxDepth;
    private final PageDownloader downloader;
    private final UrlFinder urlFinder;

    private List<LinkedUrl> urlsToProcess;
    private final int currentDepth;

    private ForkJoinCrawler(int maxDepth, PageDownloader downloader, UrlFinder urlFinder, List<LinkedUrl> urlsToProcess, int currentDepth, int threshold) {
        this.maxDepth = maxDepth;
        this.downloader = downloader;
        this.urlFinder = urlFinder;
        this.urlsToProcess = urlsToProcess;
        this.currentDepth = currentDepth;
        this.threshold = threshold;
        //TODO reduce number of parameters
    }

    private ForkJoinCrawler(ForkJoinCrawler baseCrawler, List<LinkedUrl> urlsToProcess) {
        this(baseCrawler.maxDepth, baseCrawler.downloader, baseCrawler.urlFinder, urlsToProcess, baseCrawler.currentDepth + 1, baseCrawler.threshold);
    }

    public ForkJoinCrawler(int maxDepth, PageDownloader downloader, UrlFinder urlFinder, int threshold) {
        this(maxDepth, downloader, urlFinder, Collections.emptyList(), 1, threshold);
    }

    public ForkJoinCrawler(int maxDepth, PageDownloader downloader, UrlFinder urlFinder) {
        this(maxDepth, downloader, urlFinder, Collections.emptyList(), 1, DEFAULT_THRESHOLD);
    }


    @Override
    public List<LinkedUrl> crawl(URL url) {
        this.urlsToProcess = new ArrayList<>();
        urlsToProcess.add(new LinkedUrl(url));
        return crawl();
    }

    @Override
    protected List<LinkedUrl> compute() {
        return crawl();
    }

    private List<LinkedUrl> crawl() {
        if (currentDepth > maxDepth) return urlsToProcess;

        if (urlsToProcess.size() > threshold) {
            int lastIndexIncremented = urlsToProcess.size();
            int midIndex = (lastIndexIncremented / 2);
            List<LinkedUrl> firstPart = new ArrayList<>(urlsToProcess.subList(0, midIndex));
            //midIndex excluded
            ForkJoinCrawler baseCrawler = new ForkJoinCrawler(this, firstPart);
            baseCrawler.fork();

            List<LinkedUrl> secondPart = new ArrayList<>(urlsToProcess.subList(midIndex, lastIndexIncremented));
            //lastIndexIncremented excluded
            List<LinkedUrl> secondResult = new ForkJoinCrawler(this, secondPart).compute();
            List<LinkedUrl> result = baseCrawler.join();
            result.addAll(secondResult);
            return result;
        } else {
            SingleThreadCrawler singleThreadCrawler = new SingleThreadCrawler(this.maxDepth, this.downloader, this.urlFinder);
            return urlsToProcess.stream()
                    .map(LinkedUrl::getUrl)
                    .map(singleThreadCrawler::crawl)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
        }
    }
}
