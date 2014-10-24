public class Cache {

    public class CacheBlock {
        // TODO: make state a private variable so that collection of stuff is easier.
        public int blockNumber = -1;
        private int state = Globals.State.INVALID;

        public void changeState(int state) {
            logger.logStateChange(this.state, state, debugId);
            this.state = state;
        }
    }

    public class CacheLine {
        public boolean isEmpty = true;
        public CacheBlock cacheBlock = new CacheBlock();

        public int initialize(int blockNumber, int state) {
            int oldState = Globals.State.INVALID;
            if (this.isEmpty) {
                oldState = cacheBlock.state;
            }
            this.isEmpty = false;
            this.cacheBlock.blockNumber = blockNumber;
            this.cacheBlock.changeState(state);
            return oldState;
        }
    }

    public int debugId;
    public Bus bus;
    public Logger logger;
    public CacheLine[] cacheLines = new CacheLine[Globals.cacheSize];

    public Cache(Logger logger, int debugId) {
        this.debugId = debugId;
        this.logger = logger;
        for (int i = 0; i < cacheLines.length; ++i) {
            cacheLines[i] = new CacheLine();
        }
    }

    public void insert(int blockNumber, int state) {
        int cacheLineNumber = Utils.getCacheLineNumber(blockNumber);
        int oldState = cacheLines[cacheLineNumber].initialize(blockNumber, state);
        // if (oldState == Globals.State.MODIFIED) {
            // Write back is done. 
        // }
    }

    // Checks whether the processor can perform a read or write on it.
    public boolean checkBlock(int blockNumber) {
        int cacheLinenNumber = Utils.getCacheLineNumber(blockNumber);
        CacheLine cacheLine = cacheLines[cacheLinenNumber];
        if (!cacheLine.isEmpty && cacheLine.cacheBlock.blockNumber == blockNumber) {
            int state = cacheLine.cacheBlock.state;
            if (state == Globals.State.EXCLUSIVE || state == Globals.State.MODIFIED
                    || state == Globals.State.SHARED) {
                return true;
            }
        }
        return false;
    }

    public int getBlockState(int blockNumber) {
        int cacheLineNumber = Utils.getCacheLineNumber(blockNumber);
        CacheLine cacheLine = cacheLines[cacheLineNumber];
        if (!cacheLine.isEmpty && cacheLine.cacheBlock.blockNumber == blockNumber) {
            return cacheLine.cacheBlock.state;
        }
        return Globals.State.INVALID;
    }

    public void changeState(int blockNumber, int state) {
        int cacheLinenNumber = Utils.getCacheLineNumber(blockNumber);
        CacheLine cacheLine = cacheLines[cacheLinenNumber];
        cacheLine.cacheBlock.changeState(state);
    }

    public void read(int blockNumber) {
        if (checkBlock(blockNumber)) {
            // TODO: Statistics?
        } else {
            int state = bus.requestBlockForRead(blockNumber);
            if (state == Globals.FAILED) {
                state = bus.requestBlockForRead(blockNumber); // Retry the request.
                if (state == Globals.FAILED) {
                    System.out.println("Something wrong in read");
                }
            }
            insert(blockNumber, state);
        }
    }

    public void write(int blockNumber) {
        int oldState = getBlockState(blockNumber);
        // if MODIFIED,do nothing.
        if (oldState == Globals.State.EXCLUSIVE) {
            changeState(blockNumber, Globals.State.MODIFIED);
        } else if (oldState == Globals.State.SHARED) {
            changeState(blockNumber, Globals.State.MODIFIED);
            bus.invalidateModifiedBlocks(blockNumber);
            logger.logCoherenceRequest(Globals.INVALIDATE_BLOCKS, debugId);
        } else if (oldState == Globals.State.OWNED) {
            // --------------------MOESI-----------------------
            changeState(blockNumber, Globals.State.MODIFIED);
            bus.invalidateModifiedBlocks(blockNumber);
            logger.logCoherenceRequest(Globals.INVALIDATE_BLOCKS, debugId);
        }
        // The block is not present.
        int newState = bus.requestBlockForWrite(blockNumber);
        if (newState == Globals.FAILED) {
            // Retry
            newState = bus.requestBlockForWrite(blockNumber);
            if (newState == Globals.FAILED) {
                System.out.println("Something wrong in write");
            }
        }
        changeState(blockNumber, newState);
    }
}
