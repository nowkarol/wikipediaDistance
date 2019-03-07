package finder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleWordFinder implements WordFinder {
    @Override
    public boolean wordAppears(String pageContent, String word) {
        Pattern wordPattern = Pattern.compile("\\b(\\w*" + word +"\\w*)\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = wordPattern.matcher(pageContent);
        return matcher.find();
    }
}
