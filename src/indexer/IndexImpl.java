package indexer;

import indexer.impl.CheckSumIndexer;
import indexer.impl.FileNameIndexer;
import indexer.impl.FileSizeIndexer;

/**
 *
 * @author Canale
 */
public enum IndexImpl {
    NAME,
    SIZE,
    SUM;

    public IndexerInterface init(int csDepth, int csSize, int csStep) {
        switch (this) {
            case NAME:
                return new FileNameIndexer();
            case SIZE:
                return new FileSizeIndexer();
            case SUM:
                return new CheckSumIndexer(csDepth, csSize, csStep);
            default:
                throw new AssertionError();
        }
    }


}
