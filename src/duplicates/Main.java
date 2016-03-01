package duplicates;


import checksum.PartialSumBuilder;
import com.wx.action.cmd.CommandContext;
import com.wx.console.Console;
import com.wx.console.UserConsoleInterface;
import com.wx.console.system.UnixSystemConsole;
import com.wx.grammar.ParseException;
import indexer.IndexImpl;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Canale
 */
public class Main {

    public static Console CONSOLE = new UnixSystemConsole() {
        @Override
        public void probeWidthBuffer() {
            setWidthBuffer(-1);
        }
    };

    public static void main(String[] args) throws IOException, ParseException {
        CommandContext ctx = new CommandContext.Builder(false)
                .loadDescriptionsFrom("/Commands.sp",
                        formatEnumConstants(IndexImpl.class),
                        PartialSumBuilder.DEFAULT_SUM_SIZE,
                        PartialSumBuilder.DEFAULT_STEP_SIZE
                )
                .addHelpCommand()
                .attachAction("duplicates", new Duplicates())
                .addDefaultAction("duplicates").build(new UserConsoleInterface(CONSOLE, "", ""));

        if (args == null || args.length == 0 || isHelp(args[0])) {
            args = new String[] {"help", "duplicates"};
        }
        ctx.runOrExecute(args);
    }

    private static boolean isHelp(String arg) {
        return arg.equals("help") || arg.equals("-h") || arg.equals("--help");
    }

    private static String formatEnumConstants(Class<? extends  Enum<?>> cls) {
        Enum<?>[] enumConstants = cls.getEnumConstants();

        return "[" + Stream.of(enumConstants)
                .map(e -> e.name().toLowerCase())
                .collect(Collectors.joining(", ")) + "]";
    }

}
