package tree;

import crawler.LinkedUrl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;

public class UrlTree {
    private static int count;
    private static int total;
    private final URL url;
    private final Set<UrlTree> children;

    public UrlTree(List<LinkedUrl> linkedUrls) {
        //handle cycles, tidy
        validateUrls(linkedUrls);
        linkedUrls = removeUrlWithCycles(linkedUrls);
        this.url = getRoot(linkedUrls);
        total = linkedUrls.size();
        this.children = getChildren(linkedUrls);
    }

    public UrlTree(String url, Set<UrlTree> children) throws MalformedURLException {
        this(new URL(url), children);
    }

    public UrlTree(String url, UrlTree... children) throws MalformedURLException {
        this(new URL(url), new HashSet<>(Arrays.asList(children)));
    }

    public UrlTree(String url) throws MalformedURLException {
        this(url, Collections.emptySet());
    }

    public UrlTree(URL url, Set<UrlTree> children){
        checkNotNull(url);
        checkNotNull(children);
        this.url = url;
        this.children = new HashSet<>(children);
    }

    private void validateUrls(List<LinkedUrl> linkedUrls) {
        long rootsQuantity = linkedUrls.stream()
                .filter(LinkedUrl::isRoot)
                .count();
        if (rootsQuantity != 1){
            throw new IllegalStateException("Passed LinkedUrl list should contain max one root it has: " + rootsQuantity);
        }
    }

    private List<LinkedUrl> removeUrlWithCycles(List<LinkedUrl> linkedUrls) {
        return linkedUrls.stream().filter(this::isCycleFree).collect(Collectors.toList());
    }

    private boolean isCycleFree(LinkedUrl linkedUrl) {
        URL url = linkedUrl.getUrl();
        LinkedUrl parentUrl = linkedUrl.getParentUrl();
        while(parentUrl != null) {
            if (parentUrl.getUrl().equals(url)){
                return false;
            }
            parentUrl = parentUrl.getParentUrl();
        }
        return true;
    }

    private Set<UrlTree> getChildren(List<LinkedUrl> linkedUrls) {
        return getChildrenRecursively(getRoot(linkedUrls), linkedUrls);
    }

    private Set<UrlTree> getChildrenRecursively(URL root, List<LinkedUrl> linkedUrls) {
        Set<URL> childrenUrls = getChildrenUrls(root, linkedUrls);
        Set<UrlTree> result = getUrlTrees(childrenUrls, linkedUrls);
        if (childrenUrls.size() != 0) System.out.printf("Fetched tree for %s which consist of %d next level objects \n", root, childrenUrls.size());
        count++;
        System.out.printf("Progress: %d of %d \n", count, total);
        return result;

    }

    private Set<URL> getChildrenUrls(URL root, List<LinkedUrl> linkedUrls) {
        return linkedUrls.parallelStream()
                .filter(linkedUrl -> linkedUrl.isChildOf(root))
                .map(LinkedUrl::getUrl)
                .collect(Collectors.toSet());
    }

    private Set<UrlTree> getUrlTrees(Set<URL> childrenUrls, List<LinkedUrl> linkedUrls) {
        return childrenUrls.parallelStream().unordered()
                .map(childrenUrl -> {
                    Set<UrlTree> thisChildren = getChildrenRecursively(childrenUrl, linkedUrls);
                    return new UrlTree(childrenUrl, thisChildren);
                })
                .collect(toSet());
    }


    private URL getRoot(List<LinkedUrl> linkedUrls) {
        return linkedUrls.stream().filter(LinkedUrl::isRoot).findAny().get().getUrl();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UrlTree urlTree = (UrlTree) o;
        return Objects.equals(url, urlTree.url) &&
                Objects.equals(children, urlTree.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, children);
    }

    @Override
    public String toString() {
        //TODO change it to more visual
        return "UrlTree{" +
                "url='" + url + '\'' +
                ", children=" + children +
                '}';
    }
}
