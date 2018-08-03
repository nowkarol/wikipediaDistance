import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

class Page {
    private final String content;

    Page(String content) {
        this.content = content;
    }

    Page(URL url) {
        content = downloadContent(url);
    }

    private String downloadContent(URL url) {
        StringBuilder site = new StringBuilder();
        try (InputStream inputStream = url.openStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                site.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return site.toString();
    }


    String getContent() {
        return content;
    }
}
