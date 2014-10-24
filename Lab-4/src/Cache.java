public class Cache {

    public class CacheBlock {
        // TODO: make state a private variable so that collection of stuff is easier.
        public int state = -1;
        public int blockNumber = -1;

        public void changeState(int blockNumber, int state) {
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
            this.cacheBlock.changeState(blockNumber, state);
            return oldState;
        }
    }

    public Bus bus;
    public CacheLine[] cacheLines = new CacheLine[Globals.cacheSize];

    public Cache() {
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
        int cacheLinenNumber = Utils.getCacheLineNumber(blockNumber);
        CacheLine cacheLine = cacheLines[cacheLinenNumber];
        if (!cacheLine.isEmpty && cacheLine.cacheBlock.blockNumber == blockNumber) {
            return cacheLine.cacheBlock.state;
        }
        return Globals.State.INVALID;
    }

    public void changeState(int blockNumber, int state) {
        int cacheLinenNumber = Utils.getCacheLineNumber(blockNumber);
        CacheLine cacheLine = cacheLines[cacheLinenNumber];
        cacheLine.cacheBlock.state = state;
    }

    public void read(int blockNumber) {
        if (checkBlock(blockNumber)) {
            // TODO: Statistics?
        } else {
            int state = bus.requestBlockForRead(blockNumber);
            if (state == Globals.FAILED) {
                state = bus.requestBlockForRead(blockNumber); // Retry the request.
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
        } else if (oldState == Globals.State.OWNED) {
            // --------------------MOESI-----------------------
            changeState(blockNumber, Globals.State.MODIFIED);
            bus.invalidateModifiedBlocks(blockNumber);
        }
        // The block is not present.
        int newState = bus.requestBlockForWrite(blockNumber);
        if (newState == Globals.FAILED) {
            // Retry
            newState = bus.requestBlockForWrite(blockNumber);
        }
        changeState(blockNumber, newState);
    }
}
