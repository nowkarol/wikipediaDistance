package downloader;

import java.net.URL;

public interface PageDownloader {
    String downloadContent(URL url);
}