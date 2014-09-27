/*
 * Rename Register File
 */
public class RRF {

    public class RenameRegister {
        public boolean valid = false;
        public boolean busy = false;
        public double value;

        public double reset() {
            valid = false;
            busy = false;
            return value;
        }

        public void allocate() {
           valid = false;
           busy = true;
        }
    }

    public RenameRegister[] renameRegisters = new RenameRegister[Global.NUM_RENAME_REGISTERS];

    // TODO: should we maintain the first empty register name?
    public int findEmptyRegister() {
        for (int i = 0; i < Global.NUM_RENAME_REGISTERS; ++i) {
            if (!renameRegisters[i].busy) {
                return i;
            }
        }
        return -1; // RRF is full.
    }

    public void updateRegister(int tag, double value) {
        RenameRegister register = renameRegisters[tag];
        register.value = value;
        register.valid = true;
    }
}
