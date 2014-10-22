
/*
 * General Utilities.
 */

public class Utils {

    // Just in case something complicated comes up.
    public static int getCacheLineNumber(int blockNumber) {
        return blockNumber % Globals.cacheSize;
    }
}
