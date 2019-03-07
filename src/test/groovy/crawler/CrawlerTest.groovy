package crawler

import downloader.PageDownloader
import finder.RegexUrlFinder
import finder.SimpleWordFinder
import finder.UrlFinder
import finder.WordFinder
import spock.lang.Ignore
import spock.lang.Specification

abstract class CrawlerTest extends Specification {
    abstract Crawler getCrawler(PageDownloader downloader, UrlFinder urlFinder, WordFinder wordFinder)

    @Ignore
    //change test after change of behavior
    def "should follow url one level deep and create three LinkedUrls"() {
        given:
        def sourceUrl = new URL("https://pl.wikipedia.org/wiki/Wikipedia")

        when:

        List<LinkedUrl> result = getCrawler(stubPageDownloader(), new RegexUrlFinder(), new SimpleWordFinder())
                .crawl(sourceUrl, "Encyklopedia")

        then:
        result.size() == 3
        result.contains new LinkedUrl("http://www.first.pl")
        result.contains new LinkedUrl(new URL("http://www.secondA.pl"), new LinkedUrl("http://www.first.pl"))
        result.contains new LinkedUrl(new URL("http://www.secondB.pl"), new LinkedUrl("http://www.first.pl"))
    }

    @Ignore
    //change test after change of behavior
    def "should follow url two level deep and create seven LinkedUrls"() {
        given:
        def sourceUrlString = "http://www.first.pl"
        def sourceUrl = new URL(sourceUrlString)

        when:
        List<LinkedUrl> result = getCrawler(stubPageDownloader(), new RegexUrlFinder(), new SimpleWordFinder())
                .crawl(sourceUrl, "Encyklopedia")

        then:
        result.size() == 7
        result.contains new LinkedUrl("http://www.first.pl")
        result.contains new LinkedUrl("http://www.secondA.pl", sourceUrlString)
        result.contains new LinkedUrl("http://www.secondB.pl", sourceUrlString)
        result.contains new LinkedUrl("http://www.thirdA.pl", new LinkedUrl("http://www.secondA.pl", sourceUrlString))
        result.contains new LinkedUrl("http://www.thirdA.pl", new LinkedUrl("http://www.secondB.pl", sourceUrlString))
        result.contains new LinkedUrl("http://www.thirdB.pl", new LinkedUrl("http://www.secondA.pl", sourceUrlString))
        result.contains new LinkedUrl("http://www.thirdB.pl", new LinkedUrl("http://www.secondB.pl", sourceUrlString))
    }

    @Ignore
    //change test after change of behavior
    def "should follow url three level deep and create eleven LinkedUrls"() {
        given:
        def sourceUrlString = "http://www.first.pl"
        def sourceUrl = new URL(sourceUrlString)

        when:
        List<LinkedUrl> result = getCrawler(stubPageDownloader(), new RegexUrlFinder(), new SimpleWordFinder())
                .crawl(sourceUrl, "Encyklopedia")

        then:
        result.size() == 11
        result.contains new LinkedUrl("http://www.first.pl")
        result.contains new LinkedUrl("http://www.secondA.pl", sourceUrlString)
        result.contains new LinkedUrl("http://www.secondB.pl", sourceUrlString)
        result.contains new LinkedUrl("http://www.thirdA.pl", new LinkedUrl("http://www.secondA.pl", sourceUrlString))
        result.contains new LinkedUrl("http://www.thirdA.pl", new LinkedUrl("http://www.secondB.pl", sourceUrlString))
        result.contains new LinkedUrl("http://www.thirdB.pl", new LinkedUrl("http://www.secondA.pl", sourceUrlString))
        result.contains new LinkedUrl("http://www.thirdB.pl", new LinkedUrl("http://www.secondB.pl", sourceUrlString))
        result.contains new LinkedUrl("http://www.fourth.pl",
                new LinkedUrl("http://www.thirdA.pl", new LinkedUrl("http://www.secondA.pl", sourceUrlString)))

        result.contains new LinkedUrl("http://www.fourth.pl",
                new LinkedUrl("http://www.thirdB.pl", new LinkedUrl("http://www.secondA.pl", sourceUrlString)))

        result.contains new LinkedUrl("http://www.fourth.pl",
                new LinkedUrl("http://www.thirdA.pl", new LinkedUrl("http://www.secondB.pl", sourceUrlString)))

        result.contains new LinkedUrl("http://www.fourth.pl",
                new LinkedUrl("http://www.thirdB.pl", new LinkedUrl("http://www.secondB.pl", sourceUrlString)))

    }

    @Ignore
    //TODO ensure that single link is downloaded only once
    def "should not follow circular link"() {
        given:
        def sourceUrl = new URL("http://www.first.pl")
        PageDownloader stubDownloaderWithCycle = new PageDownloader() {
            @Override
            String downloadContent(URL url) {
                if (url.equals(new URL("http://www.first.pl"))) {
                    return '"http://www.second.pl"'
                }

                if (url.equals(new URL("http://www.second.pl"))) {
                    return '"http://www.first.pl"'
                }
                return ""

            }
        }
        when:
        List<LinkedUrl> result = getCrawler(stubDownloaderWithCycle, new RegexUrlFinder(), new SimpleWordFinder())
                .crawl(sourceUrl, "Encyklopedia")

        then:
        result.size() == 2
        result.contains new LinkedUrl("http://www.second.pl", "http://www.first.pl")
        result.contains new LinkedUrl("http://www.first.pl", "http://www.second.pl")
    }

    PageDownloader stubPageDownloader(){
        return new PageDownloader() {
            @Override
            String downloadContent(URL url) {
                if (url.equals(new URL("http://www.first.pl"))) {
                    return '"http://www.secondA.pl" garbage "http://www.secondB.pl"'
                }

                if (url.equals(new URL("http://www.secondA.pl")) || url.equals(new URL("http://www.secondB.pl"))) {
                    return '"http://www.thirdA.pl" {"not important"} "http://www.thirdB.pl"'
                }

                if (url.equals(new URL("http://www.thirdA.pl")) || url.equals(new URL("http://www.thirdB.pl"))) {
                    return '"http://www.fourth.pl"'
                }

                if (url.equals(new URL("http://www.fourth.pl"))) {
                    return 'garbage'
                }
                return ""
            }
        }

    }
}
