package downloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class SimplePageDownloader implements PageDownloader{
    public static long counter = 0;
    @Override
    public String downloadContent(URL url) {
        StringBuilder site = new StringBuilder();
        try (InputStream inputStream = url.openStream(); BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                site.append(line);
            }
        } catch (IOException e) {
//            throw new RuntimeException(e);
        }
        String result = site.toString();
        counter+=result.getBytes().length;
        return site.toString();
    }

}
