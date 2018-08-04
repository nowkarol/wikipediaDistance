import spock.lang.Specification

class CrawlerTest extends Specification {
    def "should create LinkedUrl when provided Page contains url"() {
        given:
        def sourceUrl = new URL("http://www.wp.pl")
        Page pageWithOneUrl = new Page(sourceUrl, "\"http://karolnowak.net\"")

        when:
        List<LinkedUrl> result = new Crawler().crawl(pageWithOneUrl)

        then:
        result.size() == 1
        result[0] == new LinkedUrl("http://karolnowak.net", "http://www.wp.pl")
    }

    def "should create two LinkedUrls when provided Page contains two urls"() {
        given:
        def sourceUrl = new URL("http://www.wp.pl")
        Page pageWithOneUrl = new Page(sourceUrl, "\"http://karolnowak.net\" garbage \"http://google.pl\"")

        when:
        List<LinkedUrl> result = new Crawler().crawl(pageWithOneUrl)

        then:
        result.size() == 2
        result.contains new LinkedUrl("http://karolnowak.net", "http://www.wp.pl")
        result.contains new LinkedUrl("http://google.pl", "http://www.wp.pl")
    }

    def "should follow url from Page and create two LinkedUrls"() {
        given:
        def sourceUrl = new URL("http://www.first.pl")
        PageDownloader stubDownloader = new PageDownloader() {
            @Override
            String downloadContent(URL url) {
                if (url.equals(new URL("http://www.first.pl"))) {
                    return '"http://www.second.pl"'
                }

                if (url.equals(new URL("http://www.second.pl"))) {
                    return '"http://www.third.pl"'
                }
                return ""

            }
        }
        Page pageWithOneUrlWhichLeadsToAnother = new Page(sourceUrl, stubDownloader)

        when:
        List<LinkedUrl> result = new Crawler().crawl(pageWithOneUrlWhichLeadsToAnother)

        then:
        result.size() == 2
        result[0] == new LinkedUrl("http://www.second.pl", "http://www.first.pl")
        result[1] == new LinkedUrl("http://www.third.pl", "http://www.second.pl")
    }

    def "should follow url from Page two level deep and create six LinkedUrls"() {
        given:
        def sourceUrl = new URL("http://www.first.pl")
        PageDownloader stubDownloader = new PageDownloader() {
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

        when:
        Page pageWithTwoLevelLinks = new Page(sourceUrl, stubDownloader)

        List<LinkedUrl> result = new Crawler().crawl(pageWithTwoLevelLinks)

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
        PageDownloader stubDownloader = new PageDownloader() {
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

        when:
        Page pageWithTwoLevelLinks = new Page(sourceUrl, stubDownloader)

        List<LinkedUrl> result = new Crawler(1).crawl(pageWithTwoLevelLinks)

        then:
        result.size() == 2
        result.contains new LinkedUrl(new URL("http://www.secondA.pl"), new LinkedUrl("http://www.first.pl"))
        result.contains new LinkedUrl(new URL("http://www.secondB.pl"), new LinkedUrl("http://www.first.pl"))
    }


    def "should not follow circular link"() {
        given:
        def sourceUrl = new URL("http://www.first.pl")
        PageDownloader stubDownloader = new PageDownloader() {
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
        Page pageWithCircularLink = new Page(sourceUrl, stubDownloader)

        when:
        List<LinkedUrl> result = new Crawler().crawl(pageWithCircularLink)

        then:
        result.size() == 2
        result.contains new LinkedUrl("http://www.second.pl", "http://www.first.pl")
        result.contains new LinkedUrl("http://www.first.pl", "http://www.second.pl")
    }

}
