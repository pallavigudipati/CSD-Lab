/*
 * Arithmetic Logic Unit
 */
public class ALU {

    private int[] latency;
    private int usageType = -1;
    private boolean busy = false;
    private int startCycle = -1;

    public ALU(int[] latency) {
        this.latency = latency;
    }

    // Return False if it is busy.
    public boolean updateState(int currentCycle) {
        if (busy && startCycle + latency[usageType] < currentCycle) {
            return false;
        }
        reset();
        return true;
    }

    public void startExecution(int usageType, int currentCycle) {
        this.busy = true;
        this.usageType = usageType;
        this.startCycle = currentCycle; // TODO: Not accounting for 0 latencies.
    }

    public void reset() {
        usageType = -1;
        busy = false;
        startCycle = -1;
    }
}
