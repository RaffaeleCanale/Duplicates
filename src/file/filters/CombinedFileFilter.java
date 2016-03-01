package file.filters;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;

/**
 * Wrapper that emulates several filters into a single one.
 *
 * This filter automatically accepts directories.
 *
 * Created on 29/02/2016
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class CombinedFileFilter implements FileFilter {

    private final Set<FileFilter> filters;

    public CombinedFileFilter(Set<FileFilter> filters) {
        this.filters = filters;
    }

    @Override
    public boolean accept(File pathname) {
        return pathname.isDirectory() || filters.stream().allMatch(f -> f.accept(pathname));
    }

    @Override
    public String toString() {
        return filters.toString();
    }
}
