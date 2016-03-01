package checksum;

import indexer.impl.CheckSumIndexTest;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created on 01/03/2016
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class PartialSumBuilderTest {

    @BeforeClass
    public static void createFiles() throws IOException {
        CheckSumIndexTest.createFiles();
    }

    @Test
    public void fullSumTest() throws IOException {
        PartialSumBuilder a1Sum = createBuilder(CheckSumIndexTest.FILE_A1);
        PartialSumBuilder a2Sum = createBuilder(CheckSumIndexTest.FILE_A2);
        PartialSumBuilder bSum = createBuilder(CheckSumIndexTest.FILE_B);
        PartialSumBuilder cSum = createBuilder(CheckSumIndexTest.FILE_C);

        computeFully(a1Sum);
        computeFully(a2Sum);
        computeFully(bSum);
        computeFully(cSum);

        Assert.assertEquals(a1Sum.getPartialSum(), a2Sum.getPartialSum());
        Assert.assertFalse(a1Sum.getPartialSum().equals(bSum.getPartialSum()));
        Assert.assertFalse(a1Sum.getPartialSum().equals(cSum.getPartialSum()));
        Assert.assertFalse(bSum.getPartialSum().equals(cSum.getPartialSum()));
    }

    @Test
    public void partialSumTest() throws IOException {
        PartialSumBuilder a1Sum = createBuilder(CheckSumIndexTest.FILE_A1);
        PartialSumBuilder a2Sum = createBuilder(CheckSumIndexTest.FILE_A2);
        PartialSumBuilder bSum = createBuilder(CheckSumIndexTest.FILE_B);
        PartialSumBuilder cSum = createBuilder(CheckSumIndexTest.FILE_C);

        a1Sum.computeNextStep();
        a2Sum.computeNextStep();
        bSum.computeNextStep();
        cSum.computeNextStep();

        Assert.assertEquals(a1Sum.getPartialSum(), a2Sum.getPartialSum());
        Assert.assertTrue(a1Sum.getPartialSum().equals(bSum.getPartialSum()));
        Assert.assertFalse(a1Sum.getPartialSum().equals(cSum.getPartialSum()));
        Assert.assertFalse(bSum.getPartialSum().equals(cSum.getPartialSum()));
    }

    private void computeFully(PartialSumBuilder builder) throws IOException {
        while (!builder.endReached()) {
            builder.computeNextStep();
        }
    }

    private PartialSumBuilder createBuilder(File f) throws FileNotFoundException {
        return new PartialSumBuilder(PartialSumBuilder.DEFAULT_SUM_SIZE, PartialSumBuilder.DEFAULT_STEP_SIZE, f);
    }

}