
public class Cache {

    public class CacheBlock {
        public int state = -1;
        public int blockNumber = -1;

        public void changeState(int blockNumber) {
            // TODO state machine.
        }
    }

    public class CacheLine {
        public boolean isEmpty = true;
        public CacheBlock cacheBlock = new CacheBlock();

        public void initialize(int blockNumber) {
            this.isEmpty = false;
            this.cacheBlock.changeState(blockNumber);
        }
    }

    public CacheLine[] cacheLines = new CacheLine[Globals.cacheSize];

    public void insert(int cacheLineNumber, int blockNumber) {
        cacheLines[cacheLineNumber].initialize(blockNumber);
    }

    public void remove(int cacheLineNumber) {
        
    }

    // Updates state.
    public void updateState(int blockNumber, int action, int source) {
        
    }

    public void fetch(int blockNumber) {
        int cacheLineNumber = Utils.getCacheLineNumber(blockNumber);
        CacheLine cacheLine = cacheLines[cacheLineNumber];
        if (!cacheLine.isEmpty && cacheLine.cacheBlock.blockNumber == blockNumber) {
            return;
        }
        // Fetch cache block.
        if (!cacheLine.isEmpty) {
            // If occupied, remove the block.
            remove(cacheLineNumber);
        }
        // Insert new block.
        insert(cacheLineNumber, blockNumber);
    }
}
