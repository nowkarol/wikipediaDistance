import spock.lang.Specification

class CrawlerTest extends Specification {
    def "should create two LinkedUrl when provided Page contains two urls"() {
        given:
        def sourceUrl = new URL("http://www.first.pl")

        when:
        List<LinkedUrl> result = new Crawler(1, stubPageDownloader(), new RegexUrlFinder()).crawl(sourceUrl)

        then:
        result.size() == 2
        result.contains new LinkedUrl("http://www.secondA.pl", "http://www.first.pl")
        result.contains new LinkedUrl("http://www.secondB.pl", "http://www.first.pl")
    }

    def "should follow url from Page two level deep and create six LinkedUrls"() {
        given:
        def sourceUrl = new URL("http://www.first.pl")


        when:
        List<LinkedUrl> result = new Crawler(2, stubPageDownloader(), new RegexUrlFinder()).crawl(sourceUrl)

        then:
        result.size() == 6
        result.contains new LinkedUrl(new URL("http://www.secondA.pl"), new LinkedUrl("http://www.first.pl"))
        result.contains new LinkedUrl(new URL("http://www.secondB.pl"), new LinkedUrl("http://www.first.pl"))
        result.contains new LinkedUrl(new URL("http://www.thirdA.pl"), new LinkedUrl("http://www.secondA.pl"))
        result.contains new LinkedUrl(new URL("http://www.thirdA.pl"), new LinkedUrl("http://www.secondB.pl"))
        result.contains new LinkedUrl(new URL("http://www.thirdB.pl"), new LinkedUrl("http://www.secondA.pl"))
        result.contains new LinkedUrl(new URL("http://www.thirdB.pl"), new LinkedUrl("http://www.secondB.pl"))
    }

    def "should follow url from Page only one level deep and create two LinkedUrls if deep level is set to one"() {
        given:
        def sourceUrl = new URL("http://www.first.pl")

        when:

        List<LinkedUrl> result = new Crawler(1, stubPageDownloader(), new RegexUrlFinder()).crawl(sourceUrl)

        then:
        result.size() == 2
        result.contains new LinkedUrl(new URL("http://www.secondA.pl"), new LinkedUrl("http://www.first.pl"))
        result.contains new LinkedUrl(new URL("http://www.secondB.pl"), new LinkedUrl("http://www.first.pl"))
    }


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
        List<LinkedUrl> result = new Crawler(10, stubDownloaderWithCycle, new RegexUrlFinder()).crawl(sourceUrl)

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
                    return "no links"
                }

                return ""
            }
        }

    }
}
