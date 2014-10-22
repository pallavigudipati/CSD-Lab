
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

        public void remove() {
            
        }
    }

    public CacheLine[] cacheLines = new CacheLine[Globals.cacheSize];

    public void insert(int blockNumber) {
        if (cacheLines[blockNumber].isEmpty) {
            cacheLines[blockNumber].initialize(blockNumber);
        } else {
            cacheLines[blockNumber].remove();
        }
    }

    // A call from Bus.
    public void update(int blockNumber) {
        
    }
}
