package finder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NaiveUrlFinder implements UrlFinder {
    @Override
    public List<URL> findAll(String pageContent) {
        String[] parameters = pageContent.split("\"");
        if (parameters.length == 1) return Collections.emptyList();
        return Stream.of(parameters)
                .filter(param -> param.startsWith("http://"))
                .map(spec -> {
                   try {
                        return new URL(spec);
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
        }).collect(Collectors.toList());
    }

}
