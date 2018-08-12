package downloader;

import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

public class CachedPageDownloader implements PageDownloader {
    private final PageDownloader pageDownloader;
    private final ConcurrentHashMap<URL, String> cache = new ConcurrentHashMap<>();

    public CachedPageDownloader(PageDownloader pageDownloader) {
        this.pageDownloader = pageDownloader;
    }
    @Override
    public String downloadContent(URL url) {
        if (cache.containsKey(url)) {
            return cache.get(url);
        }
        String content = pageDownloader.downloadContent(url);
        cache.put(url, content);
        return content;
    }
}
