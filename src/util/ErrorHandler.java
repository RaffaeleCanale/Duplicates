package util;

import java.io.File;

import static duplicates.Main.CONSOLE;

/**
 * Created on 01/03/2016
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class ErrorHandler {


    public static void fileIgnored(File file, Exception e) {
        CONSOLE.carriageReturn();
        CONSOLE.errln("Ignored file: " + file);
        CONSOLE.errln("    " + e.getMessage());
        CONSOLE.flush();
    }
}
