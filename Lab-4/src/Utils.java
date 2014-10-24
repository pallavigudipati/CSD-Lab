import java.util.Random;

/*
 * General Utilities.
 */

public class Utils {

    // Just in case something complicated comes up.
    public static int getCacheLineNumber(int blockNumber) {
        return blockNumber % Globals.cacheSize;
    }

    // Random Coin Toss
    public static boolean tossCoin() {
        Random randomDouble = new Random();
        int randomNumber = randomDouble.nextInt(1);
        if (randomNumber == 0) {
            return false;
        }
        return true;
    }

    public static int randomBlock() {
        Random randomDouble = new Random();
        return randomDouble.nextInt(Globals.mainMemorySize - 1);
    }

    public static String statesToString(int oldState, int newState) {
        return new String(Globals.State.names[oldState - Globals.State.offset]
                + " -> " + Globals.State.names[newState - Globals.State.offset]);
    }

    public static String requestTypeToString(int requestType) {
        if (requestType == Globals.CACHE_TO_CACHE) {
            return new String("Cache-To-Cache");
        } else if (requestType == Globals.INVALIDATE_BLOCKS) {
            return new String("Invalidate Blocks");
        } else if (requestType == Globals.CANCEL_REQUEST) {
            return new String("Cancel Request");
        }
        return null;
    }
}
