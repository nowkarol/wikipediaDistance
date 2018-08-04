import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class LinkedUrl {
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

    LinkedUrl(String url) throws MalformedURLException {
        this(new URL(url), null);
    }


    public static List<LinkedUrl> createFromParent(URL parent, List<URL> urls) {
        LinkedUrl rootParentUrl = new LinkedUrl(parent);
        return urls.stream()
                .map(url -> new LinkedUrl(url, rootParentUrl))
                .collect(Collectors.toList());
    }

    public URL getUrl() {
        return url;
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
