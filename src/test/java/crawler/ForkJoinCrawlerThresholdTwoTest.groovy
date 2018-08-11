package crawler

import downloader.PageDownloader
import finder.UrlFinder

class ForkJoinCrawlerThresholdTwoTest extends CrawlerTest {
    @Override
    Crawler getCrawler(int maxDepth, PageDownloader downloader, UrlFinder urlFinder) {
        return new ForkJoinCrawler(maxDepth, downloader, urlFinder, 2)
    }
}