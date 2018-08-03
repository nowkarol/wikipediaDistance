import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class UrlFinder {
    private final String stringToSearch;
    private Pattern url = Pattern.compile("\"http://\\S+\"");

    UrlFinder(String stringToSearch){
        this.stringToSearch = stringToSearch;
    }

    List<URL> findAll(){
        Matcher matcher = url.matcher(stringToSearch);
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
