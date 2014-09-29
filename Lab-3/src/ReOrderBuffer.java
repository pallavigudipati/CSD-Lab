import java.util.LinkedList;
import java.util.Queue;

/*
 * Re-order Buffer
 */
public class ReOrderBuffer {

    public class Entry {
        public boolean busy = false;
        // public boolean finished = false; Not required, can directly use RRF.valid
        public int rrfTag = -1;
        public Instruction instruction;

        public Entry(Instruction instruction, int rrfTag) {
            this.busy = true;
            this.instruction = instruction;
            // TODO: what if destination is a mem address?
            this.rrfTag = rrfTag;
        }
    }

    public Queue<Entry> buffer = new LinkedList<Entry>();
    public int maxLength;
    public ARF arf;

    public ReOrderBuffer(ARF arf, int maxLength) {
        this.maxLength = maxLength;
        this.arf = arf;
    }

    public boolean isFull() {
        return buffer.size() == maxLength;
    }

    // The PipelineManager has to check whether the buffer is full or not. 
    public void fillEntry(Instruction instruction, int rrfTag) {
        Entry entry = new Entry(instruction, rrfTag);
        buffer.add(entry);
    }

    public void completePending() {
        while (buffer.peek() != null && arf.rrf.renameRegisters[buffer.peek().rrfTag].valid) {
            Entry entry = buffer.poll();
            arf.updateRegister(entry.instruction.destination.value, entry.rrfTag);
            System.out.println(entry.instruction.instructionId + ": Removed from ROB");
        }
    }
}
