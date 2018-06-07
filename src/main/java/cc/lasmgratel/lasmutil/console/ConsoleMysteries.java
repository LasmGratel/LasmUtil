package cc.lasmgratel.lasmutil.console;

import java.util.Scanner;

public interface ConsoleMysteries {
    /**
     * Get current Java environment encoding with magic.
     */
    static String getEncoding() {
        return System.getProperty("file.encoding");
    }

    /**
     * Pong! Pong! Pong! Create an interactive scanner!
     */
    static Scanner createScanner() {
        return new Scanner(System.in, getEncoding());
    }
}
