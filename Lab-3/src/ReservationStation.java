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

        public Entry(Instruction instruction) {
            this.busy = true;
            this.instruction = instruction;
            setOperands();
        }

        // WOnt be necessary if we are deleting the entry
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

        private void setOperands() {
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
            // TODO: Destination
            /*
            if (instruction.destination.isRegister) {
                
                destination = 
            } else {
                destination = new Operand(true, -1);
            }
            */
        }
    }

    public List<Entry> buffer = new LinkedList<Entry>();
    public ARF arf;
    public int maxLength;

    public ReservationStation(ARF arf, int maxLength) {
        this.arf = arf;
        this.maxLength = maxLength;
    }

    // return false if the reservation station is full.
    public boolean fillEntry(Instruction instruction) {
        if (buffer.size() == maxLength) {
            return false;
        }
        Entry entry = new Entry(instruction);
        buffer.add(entry);
        return true;
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

    // TODO dispatch once ALU is written
}
