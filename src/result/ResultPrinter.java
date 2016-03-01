package result;

import com.wx.console.Console;
import com.wx.console.color.Color;
import com.wx.util.Format;
import duplicates.DuplicatesParameters;
import util.GlobalIndexProgress;

import java.io.File;
import java.util.*;

import static duplicates.Main.CONSOLE;

/**
 * @author Canale
 */
public class ResultPrinter implements Printer {

    public static synchronized void printProgress(String prefix, long count, long total) {
        String message;
        if (count < 0) {
            message = "...";
        } else {
            double ratio = (double) count / (double) total;
            message = Format.formatPercentage(ratio);
        }

        printProgress(prefix, message);
    }

    public static synchronized void printDone(String prefix) {
        printProgress(prefix, "100%");
        CONSOLE.println();
    }

    public static synchronized void printProgress(String prefix, String message) {
        CONSOLE.carriageReturn();
        CONSOLE.print(prefix);
        CONSOLE.setColor(Color.Cyan);
        CONSOLE.print(message);
        CONSOLE.resetStyle();
        CONSOLE.flush();
    }

    private static final String STATUS_PREFIX = "Creating report... ";
    private final DuplicatesParameters parameters;
    private final List<List<File>> allDuplicates;

    public ResultPrinter(DuplicatesParameters parameters, Collection<List<File>> allDuplicates) {
        this.parameters = parameters;
        this.allDuplicates = new ArrayList<>(allDuplicates);
    }

    @Override
    public void printResult(Console c) {
        sortResults();
        writeToHtml(c);
    }

    private void writeToHtml(Console c) {
        c.print("Duplicates found for ");
        c.setBold();
        for (File dir : parameters.getInputFiles()) {
            c.print(dir.getAbsolutePath() + " ");
        }
        c.resetBold();
        c.print(": ");
        c.setColor(Color.Red);
        c.print(allDuplicates.size());
        c.resetStyle();
        c.println(" duplicates found.\n");


        c.print("Comparator(s) : ");
        c.setColor(Color.Blue);
        c.println(parameters.getIndexer());
        c.resetStyle();

        c.print("Filter(s) : ");
        c.setColor(Color.Blue);
        c.println(parameters.getFilters());
        c.resetStyle();

        c.print("Files analyzed : ");
        c.setColor(Color.Blue);
        c.println(GlobalIndexProgress.filesCount);
        c.resetStyle();

        if (GlobalIndexProgress.checkSumIndexProgress > 0) {
            c.print("Checksum computed : ");
            c.setColor(Color.Blue);
            c.println(Format.formatSize(GlobalIndexProgress.checkSumIndexProgress) +
                    " (" + Format.formatPercentage((double) GlobalIndexProgress.checkSumIndexProgress / (double) GlobalIndexProgress.totalFilesSize) + ")");
            c.resetStyle();
        }

        long toSaveSize = 0;
        int count = 0;
        c.println();
        c.print("<table border=\"1\">"
                + "<tr>"
                + "<th>Size</th>"
//                + "<th>Comparator info</th>"
                + "<th>File path</th>"
                + "</tr>");

        final int total = allDuplicates.size();
        boolean pairLine = true;
        for (List<File> list : allDuplicates) {
            String color = pairLine ? "white" : "lightgrey";
            toSaveSize += printDuplicates(c, list, color);

            pairLine = !pairLine;
            count++;
            printProgress(STATUS_PREFIX, count, total);
        }
        printDone(STATUS_PREFIX);
        c.print("</table>");

        c.resetStyle();
        c.println();
        c.println();
        c.print("About ");
        c.setBold();
        c.setColor(Color.Green);
        c.setBold();
        c.print(Format.formatSize(toSaveSize));
        c.resetStyle();
        c.println(" could be saved.");
    }

    private void sortResults() {
        printProgress(STATUS_PREFIX, "SORTING");
        Comparator<List<File>> comparator = Comparator.comparingLong(l -> l.get(0).length());
        Collections.sort(allDuplicates, comparator.reversed());
    }


    private static final int MAX_CELL_SIZE = 30;

    private long printDuplicates(Console console, List<File> list, String bgColor) {
        long toSave = 0;
        boolean first = true;
        for (File file : list) {
            if (first) {
                first = false;
            } else {
                toSave += file.length();
            }

//            String comparatorInfo = comparator.info(file);
//            int ciLength = comparatorInfo.length();
//            if (ciLength > MAX_CELL_SIZE) {
//                comparatorInfo = comparatorInfo.substring(0, MAX_CELL_SIZE);
//                comparatorInfo += "... (" + (ciLength - MAX_CELL_SIZE) + ")";
//            }
            console.print("<tr>"
                    + "<td bgcolor=" + bgColor + ">" + Format.formatSize(file.length()) + "</td>"
//                    + "<td bgcolor=" + bgColor + ">" + comparatorInfo + "</td>"
                    + "<td bgcolor=" + bgColor + ">" + file.getAbsolutePath() + "</td>"
                    + "</tr>");
        }

        return toSave;
    }

}
