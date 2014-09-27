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

        public Entry(Instruction instruction) {
            this.busy = true;
            this.instruction = instruction;
            // TODO: what if destination is a mem address?
            rrfTag = arf.registers[instruction.destination.value].tag;
        }
    }

    public Queue<Entry> buffer = new LinkedList<Entry>();
    public int maxLength;
    public ARF arf;

    public ReOrderBuffer(ARF arf, int maxLength) {
        this.maxLength = maxLength;
        this.arf = arf;
    }

    // Returns False if the buffer is full. 
    public boolean fillEntry(Instruction instruction) {
        if (buffer.size() == maxLength) {
            return false;
        }
        Entry entry = new Entry(instruction);
        buffer.add(entry);
        return true;
    }

    public void completePending() {
        while (arf.rrf.renameRegisters[buffer.peek().rrfTag].valid) {
            Entry entry = buffer.poll();
            arf.updateRegister(entry.instruction.destination.value, entry.rrfTag);
        }
    }
}
