package utils;

public class Logger {
    private static boolean VERBOSE = true;

    public static void setVerbose(boolean verbose) {
        VERBOSE = verbose;
    }

    public static void log(String log) {
        if (VERBOSE) {
            System.out.println(log);
        }
    }
}
