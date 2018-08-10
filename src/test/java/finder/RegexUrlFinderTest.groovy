package finder


class RegexUrlFinderTest extends UrlFinderTest {
    @Override
    UrlFinder getUrlFinder() {
        return new RegexUrlFinder()
    }
}
