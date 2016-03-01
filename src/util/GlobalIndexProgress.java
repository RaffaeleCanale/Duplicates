package util;

import com.wx.console.color.Color;
import com.wx.util.Format;

import java.io.File;
import java.util.List;

import static duplicates.Main.CONSOLE;

/**
 * Created on 29/02/2016
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class GlobalIndexProgress {

    public static int filesCount;
    public static long totalFilesSize;

    public static int nameIndexProgress;
    public static int sizeIndexProgress;
    public static long checkSumIndexProgress;

    public static void initFrom(List<File> files) {
        nameIndexProgress = 0;
        sizeIndexProgress = 0;
        checkSumIndexProgress = 0;

        filesCount = files.size();
        totalFilesSize = files.stream().mapToLong(File::length).sum();
    }

    public static void print() {
        CONSOLE.carriageReturn();
        CONSOLE.print("Indexing... ");

        if (nameIndexProgress > 0) {
            printIndexProgress("Name", Format.formatPercentage((double) nameIndexProgress / (double) filesCount));
        }
        if (sizeIndexProgress > 0) {
            printIndexProgress("Size", Format.formatPercentage((double) sizeIndexProgress / (double) filesCount));
        }
        if (checkSumIndexProgress > 0) {
            printIndexProgress("Checksum", Format.formatSize((checkSumIndexProgress)));
        }

        CONSOLE.flush();
    }

    private static void printIndexProgress(String indexName, String progress) {
        CONSOLE.print("[" + indexName + ": ");
        CONSOLE.setColor(Color.Cyan);
        CONSOLE.print(progress);
        CONSOLE.resetColor();
        CONSOLE.print("] ");
    }

}
