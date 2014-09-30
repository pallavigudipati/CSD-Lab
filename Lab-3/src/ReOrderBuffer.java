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
    public LoadStoreUnit loadStoreUnit;

    public ReOrderBuffer(ARF arf, int maxLength,LoadStoreUnit loadStoreUnit) {
        this.maxLength = maxLength;
        this.arf = arf;
        this.loadStoreUnit = loadStoreUnit;
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
            if(entry.instruction.type==8)
            {
            	//Store Instruction
            	loadStoreUnit.addStoreEntry(entry.instruction, 0, 0);
            }
            else if(entry.instruction.type==9)
            {
            	//Load Instruction
            	//If there is nothing in the store queue with the same location, add to load
            	if(!loadStoreUnit.isMatching(0))
            	{
            		loadStoreUnit.addLoadEntry(entry.instruction, 0, 0);
            	}
            }
            else
            {
            	arf.updateRegister(entry.instruction.destination.value, entry.rrfTag);
            }
        }
    }
}
