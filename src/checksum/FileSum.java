package checksum;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a file check sum. It basically is a byte[] wrapper, but it defines an equals/hashCode method.
 *
 * Created on 28/02/2016
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class FileSum {

    private final byte[] sum;

    public FileSum(byte[] sum) {
        this.sum = Objects.requireNonNull(sum);
    }

    public byte[] getSum() {
        return sum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileSum fileSum = (FileSum) o;

        return Arrays.equals(sum, fileSum.sum);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(sum);
    }
}
