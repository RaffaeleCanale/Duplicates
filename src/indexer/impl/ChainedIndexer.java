package indexer.impl;

import indexer.IndexerInterface;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This indexer computes and mergers the results from list of indexers.
 * <p/>
 * It applies the first indexer on the given files, then, for each group, it applies the second indexer and so on.
 * <p/>
 * Created on 28/02/2016
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class ChainedIndexer implements IndexerInterface {

    private final List<IndexerInterface> indexers;

    public ChainedIndexer(List<IndexerInterface> indexers) {
        if (indexers.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.indexers = indexers;
    }


    @Override
    public Collection<List<File>> similarFiles(List<File> files, int minGroupSize) {
        return applyIndexer(files, 0, minGroupSize).collect(Collectors.toList());
    }

    private Stream<List<File>> applyIndexer(List<File> files, int index, int minGroupSize) {
        if (index >= indexers.size() || files.size() <= 1) {
            return Stream.of(files);
        }

//        List<List<File>> result = new LinkedList<>();
//        Collection<List<File>> groups = indexers.get(index).similarFiles(files, minGroupSize);
//        for (List<File> group : groups) {
//            result.addAll(applyIndexer(group, index + 1, minGroupSize));
//        }

        return indexers.get(index).similarFiles(files, minGroupSize).stream()
                .flatMap(g -> applyIndexer(g, index + 1, minGroupSize));
    }

    @Override
    public String toString() {
        return "[" + indexers.stream().map(Object::toString).collect(Collectors.joining(", ")) + "]";
    }
}
