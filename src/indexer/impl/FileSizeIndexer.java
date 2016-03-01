package indexer.impl;

import indexer.KeyMapIndexer;
import util.GlobalIndexProgress;

import java.io.File;

/**
 *
 * @author Canale
 */
public class FileSizeIndexer extends KeyMapIndexer<Long> {

    @Override
    protected Long getKey(File file) {
        return file.length();
    }

    @Override
    public String toString() {
        return "File size index";
    }


    @Override
    protected void incrementProgress() {
        GlobalIndexProgress.sizeIndexProgress++;
    }
}
