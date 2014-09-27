/*
 * Arithmetic Logic Unit
 */
public class ALU {

    public boolean busy = false;
    public Instruction instruction;
    public ARF arf;

    private int[] latency;
    private int usageType = -1;
    private int startCycle = -1;
    private int operandA;
    private int operandB;
    private int destinationRRFTag;

    public ALU(int[] latency, ARF arf) {
        this.latency = latency;
        this.arf = arf;
    }

    public boolean isReady(int currentCycle) {
        return busy && startCycle + latency[usageType] <= currentCycle; // TODO: check
    }

    public int[] pushResult() {
        int result = computeResult();
        arf.rrf.updateRegister(destinationRRFTag, result);
        reset();
        return new int[] { destinationRRFTag, result };
    }

    public void startExecution(Instruction instruction, int currentCycle, int operandA,
            int operandB, int destinationRRFTag) {
        this.busy = true;
        this.instruction = instruction;
        this.usageType = instruction.type;
        this.startCycle = currentCycle; // TODO: Not accounting for 0 latencies.
        this.operandA = operandA;
        this.operandB = operandB;
        this.destinationRRFTag = destinationRRFTag;
    }

    public void reset() {
        usageType = -1;
        busy = false;
        startCycle = -1;
        instruction = null;
    }

    public int computeResult() {
        switch (usageType) {
            case Global.ADD: return operandA + operandB;
            case Global.SUB: return operandA - operandB;
            case Global.MUL: return operandA * operandB;
            case Global.DIV: return operandA / operandB;
            case Global.OR: return operandA | operandB;
            case Global.AND: return operandA & operandB;
            case Global.XOR: return operandA ^ operandB;
            default: return -1;
        }
    }
}
