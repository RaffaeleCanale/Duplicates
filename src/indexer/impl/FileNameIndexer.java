package indexer.impl;

import indexer.KeyMapIndexer;
import util.GlobalIndexProgress;

import java.io.File;

/**
 *
 * @author Canale
 */
public class FileNameIndexer extends KeyMapIndexer<String> {

    @Override
    protected String getKey(File file) {
        return file.getName();
    }

    @Override
    public String toString() {
        return "File name index";
    }

    @Override
    protected void incrementProgress() {
        GlobalIndexProgress.nameIndexProgress++;
    }
}
