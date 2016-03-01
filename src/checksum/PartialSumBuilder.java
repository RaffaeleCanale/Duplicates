package checksum;

import com.wx.io.Accessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This builder computes a file checksum in an incremental way. It allows to view the partial computed checksum at each
 * step of the process.
 * <p/>
 * Created on 28/02/2016
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class PartialSumBuilder implements AutoCloseable {


    public static final int DEFAULT_SUM_SIZE = 1024;
    public static final int DEFAULT_STEP_SIZE = 1024;

    private final Accessor accessor;
    private final int stepSize;

    private FileSum partialSum;
    private boolean endReached;

    /**
     * Init a checksum builder for the given file.
     *
     * @param file File to compute
     *
     * @throws FileNotFoundException
     */
    public PartialSumBuilder(File file) throws FileNotFoundException {
        this(DEFAULT_SUM_SIZE, DEFAULT_STEP_SIZE, file);
    }

    /**
     * Init a checksum builder for the given file.
     *
     * @param sumSize  Size (in bytes) of the checksum
     * @param stepSize Number of iterations to compute at each step. For each step, the number of bytes read will
     *                 correspond to {@code sumSize*stepSize}
     * @param file     File to compute
     *
     * @throws FileNotFoundException
     */
    public PartialSumBuilder(int sumSize, int stepSize, File file) throws FileNotFoundException {
        if (!file.isFile()) {
            throw new IllegalArgumentException("Is not a file " + file);
        }

        this.stepSize = stepSize;
        this.accessor = new Accessor().setIn(file);

        this.partialSum = new FileSum(new byte[sumSize]);
    }

    /**
     * Compute the next step of the checksum.
     * <p/>
     * The number of bytes read for this step will either be {@code sumSize*stepSize} or less if it was the last step.
     *
     * @return Number of bytes actually read.
     *
     * @throws IOException
     */
    public int computeNextStep() throws IOException {
        if (endReached()) {
            throw new IllegalStateException("End reached already");
        }

        int step = 0;
        byte[] data = new byte[partialSum.getSum().length];

        int read = -1;
        while (step < stepSize && (read = accessor.read(data)) > 0) {
            for (int i = 0; i < data.length; i++) {
                partialSum.getSum()[i] ^= data[i];
            }
            step++;
        }

        if (read < partialSum.getSum().length) {
            endReached = true;
            close();
        }

        return (step - 1) * stepSize + read;
    }

    @Override
    public void close() throws IOException {
        accessor.close();
    }

    /**
     *
     * @return Computed partial sum
     */
    public FileSum getPartialSum() {
        return partialSum;
    }

    /**
     *
     * @return {@code true} if the whole file has been computed
     */
    public boolean endReached() {
        return endReached;
    }
}
