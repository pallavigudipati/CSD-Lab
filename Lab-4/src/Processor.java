
public class Processor {
    public int debugId; // Used purely for debugging purposes.

    public Cache cache = new Cache();
    public Bus bus;

    public Processor(int debugId) {
        this.debugId = debugId;
    }

    public void read(int blockNumber) {
        // TODO Someone should send back a local copy if it is stored.
        bus.notify(blockNumber, Globals.READ);
        cache.fetch(blockNumber);
    }

    public void write(int blockNumber) {
        cache.fetch(blockNumber);
        cache.updateState(blockNumber, Globals.WRITE, Globals.PROCESSOR);
        bus.notify(blockNumber, Globals.WRITE);
    }

    public void update(int blockNumber, int action) {
        cache.updateState(blockNumber, action, Globals.BUS);
    }
}
