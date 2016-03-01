//package result;
//
//import com.wx.console.Console;
//import duplicates.ResultSet;
//
//import java.io.File;
//import java.util.*;
//
///**
// *
// * @author Canale
// */
//public class DirStatsPrinter implements Printer {
//
//    private static final String PREFIX1 = "Performing directory stats... ";
//    private static final String PREFIX2 = "Writing second report... ";
//
//    private final List<List<File>> allDuplicates;
//    private final Map<File, Integer> hitsCount = new HashMap<>();
//
//
//    public DirStatsPrinter(ResultSet result) {
//        this.allDuplicates = result.getAllDuplicates();
//    }
//
//    @Override
//    public void printResult(Console c) {
//        List<Map.Entry<File, Integer>> stats = generateStats();
//        cleanStats(stats);
//        writeResult(c,stats);
//    }
//
//    private void writeResult(Console c, List<Map.Entry<File, Integer>> stats) {
//        c.print("<table border=\"1\">"
//                + "<tr>"
//                + "<th>Hit count</th>"
//                + "<th>Directory</th>"
//                + "</tr>");
//        int count = 0;
//        final int total = stats.size();
//        for (Map.Entry<File, Integer> entry : stats) {
//            c.print("<tr>"
//                    + "<td>" + entry.getValue() + "</td>"
//                    + "<td>" + entry.getKey().getAbsolutePath() + "</td>"
//                    + "</tr>");
//            ResultPrinter.printProgress(PREFIX2, count, total);
//        }
//        ResultPrinter.printDone(PREFIX2);
//        c.print("</table>");
//    }
//
//    private void cleanStats(List<Map.Entry<File, Integer>> stats) {
//
//        int index = 0;
//        while (index < stats.size()) {
//            File dir = stats.get(index).getKey();
//            index++;
//            if (index < stats.size()) {
//                removeSubdirs(dir, stats.listIterator(index));
//            }
//            ResultPrinter.printProgress(PREFIX1, index, stats.size());
//        }
//
//        ResultPrinter.printDone(PREFIX1);
//    }
//
//    private void removeSubdirs(File superDir, Iterator<Map.Entry<File, Integer>> it) {
//        while (it.hasNext()) {
//            File dir = it.next().getKey();
//            if (isSubDir(superDir, dir)) {
//                it.remove();
//            }
//        }
//
//
//    }
//
//    private boolean isSubDir(File superDir, File subDir) {
//        return subDir.getAbsolutePath().startsWith(superDir.getAbsolutePath());
//    }
//
//    private List<Map.Entry<File, Integer>> generateStats() {
//        int count = 0;
//        final int total = allDuplicates.size();
//        for (List<File> list : allDuplicates) {
//            for (File dup : list) {
//                increment(dup.getParentFile());
//            }
//            ResultPrinter.printProgress(PREFIX1, count, total);
//        }
//
//
//        List<Map.Entry<File, Integer>> resultList =
//                new LinkedList<>(hitsCount.entrySet());
//        ResultPrinter.printProgress(PREFIX1, "SORTING");
//        Collections.sort(resultList, (o1, o2) -> o2.getValue() - o1.getValue());
//
//
//        return resultList;
//    }
//
//    private void increment(File file) {
//        Integer actualCount = hitsCount.get(file);
//        if (actualCount == null) {
//            hitsCount.put(file, 1);
//        } else {
//            hitsCount.put(file, actualCount + 1);
//        }
//    }
//
//}
