import java.util.ArrayList;
import java.util.List;


public class Bus {

    // List of all the Caches listening.
    public List<Cache> caches = new ArrayList<Cache>();
    public int protocolType;

    public Bus(int protocolType) {
        this.protocolType = protocolType;
    }

    public void addListener(Cache cache) {
        caches.add(cache);
        cache.bus = this;
    }

    public int requestBlockForRead(int blockNumber) {
        for (Cache cache : caches) {
            int state = cache.getBlockState(blockNumber);
            if (state == Globals.State.EXCLUSIVE) {
                cache.changeState(blockNumber, Globals.State.SHARED);
                return Globals.State.SHARED;
            } else if (state == Globals.State.SHARED) {
                return Globals.State.SHARED;
            } else if (state == Globals.State.MODIFIED) {
                if (protocolType == Globals.MOESI) {
                    // You can share dirty cache.
                    // -------------------- MOESI ---------------------------
                    cache.changeState(blockNumber, Globals.State.OWNED);
                    return Globals.State.SHARED;
                }
                return Globals.FAILED;
            }
        }
        return Globals.State.EXCLUSIVE;
    }

    public void invalidateModifiedBlocks(int blockNumber) {
        for (Cache cache : caches) {
            int state = cache.getBlockState(blockNumber);
            if (state == Globals.State.SHARED) {
                cache.changeState(blockNumber, Globals.State.INVALID);
            }
        }
    }

    public int requestBlockForWrite(int blockNumber) {
        for (Cache cache : caches) {
            int state = cache.getBlockState(blockNumber);
            if (state == Globals.State.EXCLUSIVE || state == Globals.State.SHARED) {
                invalidateModifiedBlocks(blockNumber);
                return Globals.State.MODIFIED;
            } else if (state == Globals.State.MODIFIED) {
                // Write back to main memory
                cache.changeState(blockNumber, Globals.State.INVALID);
                return Globals.FAILED;
            }
        }
        return Globals.State.MODIFIED;
    }
}
