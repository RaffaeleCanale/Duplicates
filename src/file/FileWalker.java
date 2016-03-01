package file;


import com.wx.console.color.Color;
import com.wx.util.Format;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static duplicates.Main.CONSOLE;

/**
 *
 * @author Canale
 */
public class FileWalker {

    private static final int PRINT_INTERVAL = 250;

    private long totalSize = 0;
    private int filesCount = 0;
    private int dirCount = 0;
    private final FileFilter fileFilter;

    public FileWalker(FileFilter fileFilter) {
        this.fileFilter = fileFilter;
    }

    public List<File> walkFrom(File file) {
        if (!file.isDirectory()) {
            return Collections.singletonList(file);
        }

        List<File> files = new LinkedList<>();

        totalSize = 0;
        filesCount = 0;
        dirCount = 0;
        
        buildFrom0(file, files);
        printStatus(file);
        CONSOLE.println();

        return files;
    }

    private void buildFrom0(File dir, List<File> files) {
        dirCount++;

        for (File file : listDir(dir)) {
            if (file.isDirectory()) {
                buildFrom0(file, files);

            } else {
                files.add(file);
                filesCount++;
                totalSize += file.length();

                if (filesCount % PRINT_INTERVAL == 0) {
                    printStatus(file);
                }
            }
        }
    }

    private File[] listDir(File dir) {
        File[] files = dir.listFiles(fileFilter);
        return files == null ? new File[0] : files;
    }

    private void printStatus(File actualFile) {
        CONSOLE.carriageReturn();
        CONSOLE.print("Walking directories... ");

        CONSOLE.setBold();
        CONSOLE.print(filesCount);
        CONSOLE.resetStyle();
        CONSOLE.print("/" + dirCount + " ");
        CONSOLE.setColor(Color.Cyan);
        CONSOLE.print(Format.formatSize(totalSize));
        CONSOLE.resetStyle();
        CONSOLE.print("\t\t" + actualFile.getName());
        CONSOLE.flush();

//        try {
//            
//            wait(10);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(IndexBuilder.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}
