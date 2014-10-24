
public class Processor {
    public int debugId; // Used purely for debugging purposes and to report statistics.

    private Cache cache;

    public Processor(Logger logger, int debugId) {
        this.cache = new Cache(logger, debugId);
        this.debugId = debugId;
    }

    public void read(int blockNumber) {
        cache.read(blockNumber);
    }

    public void write(int blockNumber) {
        cache.write(blockNumber);
    }

    public Cache getCache() {
        return this.cache;
    }
}