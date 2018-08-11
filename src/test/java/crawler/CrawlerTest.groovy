package crawler

import downloader.PageDownloader
import finder.RegexUrlFinder
import finder.UrlFinder
import spock.lang.Ignore
import spock.lang.Specification

abstract class CrawlerTest extends Specification {
    abstract Crawler getCrawler(int maxDepth, PageDownloader downloader, UrlFinder urlFinder)

    def "should follow url from Page only one level deep and create two LinkedUrls if deep level is set to one"() {
        given:
        def sourceUrl = new URL("http://www.first.pl")

        when:

        List<LinkedUrl> result = getCrawler(1, stubPageDownloader(), new RegexUrlFinder()).crawl(sourceUrl)

        then:
        result.size() == 2
        result.contains new LinkedUrl(new URL("http://www.secondA.pl"), new LinkedUrl("http://www.first.pl"))
        result.contains new LinkedUrl(new URL("http://www.secondB.pl"), new LinkedUrl("http://www.first.pl"))
    }

    def "should follow url from Page two level deep and create six LinkedUrls"() {
        given:
        def sourceUrlString = "http://www.first.pl"
        def sourceUrl = new URL(sourceUrlString)

        when:
        List<LinkedUrl> result = getCrawler(2, stubPageDownloader(), new RegexUrlFinder()).crawl(sourceUrl)

        then:
        result.size() == 6
        result.contains new LinkedUrl("http://www.secondA.pl", sourceUrlString)
        result.contains new LinkedUrl("http://www.secondB.pl", sourceUrlString)
        result.contains new LinkedUrl("http://www.thirdA.pl", new LinkedUrl("http://www.secondA.pl", sourceUrlString))
        result.contains new LinkedUrl("http://www.thirdA.pl", new LinkedUrl("http://www.secondB.pl", sourceUrlString))
        result.contains new LinkedUrl("http://www.thirdB.pl", new LinkedUrl("http://www.secondA.pl", sourceUrlString))
        result.contains new LinkedUrl("http://www.thirdB.pl", new LinkedUrl("http://www.secondB.pl", sourceUrlString))
    }

    def "should follow url from Page three level deep and create six LinkedUrls"() {
        given:
        def sourceUrlString = "http://www.first.pl"
        def sourceUrl = new URL(sourceUrlString)

        when:
        List<LinkedUrl> result = getCrawler(3, stubPageDownloader(), new RegexUrlFinder()).crawl(sourceUrl)

        then:
        result.size() == 10
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
    //TODO remove this
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
        List<LinkedUrl> result = getCrawler(10, stubDownloaderWithCycle, new RegexUrlFinder()).crawl(sourceUrl)

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
