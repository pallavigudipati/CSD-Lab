/*
 * Rename Register File
 */
public class RRF {

    public class RenameRegister {
        public boolean valid = false;
        public boolean busy = false;
        public double value;
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
}
