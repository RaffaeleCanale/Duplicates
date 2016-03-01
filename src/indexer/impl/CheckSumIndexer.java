package indexer.impl;

import checksum.FileSum;
import checksum.PartialSumBuilder;
import indexer.IndexerInterface;
import util.ErrorHandler;
import util.GlobalIndexProgress;
import util.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This indexer computes the files checksum to define if they are equal or not.
 * <p/>
 * In order to significantly reduce computations when comparing files, this indexer does not necessarily compute all the
 * checksum, but it computes the partial checksums incrementally until they differ or they have been fully computed.
 * <p/>
 * Created on 28/02/2016
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class CheckSumIndexer implements IndexerInterface {


    private final int maxDepth;
    private final int sumSize;
    private final int stepSize;

    public CheckSumIndexer(int maxDepth, int sumSize, int stepSize) {
        this.maxDepth = maxDepth;
        this.sumSize = sumSize;
        this.stepSize = stepSize;
    }

    @Override
    public Collection<List<File>> similarFiles(List<File> files, int minGroupSize) {
        return similarFiles0(wrap(files), minGroupSize);
    }

    @Override
    public String toString() {
        return "Checksum{depth=" + maxDepth + ",size=" + sumSize + ",step=" + stepSize + "}";
    }

    private Collection<List<File>> similarFiles0(Collection<Wrapper> files, int minGroupSize) {
        Collection<List<Wrapper>> groups = initGroups(files, minGroupSize);


        Collection<List<File>> result = new LinkedList<>();

        for (List<Wrapper> group : groups) {
            if (isFinalized(group)) {
                result.add(unwrap(group));
            } else {
                result.addAll(applyRecursionOnGroup(group, minGroupSize));
            }
        }


        return result;
    }

    private Collection<List<Wrapper>> initGroups(Collection<Wrapper> files, int minGroupSize) {
        Map<FileSum, List<Wrapper>> partialSumsMap = new HashMap<>();


        for (Wrapper wrapper : files) {
            try {
                int bytesProcessed = wrapper.builder.computeNextStep();
                GlobalIndexProgress.checkSumIndexProgress += bytesProcessed;


                getList(wrapper.builder.getPartialSum(), partialSumsMap).add(wrapper);
            } catch (IOException e) {
                ErrorHandler.fileIgnored(wrapper.file, e);

                try {
                    wrapper.builder.close();
                } catch (IOException e1) {/*ignored*/}
            }
        }


        Collection<List<Wrapper>> result = partialSumsMap.values();
        Utils.trimResults(result, minGroupSize);

        return result;
    }

    private Collection<List<File>> applyRecursionOnGroup(List<Wrapper> group, int minGroupSize) {
        CheckSumIndexer recIndex = new CheckSumIndexer(maxDepth - 1, sumSize, stepSize);

        return recIndex.similarFiles0(group, minGroupSize);
    }

    private List<Wrapper> getList(FileSum checkSum, Map<FileSum, List<Wrapper>> partialSumsMap) {
        List<Wrapper> list = partialSumsMap.get(checkSum);
        if (list == null) {
            list = new ArrayList<>();
            partialSumsMap.put(checkSum, list);
        }

        return list;
    }

    private boolean isFinalized(List<Wrapper> group) {
        return maxDepth == 0 || group.size() <= 1 || group.get(0).builder.endReached();
    }


    private List<File> unwrap(List<Wrapper> group) {
        return group.stream().peek(this::closeSilently).map(f -> f.file).collect(Collectors.toList());
    }

    private List<Wrapper> wrap(Collection<File> files) {

        List<Wrapper> filesWrapper = new ArrayList<>(files.size());
        for (File file : files) {
            try {
                filesWrapper.add(new Wrapper(file, new PartialSumBuilder(sumSize, stepSize, file)));
            } catch (FileNotFoundException e) {
                ErrorHandler.fileIgnored(file, e);
            }
        }


        return filesWrapper;
    }

    private void closeSilently(Wrapper wrapper) {
        try {
            wrapper.builder.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private class Wrapper {
        private final File file;
        private final PartialSumBuilder builder;

        public Wrapper(File file, PartialSumBuilder builder) {
            this.file = file;
            this.builder = builder;
        }
    }
}
