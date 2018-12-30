package crawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LinkedUrl {
    private final URL url;
    private final LinkedUrl parentUrl;

    LinkedUrl(URL url, LinkedUrl parentUrl) {
        this.url = url;
        this.parentUrl = parentUrl;
    }
    LinkedUrl(URL url) {
        this(url, null);
    }

    LinkedUrl(String url, String parentUrl) throws MalformedURLException {
        this(new URL(url), new LinkedUrl(parentUrl));
    }

    LinkedUrl(String url, LinkedUrl linkedUrl) throws MalformedURLException {
        this(new URL(url), linkedUrl);
    }

    LinkedUrl(String url) throws MalformedURLException {
        this(new URL(url), null);
    }

    public List<LinkedUrl> createFromParent(List<URL> urlsOnPage) {
        return urlsOnPage.stream()
                .map(url -> new LinkedUrl(url, this))
                .collect(Collectors.toList());
    }

    public URL getUrl() {
        return url;
    }

    public boolean isRoot() {
        return parentUrl == null;
    }

    public boolean isChildOf(URL parentUrl) {
        if (this.parentUrl == null){
            return false;
        }
        return Objects.equals(this.parentUrl.getUrl(), parentUrl);
    }

    public LinkedUrl getParentUrl() {
        return parentUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkedUrl linkedUrl = (LinkedUrl) o;
        return Objects.equals(url, linkedUrl.url) && Objects.equals(parentUrl, linkedUrl.parentUrl);
    }
    @Override
    public int hashCode() {
        return Objects.hash(url, parentUrl);
    }

    //TODO handle cycle

    @Override
    public String toString() {
        return "LinkedUrl{" + "url=" + url + ", parentUrl=" + parentUrl + '}';
    }
}
