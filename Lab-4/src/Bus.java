import java.util.ArrayList;
import java.util.List;


public class Bus {

    public Logger logger;
    // List of all the Caches listening.
    public int protocolType;

    private List<Cache> caches = new ArrayList<Cache>();

    public Bus(Logger logger, int protocolType) {
        this.logger = logger;
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
                logger.logCoherenceRequest(Globals.CACHE_TO_CACHE, cache.debugId);
                return Globals.State.SHARED;
            } else if (state == Globals.State.SHARED) {
                logger.logCoherenceRequest(Globals.CACHE_TO_CACHE, cache.debugId);
                return Globals.State.SHARED;
            } else if (state == Globals.State.MODIFIED) {
                if (protocolType == Globals.MOESI) {
                    // You can share dirty cache.
                    // -------------------- MOESI ---------------------------
                    cache.changeState(blockNumber, Globals.State.OWNED);
                    logger.logCoherenceRequest(Globals.CACHE_TO_CACHE, cache.debugId);
                    return Globals.State.SHARED;
                }
                logger.logCoherenceRequest(Globals.CANCEL_REQUEST, cache.debugId);
                cache.changeState(blockNumber, Globals.State.INVALID);
                return Globals.FAILED;
            }
        }
        return Globals.State.EXCLUSIVE;
    }

    public void invalidateModifiedBlocks(int blockNumber, int ownerCache) {
        for (Cache cache : caches) {
            if (cache.debugId == ownerCache) {
                continue;
            }
            int state = cache.getBlockState(blockNumber);
            if (state == Globals.State.SHARED) {
                cache.changeState(blockNumber, Globals.State.INVALID);
            }
        }
    }

    public int requestBlockForWrite(int blockNumber, int processorId) {
        for (Cache cache : caches) {
            int state = cache.getBlockState(blockNumber);
            if (state == Globals.State.EXCLUSIVE || state == Globals.State.SHARED) {
                invalidateModifiedBlocks(blockNumber, processorId);
                logger.logCoherenceRequest(Globals.INVALIDATE_BLOCKS, cache.debugId);
                return Globals.State.MODIFIED;
            } else if (state == Globals.State.MODIFIED) {
                // Write back to main memory
                cache.changeState(blockNumber, Globals.State.INVALID);
                logger.logCoherenceRequest(Globals.CANCEL_REQUEST, cache.debugId);
                return Globals.FAILED;
            }
        }
        return Globals.State.MODIFIED;
    }
}
