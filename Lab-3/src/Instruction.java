/*
 * A single instruction after it is parsed.
 */
public class Instruction {
    
    public class Address {
        public Boolean isRegister;
        public Integer value;
        /*
        public Address(Boolean isRegister, Integer value) {
            this.isRegister = isRegister;
            this.value = value;
        }*/
    }

    int instructionId; // Purely for the purpose of debugging.

    int startCycle;
    int endCycle;
    int type;
    Address sourceA = new Address();
    Address sourceB = new Address();
    Address destination = new Address();
}
