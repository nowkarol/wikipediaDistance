package downloader;

import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

public class CachedPageDownloader implements PageDownloader {
    private final PageDownloader pageDownloader;
    private final ConcurrentHashMap<URL, String> pages = new ConcurrentHashMap<>();

    public CachedPageDownloader(PageDownloader pageDownloader) {
        this.pageDownloader = pageDownloader;
    }
    @Override
    public String downloadContent(URL url) {
        if (pages.containsKey(url)) {
            return pages.get(url);
        }
        String content = pageDownloader.downloadContent(url);
        pages.put(url, content);
        return content;
    }
}
