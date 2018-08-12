package finder;

import java.net.URL;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class CachedUrlFinder implements UrlFinder {
    private final UrlFinder urlFinder;
    private final ConcurrentHashMap<String, List<URL>> cache = new ConcurrentHashMap<>();

    public CachedUrlFinder(UrlFinder urlFinder) {
        this.urlFinder = urlFinder;
    }


    @Override
    public List<URL> findAll(String pageContent) {
        if (cache.containsKey(pageContent)) {
            return cache.get(pageContent);
        }

        List<URL> result = urlFinder.findAll(pageContent);
        cache.put(pageContent, result);
        return result;
    }
}
