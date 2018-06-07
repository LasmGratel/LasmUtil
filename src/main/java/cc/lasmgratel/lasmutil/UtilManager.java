package cc.lasmgratel.lasmutil;

import java.util.function.Consumer;

public class UtilManager {
    private static Consumer<Object> logger = System.out::println;

    public static Consumer<Object> getLogger() {
        return logger;
    }

    public static void setLogger(Consumer<Object> logger) {
        UtilManager.logger = logger;
    }
}
