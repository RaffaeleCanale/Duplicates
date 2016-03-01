package indexer.impl;

import checksum.PartialSumBuilder;
import com.wx.io.Accessor;
import com.wx.util.Format;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import util.GlobalIndexProgress;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Created on 01/03/2016
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class CheckSumIndexTest {

    private static final long TEST_FILE_SIZE = (long) (1.5 * (1 << 20));
    private static final int RANDOM_SEED = 24;

    public static final File FILE_A1 = new File("FILE_A1");
    public static final File FILE_A2 = new File("FILE_A2");
    public static final File FILE_B = new File("FILE_B");
    public static final File FILE_C = new File("FILE_C");

    @BeforeClass
    public static void createFiles() throws IOException {
        if (!FILE_A1.exists()) {
            Random rnd = new Random(RANDOM_SEED);

            try(Accessor accessor = new Accessor().setOut(FILE_A1)) {
                for (long i = 0; i < TEST_FILE_SIZE; i++) {
                    accessor.write(rnd.nextInt(240));
                }
            }
        }


        if (!FILE_A2.exists()) {
            Files.copy(FILE_A1.toPath(), FILE_A2.toPath());
        }

        if (!FILE_B.exists()) {
            Files.copy(FILE_A1.toPath(), FILE_B.toPath());
            try (RandomAccessFile rf = new RandomAccessFile(FILE_B, "rw")) {
                rf.seek(TEST_FILE_SIZE - 1);
                rf.write(241);
            }
        }

        if (!FILE_C.exists()) {
            Files.copy(FILE_A1.toPath(), FILE_C.toPath());
            try (RandomAccessFile rf = new RandomAccessFile(FILE_C, "rw")) {
//                rf.seek(TEST_FILE_SIZE - 1);
                rf.write(241);
            }
        }


    }


    @Test
    public void test() {
        CheckSumIndexer index = new CheckSumIndexer(-1, PartialSumBuilder.DEFAULT_SUM_SIZE, PartialSumBuilder.DEFAULT_STEP_SIZE);
        Collection<List<File>> result = index.similarFiles(Arrays.asList(FILE_A1, FILE_A2, FILE_B, FILE_C), 0);


        print(result);
        Assert.assertEquals(3, result.size());

        System.out.println(Format.formatSize(GlobalIndexProgress.checkSumIndexProgress));

    }

    private void print(Collection<List<File>> result) {
        for (List<File> group : result) {
            System.out.println(" - " + group);
        }
    }

}