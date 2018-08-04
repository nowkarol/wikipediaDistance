import java.net.MalformedURLException;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws MalformedURLException {
        Page page = new Page(new URL("http://google.pl"));
        Crawler crawler = new Crawler(1);
        crawler.crawl(page).forEach(System.out::println);
    }
}
