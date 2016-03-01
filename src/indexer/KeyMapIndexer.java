package indexer;

import util.Utils;

import java.io.File;
import java.util.*;

/**
 * Simple indexer that group files according to a key computed for each file.
 * <p/>
 * The implementing class must define the function that computes a key from a file.
 *
 * @author Canale
 */
public abstract class KeyMapIndexer<T> implements IndexerInterface {


    @Override
    public Collection<List<File>> similarFiles(List<File> files, int minGroupSize) {
        Map<T, List<File>> index = new HashMap<>();

        for (File file : files) {
            getListFor(index, getKey(file)).add(file);
            incrementProgress();
        }

        Collection<List<File>> result = index.values();
        Utils.trimResults(result, minGroupSize);

        return result;
    }

    /**
     * This method is called after each iteration
     */
    protected void incrementProgress() {
    }

    /**
     * Compute a key from the given file. Two files considered similar should produce an identical key.
     *
     * @param file Target file
     *
     * @return Key corresponding to this file
     */
    protected abstract T getKey(File file);

    private List<File> getListFor(Map<T, List<File>> index, T key) {
        List<File> list = index.get(key);
        if (list == null) {
            list = new LinkedList<>();
            index.put(key, list);
        }

        return list;
    }

}
