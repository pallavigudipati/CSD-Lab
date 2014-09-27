/*
 * Architected Register File
 */
public class ARF {

    public class Register {
        public boolean busy = false;
        public int tag = -1;
        public int value;
    }

    public Register[] registers = new Register[Global.NUM_REGISTERS];
    public RRF rrf;

    public ARF(RRF rrf) {
        this.rrf = rrf;
    }

    // Returns {boolean, double} -> boolean: isValue, double: value or tag 
    public Object[] readRegister(int registerNum) {
        Register register = registers[registerNum];
        Object[] toReturn = new Object[2];
        if (!register.busy) {
            toReturn[0] = true;
            toReturn[1] = register.value;
        } else if (rrf.renameRegisters[register.tag].valid) {
            toReturn[0] = true;
            toReturn[1] = rrf.renameRegisters[register.tag].value;
        } else {
            // Passes the tag to the reservation station.
            toReturn[0] = false;
            toReturn[1] = register.tag;
        }
        return toReturn;
    }

    // Returns False if the allocation was not a success.
    public boolean allocateRegister(int registerNum) {
        Register register = registers[registerNum];
        int emptyRegister = rrf.findEmptyRegister();
        if (emptyRegister < 0) {
            return false;
        }
        RRF.RenameRegister renameRegister = rrf.renameRegisters[emptyRegister];
        renameRegister.allocate();
        register.tag = emptyRegister;
        register.busy = true;
        return true;
    }

    // Returns False if the RRF is not valid yet.
    public boolean updateRegister(int registerNum, int tag) {
        Register register = registers[registerNum];
        RRF.RenameRegister renameRegister = rrf.renameRegisters[tag];
        if (!renameRegister.valid) {
            return false;
        }
        register.value = renameRegister.reset();
        if (tag == register.tag) {
            register.busy = false;
        }
        return true;
    }
}
