
public class Instruction {
    
    public class Address {
        public Boolean isRegister;
        public Integer value;

        public Address(Boolean isRegister, Integer value) {
            this.isRegister = isRegister;
            this.value = value;
        }
    }

    int type;
    Address sourceA;
    Address sourceB;
    Address destination;
}
