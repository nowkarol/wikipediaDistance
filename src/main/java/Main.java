import java.net.MalformedURLException;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws MalformedURLException {
        final URL url = new URL("http://google.pl");
        Crawler crawler = new Crawler(3, new SimplePageDownloader());
        crawler.crawl(url).forEach(System.out::println);
    }
}
