/*
 * All the global variables of this package.
 */
public class Global {
	// ALU operations
	public final static int MAX_OPERATIONS = 7;
	public final static int ADD = 1;
	public final static int SUB = 2;
	public final static int MUL = 3;
	public final static int DIV = 4;
	public final static int AND = 5;
	public final static int OR = 6;
	public final static int XOR = 7;

	// Load and Store
	public final static int STORE = 8;
	public final static int LOAD = 9;

	// Number of registers in ARF and RRF
	public final static int NUM_REGISTERS = 8;
	public final static int NUM_RENAME_REGISTERS = 8; // TODO ??

	// Number of ALUs
	public static int NUM_ALU = 2;

	// Default value in a location in memory
	public static int MEM_DEFAULT_VALUE = 5;
}