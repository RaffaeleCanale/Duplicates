package duplicates;

import file.filters.CombinedFileFilter;
import indexer.impl.ChainedIndexer;
import file.FileWalker;

import java.io.File;
import java.io.FileFilter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Container for all the parameters/input of the Duplicates process.
 *
 * Created on 29/02/2016
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class DuplicatesParameters {

    private final List<File> inputFiles;
    private final ChainedIndexer indexer;
    private final FileFilter filters;
    private final int minGroupSize;

    public DuplicatesParameters(List<File> inputFiles, ChainedIndexer indexer, Set<FileFilter> filters, int minGroupSize) {
        this.inputFiles = inputFiles;
        this.indexer = indexer;
        this.minGroupSize = minGroupSize;
        this.filters = initFilter(filters);
    }

    private FileFilter initFilter(Set<FileFilter> filters) {
        if (filters == null || filters.isEmpty()) {
            return null;
        } else {
            return new CombinedFileFilter(filters);
        }
    }

    public List<File> walkFiles() {
        FileWalker fileWalker = new FileWalker(filters);
        return inputFiles.stream().flatMap(d -> fileWalker.walkFrom(d).stream()).collect(Collectors.toList());
    }

    public Collection<List<File>> indexFiles(List<File> files) {
        return indexer.similarFiles(files, minGroupSize);
    }

    public ChainedIndexer getIndexer() {
        return indexer;
    }

    public FileFilter getFilters() {
        return filters;
    }

    public List<File> getInputFiles() {
        return inputFiles;
    }

    public static class Builder {
        private List<File> inputFiles = new LinkedList<>();
        private ChainedIndexer indexer;
        private Set<FileFilter> filters = new HashSet<>();
        private int minGroupSize;

        public void setIndexer(ChainedIndexer indexer) {
            this.indexer = indexer;
        }

        public void setMinGroupSize(int minGroupSize) {
            this.minGroupSize = minGroupSize;
        }

        public void addInputFiles(List<File> inputFiles) {
            this.inputFiles.addAll(inputFiles);
        }

        public void addInputFilesPath(List<String> path) {
            addInputFiles(path.stream().map(File::new).collect(Collectors.toList()));
        }

        public void addFilters(FileFilter filter) {
            filters.add(filter);
        }

        public DuplicatesParameters build() {
            return new DuplicatesParameters(inputFiles, indexer, filters, minGroupSize);
        }
    }
}
