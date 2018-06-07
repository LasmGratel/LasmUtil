package cc.lasmgratel.lasmutil.console;

import static cc.lasmgratel.lasmutil.UtilManager.getLogger;

/**
 * Punish the unruly damn console!
 */
public interface ConsolePunisher {
    /**
     * Set encoding to Planeptune language OwO
     */
    static void setEncoding(String chcpCode) {
        try {
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "chcp", chcpCode).inheritIO();
            Process p = pb.start();
            p.waitFor();
        } catch (Exception e) {
            getLogger().accept("Your program cannot change encoding because of magic!");
            e.printStackTrace();
        }
    }
}
