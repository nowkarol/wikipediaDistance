package finder;

import java.util.concurrent.ConcurrentHashMap;

public class CachedWordFinder implements WordFinder{
    private final SimpleWordFinder wordFinder;
    private final ConcurrentHashMap<String, Boolean> cache = new ConcurrentHashMap<>();

    public CachedWordFinder(SimpleWordFinder wordFinder) {
        this.wordFinder = wordFinder;
    }

    @Override
    public boolean wordAppears(String pageContent, String wordToFind) {
        if (cache.containsKey(pageContent)) {
            return cache.get(pageContent);
        }

        Boolean result = wordFinder.wordAppears(pageContent, wordToFind);
        cache.put(pageContent, result);
        return result;
    }
}
