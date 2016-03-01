package file.filters;

import com.wx.util.Format;
import util.Utils;

import java.io.File;
import java.io.FileFilter;

/**
 * Simple filter for the file size.
 *
 * @author Canale
 */
public class SizeFilter implements FileFilter {

    private final long minSize;
    private final long maxSize;

    public SizeFilter(long minSize, long maxSize) {
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    @Override
    public boolean accept(File file) {
        return Utils.matches(file.length(), minSize, maxSize);
    }


    @Override
    public String toString() {
        if (minSize > 0 && maxSize > 0) {
            return "Files of size [" + Format.formatSize(minSize) + ", " + Format.formatSize(maxSize);
        } else if (minSize > 0) {
            return "Files bigger than " + Format.formatSize(minSize);
        } else if (maxSize > 0) {
            return "Files smaller than " + Format.formatSize(maxSize);
        } else {
            return "";
        }
    }
}
