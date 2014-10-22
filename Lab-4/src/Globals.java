
/*
 * All the global variables. 
 */

public class Globals {

    // Specifications.
    public static int cacheSize = 32; // In blocks.
    public static int mainMemorySize = 1024; // In blocks.
    public static int blockSize = 32; // In bytes.
    public static int busSize = 32; // In bits. TODO Find its significance.

    // States.
    public static class State {
        public static int MODIFIED = 1;
        public static int EXCLUSIVE = 2;
        public static int SHARED = 3;
        public static int INVALID = 4;
        public static int OWNED = 5;
    }

    // Read/ Write
    public static int READ = 10;
    public static int WRITE = 11;
}
