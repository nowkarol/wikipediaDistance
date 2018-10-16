package crawler

import downloader.PageDownloader
import finder.UrlFinder

class MultiThreadFJCrawlerTest extends CrawlerTest{
    @Override
    Crawler getCrawler(int maxDepth, PageDownloader downloader, UrlFinder urlFinder) {
        return new MultiThreadFJCrawler(maxDepth, downloader, urlFinder)
    }
}