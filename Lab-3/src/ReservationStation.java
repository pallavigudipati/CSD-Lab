import java.util.LinkedList;
import java.util.List;

/*
 * Reservation Station.
 */
public class ReservationStation {

    public class Entry {

        public class Operand {
            public boolean valid = false;
            public int tagOrValue;

            public Operand(boolean valid, int tagOrValue) {
                this.valid = valid;
                this.tagOrValue = tagOrValue;
            }
        }

        public boolean busy = false;
        public boolean ready = false;
        public Instruction instruction;
        public Operand operandA;
        public Operand operandB;
        public Operand destination; // TODO: Operand C. How do we handle this.

        public Entry(Instruction instruction) throws RRFFullException {
            this.busy = true;
            this.instruction = instruction;
            setOperands();
        }

        // Wont be necessary if we are deleting the entry
        public void reset() {
            busy = false;
            ready = false;
            instruction = null;
            operandA = null;
            operandB = null;
            destination = null;
        }

        public void checkReady() {
            if (operandA.valid && operandB.valid) {
                ready = true;
            } 
        }

        private void setOperands() throws RRFFullException {
            // OperandA : doing both operands separately as it might differ for load and store.
            if (instruction.sourceA.isRegister) {
                Object[] registerContents = arf.readRegister(instruction.sourceA.value);
                operandA = new Operand((boolean) registerContents[0], (int) registerContents[1]);
            } else {
                operandA = new Operand(true, instruction.sourceA.value);
            }

            // OperandB.
            if (instruction.sourceB.isRegister) {
                Object[] registerContents = arf.readRegister(instruction.sourceB.value);
                operandB = new Operand((boolean) registerContents[0], (int) registerContents[1]);
            } else {
                operandB = new Operand(true, instruction.sourceB.value);
            }
            checkReady();

            // TODO: For load and store
            if (instruction.destination.isRegister) {
                if (arf.allocateRegister(instruction.destination.value)) {
                    destination = new Operand(true, -1);
                } else {
                    throw new RRFFullException("No empty registers.");
                }
            } else {
                destination = new Operand(true, -1);
            }
        }
    }

    public List<Entry> buffer = new LinkedList<Entry>();
    public ARF arf;
    public int maxLength;

    public ReservationStation(ARF arf, int maxLength) {
        this.arf = arf;
        this.maxLength = maxLength;
    }

    public boolean isFull() {
        return buffer.size() == maxLength;
    }

    // The PipelineManager should check whether the Station is full or not.
    public void fillEntry(Instruction instruction) throws RRFFullException {
        Entry entry = new Entry(instruction);
        buffer.add(entry);
    }

    public void forward(int tag, int value) {
        for (Entry entry : buffer) {
            if (!entry.operandA.valid && entry.operandA.tagOrValue == tag) {
                entry.operandA.tagOrValue = value;
                entry.operandA.valid = true;
            }
            if (!entry.operandB.valid && entry.operandB.tagOrValue == tag) {
                entry.operandB.tagOrValue = value;
                entry.operandB.valid = true;
            }
            entry.checkReady();
        }
    }

    // Returns null if no entry is ready. Also remove the entry from the Station.
    public Entry getFirstReadyEntry() {
        for (int i = 0; i < buffer.size(); ++i) {
            if (buffer.get(i).ready) {
                Entry entry = buffer.get(i);
                buffer.remove(i);
                return entry;
            }
        }
        return null;
    }
}
