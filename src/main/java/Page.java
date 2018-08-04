import java.net.URL;

class Page {
    private final URL url;
    private final String content;
    private PageDownloader downloader;

    Page(URL url, String content){
        this.url = url;
        this.content = content;
        this.downloader = new PageDownloader(){
            @Override
            String downloadContent(URL url) {
                return "";
            }
        };
    }

    Page(URL url){
        this(url, new PageDownloader());
    }

    Page(URL url, PageDownloader downloader){
        this(url, downloader.downloadContent(url));
        this.downloader = downloader;
    }




    @Override
    public String toString() {
        return "Page{" + "url=" + url + ", content='" + content + '\'' + '}';
    }

    URL getUrl() {
        return url;
    }

    String getContent() {
        return content;
    }

    Page newPageWithSameDownloader(URL url) {
        return new Page(url, downloader);
    }
}
