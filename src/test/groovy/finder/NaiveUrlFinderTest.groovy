package finder


class NaiveUrlFinderTest extends UrlFinderTest {
    @Override
    UrlFinder getUrlFinder() {
        return new NaiveUrlFinder()
    }
}
