package duplicates;

import com.wx.action.Action;
import com.wx.action.arg.ArgumentsSupplier;
import com.wx.console.UserConsoleInterface;
import com.wx.console.file.HtmlConsole;
import com.wx.util.representables.string.EnumCasterLC;
import file.filters.SizeFilter;
import indexer.impl.ChainedIndexer;
import indexer.IndexImpl;
import indexer.IndexerInterface;
import result.ResultPrinter;
import util.GlobalIndexProgress;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main process
 *
 * @author Canale
 */
public class Duplicates implements Action {

	@Override
	public void execute(UserConsoleInterface in, ArgumentsSupplier args) {
		DuplicatesParameters parameters = initParameters(args);


		// File walk
		List<File> files = parameters.walkFiles();


		// Init progress
		GlobalIndexProgress.initFrom(files);
		PrintThread printThread = new PrintThread();
		printThread.start();

		// Compute
		Collection<List<File>> duplicates = parameters.indexFiles(files);

		// Stop progress thread
		printThread.stop = true;
		try {
			printThread.join();
		} catch (InterruptedException e) {/* Ignored */}


		// Print result
		try {
			HtmlConsole html = new HtmlConsole(getReportFile());
			new ResultPrinter(parameters, duplicates).printResult(html);
			html.close();
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Cannot write file " + getReportFile() + ": " + e.getMessage());
		}

	}

	private DuplicatesParameters initParameters(ArgumentsSupplier args) {
		// Load args
		List<String> inputPaths = args.supplyStringList();
		List<String> indexNames = args.supplyStringList();
		int minDuplicates = args.supplyInteger();
		int minSize = args.supplyInteger() * 1000; // Size arguments are in KB
		int maxSize = args.supplyInteger() * 1000;
		int csMaxDepth = args.supplyInteger();
		int csSize = args.supplyInteger();
		int csStep = args.supplyInteger();

		// Build
		DuplicatesParameters.Builder builder = new DuplicatesParameters.Builder();
		builder.addInputFilesPath(inputPaths);

		EnumCasterLC<IndexImpl> caster = new EnumCasterLC<>(IndexImpl.class);
		List<IndexImpl> indexersImpl = indexNames.stream()
				.map(caster::castOut).collect(Collectors.toList());
		checkIndexers(indexersImpl);

		List<IndexerInterface> indexers = indexersImpl.stream()
				.map(e -> e.init(csMaxDepth, csSize, csStep))
				.collect(Collectors.toList());
		builder.setIndexer(new ChainedIndexer(indexers));
		builder.setMinGroupSize(minDuplicates);

		if (minSize > 0 || maxSize > 0) {
			builder.addFilters(new SizeFilter(minSize, maxSize));
		}

		return builder.build();
	}

	private void checkIndexers(List<IndexImpl> indexers) {
		if (new HashSet<>(indexers).size() != indexers.size()) {
			throw new IllegalArgumentException("Comparators can be used only once each");
		}

		int sumCompIndex = indexers.indexOf(IndexImpl.SUM);
		if (sumCompIndex >= 0 && !indexers.subList(0,sumCompIndex).contains(IndexImpl.SIZE)) {
			throw new IllegalArgumentException("A 'size' indexers must precede a 'sum' indexer");
		}
	}


	private File getReportFile() {
//        return new File("Duplicates report " + Format.formatDate(new Date().getTime(), "HH:mm:ss") + ".html");
		return new File("Duplicates report.html");
	}


	private class PrintThread extends Thread {

		private static final int PRINT_INTERVAL = 500;
		private boolean stop = false;

		@Override
		public void run() {
			while (!stop) {
				GlobalIndexProgress.print();
				try {
					sleep(PRINT_INTERVAL);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			GlobalIndexProgress.print();
			Main.CONSOLE.println();
		}
	}
}
