package finder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUrlFinder implements UrlFinder {
    private Pattern url = Pattern.compile("\"http(s)?://[0-9a-zA-Z./$-_+!*'(),]+\"");

    @Override
    public List<URL> findAll(String pageContent) {
        Matcher matcher = url.matcher(pageContent);
        List<URL> result = new ArrayList<>();
        while (matcher.find()) {
            String urlInQuotations = matcher.group();
            String urlWithoutQuotations = urlInQuotations.substring(1, urlInQuotations.length() - 1);
            try {
                result.add(new URL(urlWithoutQuotations));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
}
