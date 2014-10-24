
public class Processor {
    public int debugId; // Used purely for debugging purposes.

    public Cache cache = new Cache();

    public Processor(int debugId) {
        this.debugId = debugId;
    }

    public void read(int blockNumber) {
        cache.read(blockNumber);
    }

    public void write(int blockNumber) {
          cache.write(blockNumber);
    }
}
