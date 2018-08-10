package finder;

import java.net.URL;
import java.util.List;

public interface UrlFinder {
    List<URL> findAll(String pageContent);
}
