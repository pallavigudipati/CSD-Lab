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
        switch (command) {
            case "ADD": return 1;
            case "SUB": return 2;
            case "MUL": return 3;
            case "DIV": return 4;
            case "AND": return 5;
            case "OR": return 6;
            case "XOR": return 7;
            case "STORE": return 8;
            case "LOAD": return 9;
            default: return -1;
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
