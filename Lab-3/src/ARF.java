/*
 * Architected Register File
 */
public class ARF {

    public class Register {
        public boolean busy = false;
        public int tag = -1;
        public double value;
    }

    public Register[] registers = new Register[Global.NUM_REGISTERS];
    public RRF rrf;

    public ARF(RRF rrf) {
        this.rrf = rrf;
    }

    public double readRegister(int registerNum) {
        Register register = registers[registerNum];
        if (!register.busy) {
            return register.value;
        } else if (rrf.renameRegisters[register.tag].valid) {
            return rrf.renameRegisters[register.tag].value;
        } else {
            // TODO pass the tag to the reservation station.
            return 0;
        }
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
