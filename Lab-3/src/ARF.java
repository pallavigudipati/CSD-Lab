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
}
