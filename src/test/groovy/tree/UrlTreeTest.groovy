package tree

import crawler.LinkedUrl
import org.junit.Ignore
import spock.lang.Specification

import static java.util.Collections.emptySet

class UrlTreeTest extends Specification {

    def "should return TreeUrl without children when passed LinkedUrl doesn't have parent"() {
        given:
        def linkedUrl = new LinkedUrl("http://karolnowak.net")

        when:
        def result = new UrlTree([linkedUrl])

        then:
        result.equals new UrlTree("http://karolnowak.net", emptySet())
    }

    def "should return TreeUrl with one children when passed LinkedUrl has one parent"() {
        given:
        def parentUrl = new LinkedUrl("http://karolnowak.net")
        def childUrl = new LinkedUrl("http://google.pl", parentUrl)

        when:
        def result = new UrlTree([parentUrl, childUrl])

        then:
        result.equals new UrlTree("http://karolnowak.net", [new UrlTree("http://google.pl")] as Set)
    }

    def "order in List should not matter"() {
        given:
        def parentUrl = new LinkedUrl("http://karolnowak.net")
        def childUrl = new LinkedUrl("http://google.pl", parentUrl)

        when:
        def result = new UrlTree([childUrl, parentUrl])

        then:
        result.equals new UrlTree("http://karolnowak.net", [new UrlTree("http://google.pl")] as Set)
    }

    def "should handle 3 level deep LinkedUrl"(){
        given:
        def root = new LinkedUrl("http://karolnowak.net")
        def firstLevel = new LinkedUrl("http://google.pl", root)
        def secondLevel = new LinkedUrl("http://test.pl", firstLevel)
        def thirdLevel = new LinkedUrl("http://last.pl", secondLevel)

        when:
        def result = new UrlTree([root, firstLevel, secondLevel, thirdLevel])

        then:
        result.equals new UrlTree("http://karolnowak.net",
                            new UrlTree("http://google.pl",
                                    new UrlTree("http://test.pl",
                                            new UrlTree("http://last.pl"))))

    }

    def "should handle 3 level deep LinkedUrl with two LinkedUrls on highest level"(){
        given:
        def root = new LinkedUrl("http://karolnowak.net")

        def firstLevelA = new LinkedUrl("http://google.pl", root)
        def secondLevelA = new LinkedUrl("http://test.pl", firstLevelA)
        def thirdLevelA = new LinkedUrl("http://last.pl", secondLevelA)

        def firstLevelB = new LinkedUrl("http://google.com", root)
        def secondLevelB = new LinkedUrl("http://test.com", firstLevelB)
        def thirdLevelB = new LinkedUrl("http://last.com", secondLevelB)

        when:
        def result = new UrlTree([root, secondLevelA, firstLevelA, thirdLevelB, thirdLevelA, secondLevelB, firstLevelB])

        then:
        result.equals new UrlTree("http://karolnowak.net",
                new UrlTree("http://google.pl",
                        new UrlTree("http://test.pl",
                                new UrlTree("http://last.pl"))),
                new UrlTree("http://google.com",
                        new UrlTree("http://test.com",
                                new UrlTree("http://last.com"))))
    }


    def "should throw exception when LinkedUrls has no root"() {
        given:
        def notPassedRootUrl = new LinkedUrl("http://karolnowak.net")
        def childUrl = new LinkedUrl("http://google.pl", notPassedRootUrl)

        when:
        new UrlTree([childUrl])

        then:
        thrown(IllegalStateException)

    }

    def "should throw exception when LinkedUrl list has more than one root"() {
        given:
        def root = new LinkedUrl("http://karolnowak.net")
        def firstLevel = new LinkedUrl("http://google.pl", root)
        def secondLevel = new LinkedUrl("http://test.pl", firstLevel)
        def anotherRoot = new LinkedUrl("http://last.pl")

        when:
        new UrlTree([root, firstLevel, secondLevel, anotherRoot])

        then:
        thrown(IllegalStateException)

    }

    @Ignore
    //TODO
    def "should throw exception when any of LinkedUrls is not connected to root"() {
        given:
        def root = new LinkedUrl("http://karolnowak.net")
        def firstLevel = new LinkedUrl("http://google.pl", root)
        def notPassedOtherRoot = new LinkedUrl("http://test.pl")
        def otherFirstLevel = new LinkedUrl("http://last.pl", notPassedOtherRoot)

        when:
        new UrlTree([root, firstLevel, otherFirstLevel])

        then:
        thrown(IllegalStateException)
    }

    def "should throw exception when list is null"() {
        when:
        new UrlTree((List)null)

        then:
        thrown(NullPointerException)

    }

    def "should throw exception when Url is null"() {
        when:
        new UrlTree((URL)null, [] as Set)

        then:
        thrown(NullPointerException)
    }
}
