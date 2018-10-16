package crawler

import spock.lang.Specification

class LinkedUrlTest extends Specification {

    def "shouldCreate LinkedUrl for all passed Urls"() {
        given:
        LinkedUrl parentUrl = new LinkedUrl("http://karolnowak.net")
        def urls = [new URL("http://google.de"),
                    new URL("http://google.com"),
                    new URL("http://google.pl")]

        when:
        def result = parentUrl.createFromParent(urls)

        then:
        result.size() ==  3
        result[0] == new LinkedUrl("http://google.de", "http://karolnowak.net")
        result[1] == new LinkedUrl("http://google.com", "http://karolnowak.net")
        result[2] == new LinkedUrl("http://google.pl", "http://karolnowak.net")
    }
}