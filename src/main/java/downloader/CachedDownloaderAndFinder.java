package downloader;

import finder.UrlFinder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class CachedDownloaderAndFinder implements PageDownloader, UrlFinder {
    private static final String MAGIC_STRING = "vulnerability";
    private final PageDownloader downloader;
    private final UrlFinder finder;
    private final ConcurrentHashMap<URL, List<URL>> cache = new ConcurrentHashMap<>();


    public CachedDownloaderAndFinder(PageDownloader downloader, UrlFinder finder) {
        this.downloader = downloader;
        this.finder = finder;
    }
    @Override
    public String downloadContent(URL urlToDownloadAndParse) {
        if (cache.containsKey(urlToDownloadAndParse)) {
            return MAGIC_STRING + urlToDownloadAndParse.toString();
        }

        String page = downloader.downloadContent(urlToDownloadAndParse);
        List<URL> resultUrls = finder.findAll(page);
        cache.put(urlToDownloadAndParse, resultUrls);

        return MAGIC_STRING + urlToDownloadAndParse.toString();
    }

    @Override
    public List<URL> findAll(String pageContent) {
        if (pageContent.startsWith(MAGIC_STRING)){
            String urlString = pageContent.substring(MAGIC_STRING.length());
            try {
                return cache.get(new URL(urlString));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        throw new IllegalStateException("You have to use downloading method from this class first");
    }

    public static void main(String[] args) throws MalformedURLException {
        System.out.println(new URL("http://www.google.pl"));
    }
}
