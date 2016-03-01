package indexer;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * An indexer indexes the given set of files into sub-groups where each sub-group contains identical files (according to
 * this indexer definition of equality).
 *
 * @author Canale
 */
public interface IndexerInterface {

    /**
     * Index the given files into sub-groups of identical files (according to this indexer definition of equality).
     *
     * @param files        Files to index
     * @param minGroupSize Minimum size of the resulting group. Any smaller group of files will not appear in the result
     *                     collection.
     *
     * @return A collection of all the similar groups
     */
    Collection<List<File>> similarFiles(List<File> files, int minGroupSize);

}
