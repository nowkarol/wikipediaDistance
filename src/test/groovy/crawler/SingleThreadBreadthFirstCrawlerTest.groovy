package crawler

import downloader.PageDownloader
import finder.UrlFinder
import finder.WordFinder

class SingleThreadBreadthFirstCrawlerTest extends CrawlerTest{
    @Override
    Crawler getCrawler(PageDownloader downloader, UrlFinder urlFinder, WordFinder wordFinder) {
        return new SingleThreadBreadthFirstCrawler(downloader, urlFinder, wordFinder)
    }
}