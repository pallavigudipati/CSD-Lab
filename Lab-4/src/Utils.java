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
}
