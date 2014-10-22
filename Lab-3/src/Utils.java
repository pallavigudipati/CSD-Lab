import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Queue;

/*
 * Basic utilities class for the whole package.
 */
public class Utils {

    public static Parameters parseParameters(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            int sizeOfRS = Integer.parseInt(reader.readLine());
            int sizeOfROB = Integer.parseInt(reader.readLine());
            int sizeOfSB = Integer.parseInt(reader.readLine());
            int sizeOfLB = Integer.parseInt(reader.readLine());

            int[] latency = new int[Global.MAX_OPERATIONS];
            for (int i = 0; i < Global.MAX_OPERATIONS; ++i) {
                latency[i] = Integer.parseInt(reader.readLine());
            }
            reader.close();
            return new Parameters(sizeOfRS, sizeOfROB, sizeOfSB, sizeOfLB, latency);
        } catch (Exception e) {
            System.out.println("Not able to read files: " + fileName);
            e.printStackTrace();
            return null;
        }
    }

    public static Queue<Instruction> parseInstructions(String fileName) {
        Queue<Instruction> instructions = new LinkedList<Instruction>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = null;
            int id = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                Instruction instruction = new Instruction();
                instruction.type = getInstructionType(parts[0]);
                if (!(instruction.type == Global.LOAD || instruction.type == Global.STORE)) 
                {
                    getOperand(parts[1], instruction.destination);
                    getOperand(parts[2], instruction.sourceA);                	
                    getOperand(parts[3], instruction.sourceB);
                }
                else if(instruction.type==Global.LOAD)
                {
                    getOperand(parts[1], instruction.destination);
                    getOperand(parts[2], instruction.sourceA);
                }
                else if(instruction.type==Global.STORE)
                {
                    getOperand(parts[1], instruction.sourceA);
                    getOperand(parts[2], instruction.sourceB);                	
                }
                instruction.instructionId = id;
                id += 1;
                instructions.add(instruction);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Not able to read files: " + fileName);
            e.printStackTrace();
        }
        return instructions;
    }

    public static int getInstructionType(String command) {
        if (command.equals("ADD")) {
            return Global.ADD;
        } else if (command.equals("SUB")) {
            return Global.SUB;
        } else if (command.equals("MUL")) {
            return Global.MUL;
        } else if (command.equals("DIV")) {
            return Global.DIV;
        } else if (command.equals("AND")) {
            return Global.AND;
        } else if (command.equals("OR")) {
            return Global.OR;
        } else if (command.equals("XOR")) {
            return Global.XOR;
        } else if (command.equals("STORE")) {
            return Global.STORE;
        } else if (command.equals("LOAD")) {
            return Global.LOAD;
        } else {
            return -1;
        }
    }

    public static void getOperand(String operand, Instruction.Address address) {
        if (operand.charAt(0) == 'R') {
            address.isRegister = true;
            address.value = Integer.parseInt(operand.substring(1)) - 1;
        } else {
            address.isRegister = false;
            address.value = Integer.parseInt(operand);
        }
    }
}
