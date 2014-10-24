/*
 * All the global variables. 
 */

public class Globals {

    // Processors
    public static int numProcessors = 2;

    // Iterations.
    public static int numIterations = 100000;

    // Specifications.
    public static int cacheSize = 32; // In blocks.
    public static int mainMemorySize = 1024; // In blocks.
    public static int blockSize = 32; // In bytes.
    public static int busSize = 4; // In bytes. Never used.
    public static int wordSize = 4; // In bytes.

    // States.
    public static class State {
        public static int MODIFIED = 1;
        public static int EXCLUSIVE = 2;
        public static int SHARED = 3;
        public static int INVALID = 4;
        public static int OWNED = 5;

        // For printing purposes.
        public static int offset = 1;
        public static String[] names = new String[] {"M", "E", "S", "I", "O"};
    }

    // If a request fails.
    public static int FAILED = 16;

    // Protocol Types.
    public static int MESI = 17;
    public static int MOESI = 18;

    // Coherence Requests.
    public static int CACHE_TO_CACHE = 20;
    public static int CANCEL_REQUEST = 21;
    public static int INVALIDATE_BLOCKS = 22;
}
