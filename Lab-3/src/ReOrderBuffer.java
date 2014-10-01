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
        while (buffer.peek() != null ) {
        	System.out.println("Peek loop");
            Entry entry = buffer.peek();
            if(entry.instruction.type==Global.STORE)
            {
            	if(loadStoreUnit.StoreQueueIsFull() || !entry.instruction.outOfRS)
            	{
            		break;
            	}
            	//Store Instruction
            	entry=buffer.poll();
            	int memDestination = entry.instruction.valueA;
            	int memSourceValue = entry.instruction.valueB;
            	loadStoreUnit.addStoreEntry(entry.instruction, memDestination, memSourceValue);
            }
            else if(entry.instruction.type==Global.LOAD && arf.rrf.renameRegisters[buffer.peek().rrfTag].valid)
            {
            	//Load Instruction
            	//If there is nothing in the store queue with the same location, add to load
            	if(loadStoreUnit.LoadQueueIsFull())
            	{
            		break;
            	}
            	entry=buffer.poll();
            	int loadSourceLocation=entry.instruction.valueA;
            	if(!loadStoreUnit.isMatching(0))
            	{
            		loadStoreUnit.addLoadEntry(entry.instruction, entry.rrfTag, loadSourceLocation);
            	}
            }
            else if(arf.rrf.renameRegisters[buffer.peek().rrfTag].valid)
            {
            	entry=buffer.poll();
                arf.updateRegister(entry.instruction.destination.value, entry.rrfTag);
                System.out.println(entry.instruction.instructionId + ": Removed from ROB");
            }
            else
            {
            	break;
            }
            //arf.updateRegister(entry.instruction.destination.value, entry.rrfTag);
            //System.out.println(entry.instruction.instructionId + ": Removed from ROB");

        }
    }
}
