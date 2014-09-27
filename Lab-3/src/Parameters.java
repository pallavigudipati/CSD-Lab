/*
 * All the parameters that a user can input.
 */
public class Parameters {

    /*
    public class Latency {
        public
        public int add;
        public int sub;
        public int mul;
        public int div;
        public int and;
        public int or;
        public int xor;

        public Latency(int add, int sub, int mul, int div, int and, int or, int xor) {
            this.add = add;
            this.sub = sub;
            this.mul = mul;
            this.div = div;
            this.and = and;
            this.or = or;
            this.xor = xor;
        }
    }*/

    public int sizeOfRS; // Reservation Station
    public int sizeOfROB; // Re-order Buffer
    public int sizeOfSB; // Store Buffer
    public int sizeOfLB; //Load Buffer Size
    public int[] latency; // Can be accessed using Global.type
    // public Latency latency;

    public Parameters(int sizeOfRS, int sizeOfROB, int sizeOfSB, int sizeOfLB, int[] latency) {
        this.sizeOfRS = sizeOfRS;
        this.sizeOfROB = sizeOfROB;
        this.sizeOfSB = sizeOfSB;
        this.sizeOfLB = sizeOfLB;
        this.latency = latency;
    }
}