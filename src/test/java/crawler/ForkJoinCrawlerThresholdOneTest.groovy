package crawler

import downloader.PageDownloader
import finder.UrlFinder

class ForkJoinCrawlerThresholdOneTest extends CrawlerTest {
    @Override
    Crawler getCrawler(int maxDepth, PageDownloader downloader, UrlFinder urlFinder) {
        return new ForkJoinCrawler(maxDepth, downloader, urlFinder, 1)
    }
}