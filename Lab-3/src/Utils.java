import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/*
 * Basic utilities class for the whole package.
 */
public class Utils {

    public List<Instruction> parseInstructions(String fileName) {
        List<Instruction> instructions = new ArrayList<Instruction>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                Instruction instruction = new Instruction();
                int type = getInstructionType(parts[0]);
                getOperand(parts[1], instruction.destination);
                getOperand(parts[2], instruction.sourceA);
                if (!(type == Global.LOAD || type == Global.STORE)) {
                    getOperand(parts[3], instruction.sourceB);
                }
                instructions.add(instruction);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Not able to read files: " + fileName);
            e.printStackTrace();
        }
        return instructions;
    }

    public int getInstructionType(String command) {
        if (command.equals("ADD")) {
            return 1;
        } else if (command.equals("SUB")) {
            return 2;
        } else if (command.equals("MUL")) {
            return 3;
        } else if (command.equals("DIV")) {
            return 4;
        } else if (command.equals("AND")) {
            return 5;
        } else if (command.equals("OR")) {
            return 6;
        } else if (command.equals("XOR")) {
            return 7;
        } else if (command.equals("STORE")) {
            return 8;
        } else if (command.equals("LOAD")) {
            return 9;
        } else {
            return -1;
        }
    }

    public void getOperand(String operand, Instruction.Address address) {
        if (operand.charAt(0) == 'R') {
            address.isRegister = true;
            address.value = Integer.parseInt(operand.substring(1));
        } else {
            address.isRegister = false;
            address.value = Integer.parseInt(operand);
        }
    }
}
