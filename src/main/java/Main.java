import java.net.MalformedURLException;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws MalformedURLException {
        String content = new Page(new URL("http://karolnowak.net")).getContent();
        new UrlFinder(content).findAll().forEach(System.out::println);
    }
}
