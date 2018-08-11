package crawler

import downloader.PageDownloader
import finder.UrlFinder

class SingleThreadCrawlerTest extends CrawlerTest{
    @Override
    Crawler getCrawler(int maxDepth, PageDownloader downloader, UrlFinder urlFinder) {
        return new SingleThreadCrawler(maxDepth, downloader, urlFinder)
    }
}